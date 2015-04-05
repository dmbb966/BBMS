package utilities;

import gui.GUI_NB;
import hex.Hex;
import hex.HexMap;
import hex.HexOff;
import hex.VaporEnum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Vector;

import jneat.Organism;
import jneat.Population;
import bbms.COA;
import bbms.GlobalFuncs;
import clock.Clock;
import terrain.TerrainEnum;
import unit.FitnessTypeEnum;
import unit.OrganismTypeEnum;
import unit.SideEnum;
import unit.Unit;
import unit.WaypointList;

public class FIO {
	static Charset cSet = Charset.forName("US-ASCII");
	static int readFinger = 0;

	public static boolean BWriteFile(Path p, String s, StandardOpenOption opt) {
		return BWriteFile(p, s, opt, true);
	}
	
	public static boolean BWriteFile(Path p, String s, StandardOpenOption opt, boolean newLine) {
		try (BufferedWriter writer = Files.newBufferedWriter(p, cSet, opt)) {
			if (newLine) writer.write(s + "\n", 0, s.length() + 1);
			else writer.write(s, 0, s.length());
		} catch (IOException e) {
			System.err.format("IO Error writing to file: %s%n", e);
			return false;
		}
		
		return true;		
	}

	public static File newFile(String s) {
		File f = new File(s);
		try {
			f.createNewFile();
		} catch (IOException e) {
			System.err.format("IO Exception when creating new file: %s%n", e);
			return null;
		}
		
		return f;
	}

	public static String BReadFile(Path p, String s) {
		String output = "";
		
		try (BufferedReader reader = Files.newBufferedReader(p, cSet)) {
			char c = ' ';
			
			while (c != '|') {
				c = (char) reader.read();
				output += c;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return output;
		}
		return output;
	}		
	
	public static boolean SaveScen(Path p) {
		FIO.overwriteFile(p, "# Scenario file generated by BBMS");
		
		FIO.appendFile(p,  GlobalFuncs.saveMapCharacteristics());
		FIO.appendFile(p, GlobalFuncs.scenMap.saveMap());
		
		// Now saves the COAs
		for (int i = 0; i < GlobalFuncs.allCOAs.size(); i++) {
			COA finger = GlobalFuncs.allCOAs.elementAt(i);
			
			FIO.appendFile(p, finger.PrintCOA());
		}
		FIO.appendFile(p, ">Last COA<");
		
		return true;
	}
	
	public static boolean SaveFile(Path p) {
		FIO.overwriteFile(p, "");
		FIO.appendFile(p, GlobalFuncs.saveMapCharacteristics());
		FIO.appendFile(p, GlobalFuncs.scenMap.saveMap());
		
		// Stores unit information
		FIO.appendFile(p, "\n# Unit information");
		FIO.appendFile(p, "# Format is: unitID, callsign, x, y, hullOrientation, turretOrientation, type, side, spotted, waypoints\n");
		
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			FIO.appendFile(p, GlobalFuncs.unitList.elementAt(i).SaveUnit());
		}
		FIO.appendFile(p, ">Last Unit<\n");
		
		FIO.appendFile(p, "# Unit target information");
		FIO.appendFile(p, "# Format is: unitID, targetID");
		
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			Unit finger = GlobalFuncs.unitList.elementAt(i);
			if (finger.target != null) {
				FIO.appendFile(p,  finger.SaveTarget());
			}
		}
		
		FIO.appendFile(p, ">Last Target<\n");
			
		GlobalFuncs.allSpots.SaveSpots(p);
		
		if (GlobalFuncs.currentPop == null) return true;
		
		FIO.appendFile(p, "#Filename for the associated JNEAT population that will be saved");
		String popFileName = p.toString();
		popFileName = popFileName.substring(0, popFileName.length() - 4);
		popFileName = popFileName.concat(".pop");
		FIO.appendFile(p, popFileName);
		
		File f = new File(popFileName);
		if (!f.exists()) FIO.newFile(popFileName);
		Path popPath = f.toPath();
		GlobalFuncs.currentPop.SavePopulationToFile(popPath);
		
		return true;
	}
	
	public static boolean LoadScen(Path p) {
		try {
			BufferedReader reader = Files.newBufferedReader(p, cSet);
			String readL = "";
			
			GlobalFuncs.loadMapCharacteristics(reader);
			GlobalFuncs.scenMap.loadMap(reader);
			
			GlobalFuncs.allCOAs = new Vector<COA>();
			
			// Load units
			while(readL != null) {
				readL = reader.readLine();
				GUI_NB.GCO("String: >" + readL + "<");
				
				if (readL.contentEquals("")) { }
				else if (readL.startsWith("#")) {}
				else if (readL.contains(">Last COA<")) {
					break;
				}
				else if (readL.startsWith("COA")) {
					COA newCOA = new COA(reader, readL);
					GlobalFuncs.allCOAs.addElement(newCOA);
				}
			}
			
			// All COAs loaded, now load the last saved COA, already loaded in loadMapCharacteristics
			GlobalFuncs.curCOA = GlobalFuncs.allCOAs.elementAt(GlobalFuncs.COAIndex - 1);
			GlobalFuncs.curCOA.LoadCOA();
			
			GUI_NB.GCO("Scenario loaded successfully.");	
			
			
		}  catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean LoadFile(Path p) {
		int newMapDisplayX = 0;
		int newMapDisplayY = 0;	
		spotting.SpotRecords loadSpots = new spotting.SpotRecords();	
		// Stage in reading the save game file
		// 0 = Just started
		// 1 = Loading map hex information
		// 2 = Loading unit location information
		// 3 = Loading unit targeting information
		// 4 = Loading spotting information
		int mode = 0;
		
		try {
			BufferedReader reader = Files.newBufferedReader(p,  cSet);
			GlobalFuncs.loadMapCharacteristics(reader);
			GlobalFuncs.scenMap.loadMap(reader);
			mode = 2;
			
			String readL;			
			while ((readL = reader.readLine()) != null) {				
				if (readL.startsWith("#")) {} 			//GUI_NB.GCO("Comment follows: " + readL);
				else if (readL.contentEquals("")) {} 	//GUI_NB.GCO("Blank line: " + readL);
			
				// With the above non-data holding lines stripped out, only valid input will be evaluated here
				else {
					readFinger = 0;
					GUI_NB.GCO("Reading string: *" + readL + "*");
					
					switch (mode) {
					case 0:
						// GlobalFuncs.loadMapCharacteristics(reader);
						// GUI_NB.GCO("Map dimensions are " + newMapX + " by " + newMapY);
						// GUI_NB.GCO("Clock state is: " + Clock.time);
						
						// GlobalFuncs.initializeMap(newMapX, newMapY, true);			
						mode = 1;						
						
						break;
					case 1:
						// Now will read terrain hex data
						// Format is: TerrainEnumID, elevation, obstacle height, density, obscuration
						// Will load the hex at (loadHexX, loadHexY);
						
						// GlobalFuncs.scenMap.storeHex(loadHexX,  loadHexY,  new Hex(loadHexX, loadHexY, readL));
						
						/*
						TerrainEnum tEnum = TerrainEnum.valueOf(ReadNextChunk(readL, ','));
						int elevation = Integer.parseInt(ReadNextChunk(readL, ','));
						int obsHeight = Integer.parseInt(ReadNextChunk(readL, ','));
						int density = Integer.parseInt(ReadNextChunk(readL, ','));
						int obscuration = Integer.parseInt(ReadNextChunk(readL, ','));
						int vapor = Integer.parseInt(ReadNextChunk(readL, ','));
						int deltaVapor = Integer.parseInt(ReadNextChunk(readL, ','));
						VaporEnum vEnum = VaporEnum.valueOf(ReadNextChunk(readL, ','));
												
						GlobalFuncs.scenMap.storeHex(loadHexX, loadHexY, new Hex(loadHexX, loadHexY, tEnum, elevation, obsHeight, density, obscuration, vapor, deltaVapor, vEnum));
						*/
						
						/*
						loadHexX++;
						if (loadHexX == GlobalFuncs.scenMap.xDim) {
							loadHexY++;
							loadHexX = 0;
							
							if (loadHexY == GlobalFuncs.scenMap.yDim) {	
								mode = 2;
							}
						} */
						
						break;		
					case 2:
						if (readL.equals(">Last Unit<")) 
						{
							GUI_NB.GCO("Last unit reached");		
							mode = 3;							
						}
						else {							
							Unit newUnit = new Unit(readL);
							Hex locn = GlobalFuncs.scenMap.getHex(newUnit.location.x, newUnit.location.y);
							locn.HexUnit = newUnit;
							
							/*
							//# Unit information
							//# Format is: unitID, callsign, x, y, hullOrientation, turretOrientation, type, side, spotted, waypoints
							
							int unitID = Integer.parseInt(ReadNextChunk(readL, ','));
							String callsign = ReadNextChunk(readL, ',');
							int x = Integer.parseInt(ReadNextChunk(readL, ','));
							int y = Integer.parseInt(ReadNextChunk(readL, ','));
							double hullOrientation = Double.parseDouble(ReadNextChunk(readL, ','));
							double turretOrientation = Double.parseDouble(ReadNextChunk(readL, ','));
							String type = ReadNextChunk(readL, ',');
							String side = ReadNextChunk(readL, ',');
							SideEnum sideE = SideEnum.valueOf(side);
							boolean spotted = Boolean.parseBoolean(ReadNextChunk(readL, ','));
							
							int orgNumber = Integer.parseInt(ReadNextChunk(readL, ','));
							OrganismTypeEnum orgType = OrganismTypeEnum.valueOf(ReadNextChunk(readL, ','));
							FitnessTypeEnum fitType = FitnessTypeEnum.valueOf(ReadNextChunk(readL, ','));
							double curFitness = Double.parseDouble(ReadNextChunk(readL, ','));
							
							String wpStr = ReadNextChunk(readL, ')');
							WaypointList wpList =new WaypointList();						
							// Finds waypoints until there are no more to add				
							while (!wpStr.equals("")) {							
								GUI_NB.GCO("WP Str: >" + wpStr + "<");
								GUI_NB.GCO("WP Str Size: " + wpStr.length());
								HexOff newWP = WaypointList.readWaypoint(wpStr);
								wpList.addWaypoint(newWP.getX(), newWP.getY());
								
								wpStr = ReadNextChunk(readL, ')');
							}
							
							
							
							locn.HexUnit = new Unit(locn, sideE, type, callsign, hullOrientation, turretOrientation, wpList, spotted);
							
							locn.HexUnit.orgType = orgType;
							locn.HexUnit.fitType = fitType;
							locn.HexUnit.orgGenome = orgNumber;
							locn.HexUnit.curFitness = curFitness;
							*/
						}

						
						break;
					case 3:						
						// Loads unit target information
						if (readL.equals(">Last Target<"))
						{
							GUI_NB.GCO("Last target reached.");
							mode = 4;
						}
						else {
							int unitID = Integer.parseInt(ReadNextChunk(readL, ','));
							int targetID = Integer.parseInt(ReadNextChunk(readL, ','));
							
							Unit finger = GlobalFuncs.unitList.elementAt(unitID - 1);
							finger.target = GlobalFuncs.unitList.elementAt(targetID - 1);
							GUI_NB.GCO("Set unit " + finger.callsign + " to target " + finger.target.callsign);
						}

						
						break;
						
					case 4:
						// Loads spotting information
						if (readL.equals(">Last Spot<"))
						{
							GUI_NB.GCO("All spotting information read.");
							mode = 5;
						}
						else {
							// # Format: Time index, spotter unit ID, observed unit ID, observed unit x, observed unit y
							int spotTime = Integer.parseInt(ReadNextChunk(readL, ','));
							int spotterID = Integer.parseInt(ReadNextChunk(readL, ','));
							int observedID = Integer.parseInt(ReadNextChunk(readL, ','));
							int obsX = Integer.parseInt(ReadNextChunk(readL, ','));
							int obsY = Integer.parseInt(ReadNextChunk(readL, ','));
							
							Unit spotter = GlobalFuncs.unitList.elementAt(spotterID - 1);
							Unit observed = GlobalFuncs.unitList.elementAt(observedID - 1);
							HexOff loc = new HexOff(obsX, obsY);
							
							spotting.SpotReport readSpot = new spotting.SpotReport(spotTime, spotter, observed, loc);
							loadSpots.addReport(readSpot);
						}
						break;
						
					case 5:
						// Loads population information
						if (readL.contains(".pop")) {
							
							File popFile = new File(readL);
							if (!popFile.exists()) {
								GUI_NB.GCO("ERROR!  Could not read file: >" + readL + "<");
							}
							else {
								GUI_NB.GCO("Loading spotting info from file: >" + readL + "<");
								Path popPath = popFile.toPath();
								GlobalFuncs.currentPop = new Population(popPath);
								GUI_NB.GCO("Population file loaded.  Now loading organisms into units.");
								
								for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
									Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
									
									if (finger.orgGenome != -1) { 
										for (int j = 0; j < GlobalFuncs.currentPop.organisms.size(); j++) {
											Organism org = GlobalFuncs.currentPop.organisms.elementAt(j);
											
											if (finger.orgGenome == org.genome.genome_id) {
												GUI_NB.GCO("Assigning organism #" + org.genome.genome_id + " to unit " + finger.callsign);
												finger.org = org;
												break;
											}
										}
									}								
									
								}
							}
						} else {
							GUI_NB.GCO("No population associated with this save.");
						}
						mode = 6;
						
					default: 
						GUI_NB.GCO("Mode is " + mode + ", string: " + readL);
					}
					
					GlobalFuncs.allSpots = loadSpots;
					
					// Now that everything has been loaded, we can repaint the map at the location it was saved at.
					GlobalFuncs.gui.GMD.mapDisplayX = newMapDisplayX;
					GlobalFuncs.gui.GMD.mapDisplayY = newMapDisplayY;
					GlobalFuncs.gui.repaint();
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static String ReadNextChunk (String x, char stopper) {
		return ReadNextChunk(x, stopper, true);
	}
	
	public static String ReadNextChunk (String x, char stopper, boolean trim) {
		String chunk = "";
		char finger = ' ';
		int len = 0;		
		
		while (readFinger < x.length()) {
			finger = x.charAt(readFinger);
			
			// Reaches a delimiter
			if (finger == stopper) {
				readFinger++;
				
				if (trim) chunk = chunk.trim();
				GUI_NB.GCO("Stopper reached, returning chunk: |" + chunk + "|");
				return chunk;
			}
			else {
				readFinger++;
				chunk += finger;
				len++;				
			}
		}

		if (trim) {
			chunk = chunk.trim();
		}
		GUI_NB.GCO("EOL reached, returning chunk: |" + chunk + "|");
		return chunk;		
	}

	public static boolean overwriteFile(Path p, String s) {
		return BWriteFile(p, s, StandardOpenOption.TRUNCATE_EXISTING);
	}

	public static boolean appendFile(Path p, String s) {
		return BWriteFile(p, s, StandardOpenOption.APPEND, true);
	}
	
	public static boolean appendFile(Path p, String s, boolean newLine) {
		return BWriteFile(p, s, StandardOpenOption.APPEND, newLine);		
	}
}
