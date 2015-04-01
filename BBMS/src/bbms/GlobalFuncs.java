package bbms;

import java.awt.Color;
import java.awt.MenuBar;
import java.lang.Enum;
import java.awt.Desktop.Action;
import java.io.File;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenuBar;

import jneat.Population;
import clock.Clock;
import clock.ClockThread;
import unit.Unit;
import utilities.FIO;
import utilities.MersenneTwister;
import gui.GUIKeyboard;
import gui.GUIMenu;
import gui.GUI_NB;
import hex.*;

public class GlobalFuncs {
	
	public static GUI_NB gui;
	public static HexMap scenMap;
	public static boolean mapInitialized = false;
	public static boolean clockInitialized = false;
	public static int placeUnit = 0;
	public static boolean showShaded = true;	
	public static boolean showVapor = false;
	public static boolean showLOS = true;
	public static boolean showFOW = false;	// Toggles fog of war
	
	public static boolean RotateHull = true;
	
	public static Vector<unit.Unit> unitList = new Vector<Unit>();
	public static Vector<unit.Unit> friendlyUnitList = new Vector<Unit>();
	public static Vector<unit.Unit> enemyUnitList = new Vector<Unit>();	
	public static Hex highlightedHex = null; 
	public static Unit selectedUnit = null;
	public static Hex selectedHex = null;
	
	public static Population currentPop = null;	
	public static String tempStr = "";				// Used for some dialog box results
	
	public static Thread GameClock = new Thread(new ClockThread());
	
	private static int unitCount = 0;
	
	// Vapor variables
	/** Universal modifier to vapor flow rate */	 
	public static double flowRate = 3.00;
	/** Maximum flow rate of the system - will adjust based on actual system performance */
	public static double flowRateCap = 2.95;
	/** Rate at which the flow rate increases or decreases */
	public static double flowStep = 0.05;
	/** Will increase the flow rate after this many stable ticks */
	public static int flowCheck = 5;
	/** If true, will reduce the flow rate for this tick */
	public static boolean reduceRate = false;
	/** Highest flow rate as calculated by sources and sinks*/
	public static int maxDelta = 0;		
	/** Number of ticks since the last source or sink was removed.  
	 * After being stable for ten ticks, the flow rate will increase.
	 * This throttling helps avoid overflow/underflow issues.   */
	public static int ticksStable = 0;
	/** Total amount of vapor on the map */
	public static long totalVapor = 0;
	/** Total vapor delta */
	public static int totalVaporDelta = 0;
	
	// Spotting variables
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
	 * Uses the Mersenne Twister to generate a random number given a mean and variance.  
	 * Only uses integers.
	 */
	public static int randMean (int mean, int var) {
		int min = mean - var;
		return min + randGen.nextInt(var * 2);
	}
	
	/** 
	 * Uses the Mersenne Twister to generate a random double between 0.0 and 1.0
	 */
	public static float randFloat() {
		return randGen.nextFloat();
	}
	
	/**
	 * Uses the Mersene Twister to return +1 or -1
	 */
    public static int randPosNeg() 
	{  
		 int n = randGen.nextInt();
		 if ((n % 2) == 0) return -1;
		 else return 1;
	}      
    
    /** 
     * Uses the Mersene Twister to return a random number with a Gaussian distribution
     */
    
    public static double randGauss() {
    	return randGen.nextGaussian();
    }
	
	public static void initializeMap (int x, int y, boolean loadMap) {
		scenMap = new HexMap(x, y, loadMap);
		mapInitialized = true;
		unitCount = 0;
		selectedUnit = null;
		highlightedHex = null;
		unitList = new Vector<Unit>();
		friendlyUnitList = new Vector<Unit>();
		enemyUnitList = new Vector<Unit>();		
		GUIKeyboard.initializeKeyCommands();
		Clock.SetTime(8, 24, 13);
		GlobalFuncs.ticksStable = 0;
		if (!clockInitialized) {
			GUI_NB.GCO("Starting clock");
			GameClock.start();
		}
				
		GUI_NB.GCO("Generating main map.");
		
		// Update menu to reflect added functionality that comes with a generated map
		GUIMenu menu = (GUIMenu) gui.getJMenuBar();
		menu.removeAll();		
		menu.GenerateMenu();						
		gui.setJMenuBar(menu); 
		
	}
	
	public static void initializeMap (int x, int y) {
		initializeMap(x, y, false);		
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
		
		FIO.SaveFile(p);				
			
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
