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

import bbms.GlobalFuncs;
import clock.Clock;
import terrain.TerrainEnum;
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
	
	public static boolean SaveFile(Path p) {
		GlobalFuncs.scenMap.saveMap(p);
		
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
		
		return true;
	}
	
	public static boolean LoadFile(Path p) {
	
		int newMapX = 0;
		int newMapY = 0;
		int newMapDisplayX = 0;
		int newMapDisplayY = 0;
		int loadHexX = 0;
		int loadHexY = 0;	
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
						// Just started reading the file
						// This string will contain the x, y coordinates
						
						// First chunk is going to be the new map X coordinate
						newMapX = Integer.parseInt(ReadNextChunk(readL, ','));
						newMapY = Integer.parseInt(ReadNextChunk(readL, ','));
						newMapDisplayX = Integer.parseInt(ReadNextChunk(readL, ','));
						newMapDisplayY = Integer.parseInt(ReadNextChunk(readL, ','));
						Clock.time = Integer.parseInt(ReadNextChunk(readL, ','));
						
						GUI_NB.GCO("Map dimensions are " + newMapX + " by " + newMapY);
						GUI_NB.GCO("Clock state is: " + Clock.time);
						
						GlobalFuncs.initializeMap(newMapX, newMapY, true);			
						mode = 1;						
						
						break;
					case 1:
						// Now will read terrain hex data
						// Format is: TerrainEnumID, elevation, obstacle height, density, obscuration
						// Will load the hex at (loadHexX, loadHexY);
						
						int TerrainEnumID = Integer.parseInt(ReadNextChunk(readL, ','));
						int elevation = Integer.parseInt(ReadNextChunk(readL, ','));
						int obsHeight = Integer.parseInt(ReadNextChunk(readL, ','));
						int density = Integer.parseInt(ReadNextChunk(readL, ','));
						int obscuration = Integer.parseInt(ReadNextChunk(readL, ','));
						int vapor = Integer.parseInt(ReadNextChunk(readL, ','));
						int deltaVapor = Integer.parseInt(ReadNextChunk(readL, ','));
						int vType = Integer.parseInt(ReadNextChunk(readL, ','));
						TerrainEnum tEnum = TerrainEnum.loadEnum(TerrainEnumID);
						VaporEnum vEnum = VaporEnum.loadEnum(vType);
												
						GlobalFuncs.scenMap.storeHex(loadHexX, loadHexY, new Hex(loadHexX, loadHexY, tEnum, elevation, obsHeight, density, obscuration, vapor, deltaVapor, vEnum));
						
						loadHexX++;
						if (loadHexX == newMapX) {
							loadHexY++;
							loadHexX = 0;
							
							if (loadHexY == newMapY) {	
								mode = 2;
							}
						}
						
						break;		
					case 2:
						if (readL.equals(">Last Unit<")) 
						{
							GUI_NB.GCO("Last unit reached");		
							mode = 3;							
						}
						else {													
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
							int spotnum = Integer.parseInt(ReadNextChunk(readL, ','));
							boolean spotted = false;
							if (spotnum == 1) spotted = true;
							
							
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
							
							Hex locn = GlobalFuncs.scenMap.getHex(x, y);
							SideEnum sideE;
							switch (side) {
							case "FRIENDLY":
								sideE = SideEnum.FRIENDLY;
								break;
							case "ENEMY":
								sideE = SideEnum.ENEMY;
								break;
							default:
								sideE = SideEnum.NEUTRAL;
								break;
							}
							
							locn.HexUnit = new Unit(locn, sideE, type, callsign, hullOrientation, turretOrientation, wpList, spotted);
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
