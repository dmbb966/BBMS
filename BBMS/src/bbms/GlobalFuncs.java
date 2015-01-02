package bbms;

import java.awt.Color;
import java.lang.Enum;
import java.awt.Desktop.Action;
import java.io.File;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.JLabel;

import unit.Unit;
import bbms.MersenneTwister;
import hex.*;

public class GlobalFuncs {
	
	public static GUI_NB gui;
	public static HexMap scenMap;
	public static boolean mapInitialized = false;
	public static int placeUnit = 0;
	public static boolean showShaded = true;	
	
	public static boolean RotateHull = true;
	
	public static Vector<unit.Unit> unitList = new Vector<Unit>();
	public static Vector<unit.Unit> friendlyUnitList = new Vector<Unit>();
	public static Vector<unit.Unit> enemyUnitList = new Vector<Unit>();	
	public static Hex highlightedHex = null; 
	public static Unit selectedUnit = null;
	public static Hex selectedHex = null;
	
	private static int unitCount = 0;
	
	public static spotting.SpotRecords allSpots = new spotting.SpotRecords();

	/**
	 * Random number object, uses the Mersenne Twister algorithm, coded in Java by Sean Luke (http://cs.gmu.edu/~sean/research/)
	 */
	public static MersenneTwister randGen = new MersenneTwister();
	
	/**
	 * Uses the Mersenne Twister to generate a random number between a minimum and maximum range (inclusive)
	 * Only uses integers.
	 */
	public static int randRange(int min, int max) {
		int delta = max - min + 1;
		return min + randGen.nextInt(delta);
	}
	
	/**
	 * Uses the Mersenne TWister to generate a random number given a mean and variance.  
	 * Only uses integers.
	 */
	public static int randMean (int mean, int var) {
		int min = mean - var;
		return min + randGen.nextInt(var * 2);
	}
	
	public static void initializeMap (int x, int y) {
		scenMap = new HexMap(x, y);
		mapInitialized = true;
		unitCount = 0;
		selectedUnit = null;
		highlightedHex = null;
		unitList = new Vector<Unit>();
		friendlyUnitList = new Vector<Unit>();
		enemyUnitList = new Vector<Unit>();
		GUI_NB.GCO("Generating main map.");		
	}
	
	/**
	 * If s is less than length, will return a new string with sufficient blank spaces concatanated on to the end
	 * to bring the string up to the desired length.  Returns the original string otherwise.
	 */
	public static String whiteFill (String s, int len) {
		if (s.length() >= len) return s;
		else {
			int whiteFill = len - s.length();
			for (int i = 0; i < whiteFill; i++) {
				s = s.concat(" ");
			}
			
			return s;
		}
	}
	
	/**
	 * Returns the current unit count
	 */
	public static int getUnitCount() {
		return unitCount;
	}
	
	/**
	 * Increments the total unit count by one and returns the new value
	 */
	public static int getNewUnitCount() {
		return unitCount += 1;
	}
	
	public static int normalizeAngle(int a) {
		if (a >= 360) return normalizeAngle(a -= 360);
		else if (a < 0) return normalizeAngle(a += 360);
		else return a;
	}
	
	public static double normalizeAngle(double a) {
		if (a >= 360) return normalizeAngle(a -= 360);
		else if (a < 0) return normalizeAngle(a += 360);
		else return a;
	}
	
	/**
	 * Saves the game.
	 */
	public static boolean saveState() {
		if (!GlobalFuncs.mapInitialized) return false;
		String saveFile = "src/saves/save.txt";
		File f = new File(saveFile);
		if (!f.exists()) FIO.newFile(saveFile);
		
		Path p = f.toPath();
		
		GlobalFuncs.scenMap.saveMap(p);
			
		return true;
	}
	
	public static boolean loadState() {
		// TODO: Reset all variables as if you were reinitializing the map
		
		String loadFile = "src/saves/save.txt";
		File f = new File(loadFile);
		if (!f.exists()) return false;
		
		Path p = f.toPath();
		FIO.LoadFile(p);
				
		return true;
	}

}
