package bbms;

import hex.Hex;
import hex.HexMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import terrain.TerrainEnum;

public class FIO {
	static Charset cSet = Charset.forName("US-ASCII");
	static int readFinger = 0;

	public static boolean BWriteFile(Path p, String s, StandardOpenOption opt) {
		try (BufferedWriter writer = Files.newBufferedWriter(p, cSet, opt)) {
			writer.write(s + "\n", 0, s.length() + 1);
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
	
	public static boolean LoadFile(Path p) {
		// TODO: Reset old environment
		
		int newMapX = 0;
		int newMapY = 0;
		int loadHexX = 0;
		int loadHexY = 0;
		HexMap newMap = null;
		// Stage in reading the save game file
		// 0 = Just started
		// 1 = Loading map hex information
		// 2 = Loading unit information
		// 3 = Loading spotting information
		int mode = 0;
		
		try {
			BufferedReader reader = Files.newBufferedReader(p,  cSet);
			String readL;
			String chunk;
			while ((readL = reader.readLine()) != null) {				
				if (readL.startsWith("#")) {} 			//GUI_NB.GCO("Comment follows: " + readL);
				else if (readL.contentEquals("")) {} 	//GUI_NB.GCO("Blank line: " + readL);
			
				// With the above non-data holding lines stripped out, only valid input will be evaluated here
				else {
					readFinger = 0;
					GUI_NB.GCO("Reading string: " + readL);
					
					switch (mode) {
					case 0:
						// Just started reading the file
						// This string will contain the x, y coordinates
						
						// First chunk is going to be the new map X coordinate
						newMapX = Integer.parseInt(ReadNextChunk(readL, ','));
						newMapY = Integer.parseInt(ReadNextChunk(readL, ','));						
						
						GUI_NB.GCO("Map dimensions are " + newMapX + " by " + newMapY);
						
						newMap = new HexMap(newMapX, newMapY);						
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
						TerrainEnum tEnum = TerrainEnum.loadEnum(TerrainEnumID);
												
						newMap.storeHex(loadHexX, loadHexY, new Hex(loadHexX, loadHexY, tEnum, elevation, obsHeight, density, obscuration));
						
						loadHexX++;
						if (loadHexX == newMapX) {
							loadHexY++;
							loadHexX = 0;
							
							if (loadHexY == newMapY) mode = 2;
						}
						
						break;
					case 2:
						GlobalFuncs.scenMap = newMap;
						GlobalFuncs.gui.repaint();
						mode = 3;
						
						break;
					default: 
						GUI_NB.GCO("Mode is " + mode + ", string: " + readL);
					}
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
				GUI_NB.GCO("Stopper reached, returning chunk: " + chunk);
				return chunk;
			}
			else {
				readFinger++;
				if (len == 0 && finger == ' ') {
					// Do nothing since this is a leading space
				} 
				else {
					chunk += finger;
					len++;
				}
			}
		}
		
		GUI_NB.GCO("EOL reached, returning chunk: " + chunk);
		return chunk;		
	}

	public static boolean overwriteFile(Path p, String s) {
		return BWriteFile(p, s, StandardOpenOption.TRUNCATE_EXISTING);
	}

	public static boolean appendFile(Path p, String s) {
		return BWriteFile(p, s, StandardOpenOption.APPEND);
	}
}
