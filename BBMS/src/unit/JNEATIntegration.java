package unit;

import bbms.GlobalFuncs;
import gui.GUI_NB;
import hex.Hex;

public class JNEATIntegration {
			
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
				
				GUI_NB.GCO("Prospective hex: (" + prospective.x + ", " + prospective.y + ") is input: " + sensorInput + " with output: " + networkResult);
				
				if (networkResult > resultThreshold) {
					GUI_NB.GCO("Location accepted. Teleporting unit.");
					u.TeleportTo(prospective);
					u.DisplayLOSToRange(GlobalFuncs.visibility);
					foundSpot = true;
				}
			}
			
			if (errCount > maxErrCount) {
				resultThreshold -= 0.05;
				errCount = 0;
			}
		}
	}

}
