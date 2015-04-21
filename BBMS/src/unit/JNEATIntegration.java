package unit;

import java.io.File;
import java.nio.file.Path;

import utilities.FIO;
import clock.Clock;
import clock.ClockControl;
import jneat.Organism;
import jneat.Population;
import bbms.GlobalFuncs;
import gui.DialogFileName;
import gui.DialogLoadScen;
import gui.GUI_NB;
import hex.Hex;

public class JNEATIntegration {
	
	static double death_sum = 0.0;
	static int death_count = 0;
	
	// NOTE: Start of scenario is found in FIO
	public static void EndofScenario() {
		ClockControl.Pause();
		
		for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
			finger.org.AverageFitness(finger.fitType.EvaluateFitness(finger));
			GUI_NB.GCO("Unit " + finger.callsign + " has fitness " + finger.org.fitness);
			death_count++;
		}
		
		GUI_NB.GCO(":::DESTROYED UNITS:::");
		
		for (int i = 0; i < GlobalFuncs.destroyedUnitList.size(); i++) {
			Unit finger = GlobalFuncs.destroyedUnitList.elementAt(i);

			finger.org.AverageFitness(finger.fitType.EvaluateFitness(finger));			// Essentially penalizes for unit destruction as a destroyed sensor no longer spots
			GUI_NB.GCO("Unit " + finger.callsign + " has fitness " + finger.org.fitness);
			death_sum++;			
			death_count++;
		}
		
		PrintDetailedIter();				

		
		GlobalFuncs.iterationCount++;
		
		if (GlobalFuncs.newEpoch){
			PrintSummaryOutput();
			GlobalFuncs.curEpoch++;
			GUI_NB.GCO("Starting NEW EPOCH: #" + GlobalFuncs.curEpoch);
			String debugOutput = GlobalFuncs.currentPop.epoch();
			GUI_NB.GCO(debugOutput);
			FIO.appendFile(GlobalFuncs.detailedOutput, "New Epoch: #" + GlobalFuncs.curEpoch);
			FIO.appendFile(GlobalFuncs.detailedOutput, debugOutput);
			GlobalFuncs.currentRunsPerOrg = 0;
			GlobalFuncs.orgAssignNum = 0;
			death_count = 0;
			death_sum = 0.0;
			
			// Randomly chooses a COA
			if (!GlobalFuncs.randCOAEpoch) {
				GlobalFuncs.COAIndex = GlobalFuncs.randRange(0, GlobalFuncs.allCOAs.size() - 1);
				GlobalFuncs.curCOA = GlobalFuncs.allCOAs.elementAt(GlobalFuncs.COAIndex);
			}
		}
		
		if (GlobalFuncs.pauseNewIter || (GlobalFuncs.pauseNewEpoch && GlobalFuncs.newEpoch)) {
			// Pause at these iterations; since scenario already paused, will do so
		}
		else {
			if (GlobalFuncs.curEpoch > GlobalFuncs.maxEpochs) {
				// Pause when max epochs exceeded
			}
			else {
				ClockControl.Pause();	// Unpauses the scenario
			}
		}
		
		GlobalFuncs.maxPossibleSpots = 0;
		Clock.time = 0;
		
		ScenIterationSetup(GlobalFuncs.numScoutsPer);
	}
	
	/** Sequentially fill friendly units with Neural Nets from the population */
	public static void FillAllScouts() {
		if (GlobalFuncs.currentPop == null) {
			GUI_NB.GCO("ERROR!  Must generate a population first.");
			return;
		}		
		
		if (GlobalFuncs.currentPop.organisms.size() < GlobalFuncs.friendlyUnitList.size()) {
			GUI_NB.GCO("ERROR!  Not enough organisms to fill friendly units!");
			return;
		}
		
		int numOrgs = GlobalFuncs.currentPop.organisms.size();
		for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
			if (GlobalFuncs.orgAssignNum >= numOrgs) {
				
				GlobalFuncs.orgAssignNum = 0;
				GlobalFuncs.currentRunsPerOrg++;
				GUI_NB.GCO("Current runs per Org inc to: " + GlobalFuncs.currentRunsPerOrg);
			}
			
			if (GlobalFuncs.currentRunsPerOrg >= GlobalFuncs.maxRunsPerOrg && !GlobalFuncs.newEpoch) {
				GlobalFuncs.newEpoch = true;
				GUI_NB.GCO("Last iteration of the current epoch.  CurRuns: " + GlobalFuncs.currentRunsPerOrg + " out of max: " + GlobalFuncs.maxRunsPerOrg);
			}
			
			Organism org = GlobalFuncs.currentPop.organisms.elementAt(GlobalFuncs.orgAssignNum);
			GlobalFuncs.orgAssignNum++;
			
			GUI_NB.GCO("Assiging organism #" + org.genome.genome_id + " to friendly unit " + finger.callsign);
			finger.org = org;
		}				
	}
	
	/** Deploy all friendly units according to their neural nets */
	public static void DeployAll() {
		for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
			
			unit.JNEATIntegration.DeployOne(finger);
		}
	}
			
	/** Based on sensor evaluations, determine where to deploy this unit */
	public static void DeployOne(Unit u) {
		if (u.org == null) {
			GUI_NB.GCO("ERROR!  Unit " + u.callsign + " does not have an organism!");
			return;
		}
		
		boolean foundSpot = false;
		int errCount = 0;
		double resultThreshold = 0.75;
		int maxErrCount = 30;
		
		// Cycle through hex locations
		while (!foundSpot) {
			errCount++;
					
			Hex prospective = GlobalFuncs.scenMap.RandomHexReconZone();
			
			// Only chooses unoccupied hexes
			if (prospective.HexUnit == null) {
				// Gets sensor value for this hex
				double sensorInput = OrganismTypeEnum.NormalizedSenseFlowSingle(prospective);
				
				u.org.net.inputs.firstElement().LoadSensor(sensorInput);
				u.org.net.ActivateNetwork();
				
				double networkResult = u.org.net.outputs.firstElement().getActivation();
				
				//GUI_NB.GCO("Prospective hex: (" + prospective.x + ", " + prospective.y + ") is input: " + sensorInput + " with output: " + networkResult);
				
				if (networkResult > resultThreshold) {
					//GUI_NB.GCO("Location accepted. Teleporting unit.");
					u.TeleportTo(prospective);
					//u.DisplayLOSToRange(GlobalFuncs.visibility);
					foundSpot = true;
				}
			}
			
			if (errCount > maxErrCount) {
				resultThreshold -= 0.05;
				errCount = 0;
			}
		}
	}



	/** Prints the population summary for each epoch to file */
	public static void PrintSummaryOutput() {
		FIO.appendFile(GlobalFuncs.summaryOutput, PrintSummaryLine());
	}
	
	public static String PrintSummaryLine() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append(GlobalFuncs.curEpoch + ", ");
		buf.append(GlobalFuncs.currentPop.organisms.size() + ", ");
		buf.append(GlobalFuncs.currentPop.species.size() + ", ");
		buf.append(GlobalFuncs.currentPop.mean_fitness + ", ");
		buf.append(GlobalFuncs.currentPop.highest_fitness + ", ");
		buf.append(GlobalFuncs.currentPop.avg_fit_eliminated + ", ");
		buf.append((death_sum / death_count));
		
		return buf.toString();
	}
	
	public static String PrintSummaryKey() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("Epoch #, ");
		buf.append("Pop Size, ");
		buf.append("Num Species, ");
		buf.append("Avg Fitness, ");
		buf.append("Highest Fitness, ");
		buf.append("Avg Fit Eliminated, ");
		buf.append("Avg Death Rate");
		
		return buf.toString();
	}
	
	

	
	/** Prints the iteration results from this iteration to the iteration detail file */
	public static void PrintDetailedIter() {
		
		for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
			FIO.appendFile(GlobalFuncs.fullIterOutput, PrintDetailedItrLine(finger));
		}
		
		for (int i = 0; i < GlobalFuncs.destroyedUnitList.size(); i++) {
			Unit finger = GlobalFuncs.destroyedUnitList.elementAt(i);
			FIO.appendFile(GlobalFuncs.fullIterOutput, PrintDetailedItrLine(finger));
		}
		
	}
	
	public static String PrintDetailedIterKey() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("Iteration #, ");
		buf.append("Epoch #, ");
		buf.append("COA Name, ");
		buf.append("Organism #, ");
		buf.append("Unit Callsign, ");
		buf.append("Unit Coordinates, ");
		buf.append("Unit destroyed, ");
		buf.append("Spotting credits, ");
		buf.append("Final fitness, ");
		buf.append("Organism averaged fitness, ");
		buf.append("Organism number of averages");
		
		return buf.toString();
	}
	
	/** Puts it all on one line */
	public static String PrintDetailedItrLine(Unit u) {
		StringBuffer buf = new StringBuffer("");
		
		buf.append(GlobalFuncs.iterationCount + ", ");
		buf.append(GlobalFuncs.curEpoch + ", ");
		buf.append(GlobalFuncs.curCOA.name + ", ");
		buf.append(u.org.genome.genome_id + ", ");
		buf.append(u.callsign + ", ");
		buf.append(u.location.DisplayCoords() + ", ");
		buf.append(u.destroyed + ", ");
		buf.append(u.spotCredits + ", ");
		buf.append(u.spotCredits / GlobalFuncs.maxPossibleSpots + ", ");
		buf.append(u.org.fitness + ", ");
		buf.append(u.org.fitAveragedOver);
		
		return buf.toString();
		
	}
	
	public static void ScenIterationSetup() {
		DialogFileName x = new DialogFileName(GlobalFuncs.gui, true, "Num Friendly Units");
		x.setVisible(true);
		
		int friendlyUnits = Integer.parseInt(GlobalFuncs.tempStr);
		
		if (friendlyUnits < 1) {
			GUI_NB.GCO("ERROR!  Not a valid number.");
		}		
		else {
			JNEATIntegration.ScenIterationSetup(friendlyUnits);
		}
	}
	
	public static void ScenIterationFromFile() {
		String fullPath = "src/saves/" + GlobalFuncs.tempStr;
		File popFile = FIO.newFile(fullPath);
		if (!popFile.exists()) {
			GUI_NB.GCO("Error reading population file!");
			
			DialogLoadScen x = new DialogLoadScen(GlobalFuncs.gui, true);
			x.setVisible(true);
		}
		else {
			
			GUI_NB.GCO("Loading population data.");
			Path p = popFile.toPath();
			GlobalFuncs.currentPop = new Population(p);
			GUI_NB.GCO("Population data read.  Initializing scenario with " + GlobalFuncs.currentPop.organisms.size() + " orgs");
			GlobalFuncs.newEpoch = true;
			
			int numScouts = GlobalFuncs.currentPop.PopulationSlice(GlobalFuncs.percentPerRun);			
			
			JNEATIntegration.ScenIterationSetup(numScouts);
		}
	}

	/** Goes through the setup for this scenario, namely, for the current COA will initialize new units*/
	public static void ScenIterationSetup(int numScouts) {		

		GlobalFuncs.numScoutsPer = numScouts;
		GlobalFuncs.allSpots.records.clear();
		//GUI_NB.GCO("All spot records have been cleared.");
		
		// Randomly chooses a COA
		if (GlobalFuncs.randCOAEpoch) {
			GlobalFuncs.COAIndex = GlobalFuncs.randRange(0, GlobalFuncs.allCOAs.size() - 1);
			GlobalFuncs.curCOA = GlobalFuncs.allCOAs.elementAt(GlobalFuncs.COAIndex);
		}
				
		GlobalFuncs.curCOA.LoadCOA();
		if (numScouts == 0) return;
		
		GUI_NB.GCO("New scenario iteration: Populating " + numScouts + " scouts of pop size " + GlobalFuncs.currentPop.organisms.size());
		
		
		
		// First, eliminate friendly units from the unit roster
		for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
			finger.location.HexUnit = null;
			GlobalFuncs.unitList.remove(finger);					
		}
		GlobalFuncs.friendlyUnitList.clear();
		GlobalFuncs.destroyedUnitList.clear();	// These are already gone from the map from the last run.
		

		
		// Now adds units along the left map boundary
		for (int i = 0; i < numScouts; i++) {
			int col = i / GlobalFuncs.scenMap.yDim;
			int row = i % GlobalFuncs.scenMap.yDim;
			
			Hex destination = GlobalFuncs.scenMap.getHex(col, row);
			if (destination.HexUnit != null) {
				// GUI_NB.GCO("Destination hex occupied, moving to the next one.");
				numScouts++;
			}
			else {
				// Add friendly unit
				destination.HexUnit = new Unit(destination, SideEnum.FRIENDLY, "M1A2", "Scout " + i, 90.0, 0.0, null, true);
			}
		}		 
		
		// GUI_NB.GCO("Num friendlies: " + GlobalFuncs.friendlyUnitList.size());
		
		
		if (GlobalFuncs.newEpoch) {
			GUI_NB.GCO("New epoch: outputting network information to: " + GlobalFuncs.detailedOutput.toString());
			
			File netInfo = FIO.newFile("src/saves/" + GlobalFuncs.outputPrefix + "/" + GlobalFuncs.outputPrefix + "pop" + GlobalFuncs.curEpoch + ".pop");
			GlobalFuncs.currentPop.SavePopulationToFile(netInfo.toPath());
			
			//FIO.appendFile(GlobalFuncs.detailedOutput, "\n\n\n---->>> NEW EPOCH at iteration " + GlobalFuncs.iterationCount + "<<<----\n\n\n");
			//FIO.appendFile(GlobalFuncs.detailedOutput, GlobalFuncs.currentPop.SavePopulation());
			//FIO.appendFile(GlobalFuncs.detailedOutput, GlobalFuncs.currentPop.PrintPopulation());
			GlobalFuncs.newEpoch = false;
		}
				
		FillAllScouts();		// Puts a Org in each unit
		
		DeployAll();			// Deploys those units accordingly
		
		
		GlobalFuncs.gui.repaint();
	}

}
