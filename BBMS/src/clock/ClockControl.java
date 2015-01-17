package clock;

import gui.GUI_NB;

public class ClockControl {
	
    static byte timescale = 4;					// Enumeration of rate of time
    public static boolean paused = false;		// Bool to show if the simulation paused or not
    
    /**
     * Toggles whether or not the clock is paused
     */
    public static void Pause() {
        paused = !paused;
    }
    
    /**
     * Increases the timescale of the clock by one setting.  Maxes out at x12
     */ 
    public static void AccelTime() {
        if (timescale < 8) timescale++;
        GUI_NB.GCO("Timescale is now " + timescale);
    }
    
    /**
     * Decreases the timescale of the clock by one setting.  Minimum is x1/8
     */
    public static void DecelTime() {       
        if (timescale > 1) timescale--;
        GUI_NB.GCO("Timescale is now " + timescale);;                
     }
    
    /**
     * Returns the string corresponding to the current time scale rate 
     * e.g. "x1/8" for 1/8th speed
     * Returns "ERROR" if invalid rate is given
     * @return
     */
    public static String PrintTimeScale() {
        if (paused) return "Paused";
        switch (timescale) {
        	case 1: return "x1/8";
        	case 2: return "x1/4";
        	case 3: return "x1/2";
        	case 4: return "x1";
        	case 5: return "x2";
        	case 6: return "x4";
        	case 7: return "x8";
        	case 8: return "x12";
        	default: return "ERROR";
        }
    }
    
    /**
     * Returns the double representing the current time rate scale
     * e.g. "0.125" for 1/8th speed
     * Returns -1 if invalid rate is given
     * @return
     */
    public static double NumTimeScale() {
        switch (timescale) {
        	case 1: return 0.125;
        	case 2: return 0.250;
        	case 3: return 0.500;
        	case 4: return 1.000;
        	case 5: return 2.000;
        	case 6: return 4.000;
        	case 7: return 8.000;
        	case 8: return 12.000;
        	default: return -1.000;
        }
    }
}