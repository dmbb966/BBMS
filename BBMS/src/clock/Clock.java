package clock;

import gui.GUI_NB;

import java.text.DecimalFormat;

import bbms.GlobalFuncs;
import unit.Unit;


//Exception if the hour number given is out of range (0-23, inclusive)
@SuppressWarnings("serial")
class HourBoundsException extends Exception {
 HourBoundsException(int hour) {
     System.out.println ("ERROR: You tried to set the hour to " + hour + ".\n" +
             "Please enter an hour between 0 and 23, inclusive.  Time not changed.");
 }
}

@SuppressWarnings("serial")
//Exception if the minute number given is out of range (0-59, inclusive)
class MinuteBoundsException extends Exception {
 MinuteBoundsException(int minute) {
     System.out.println ("ERROR: You tried to set the minute to " + minute + ".\n" +
             "Please enter a minute between 0 and 59, inclusive.  Time not changed.");
 }
}

//Exception if the second number given is out of range (0-59, inclusive)
@SuppressWarnings("serial")
class SecondBoundsException extends Exception {
 SecondBoundsException(int second) {
     System.out.println ("ERROR: You tried to set the second to " + second + ".\n" +
             "Please enter a second between 0 and 59, inclusive.  Time not changed.");
 }
}

//Exception if the millisecond number given is out of range (0-999, inclusive)
@SuppressWarnings("serial")
class MsBoundsException extends Exception {
 MsBoundsException (int ms) {
     System.out.println ("ERROR: You tried to set the millisecond to " + ms + ".\n" +
             "Please enter a ms between 0 and 999, inclusive.  Time not changed.");
 }
}

public class Clock {
	
    static private final DecimalFormat two_zero = new DecimalFormat("00");
	static private final DecimalFormat zero_three = new DecimalFormat(".000");
	
	public static int hour = 0;
	public static int minute = 0;
	public static int second = 0;
	public static int ms = 0;		
	
	public static int time = 0;	

	public static void moveAllUnits() {
		
		// Move units first
		
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			Unit finger = GlobalFuncs.unitList.elementAt(i);
			finger.MoveToWaypoint();
		}
				
		// Now update turret orientations appropriately
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			Unit finger = GlobalFuncs.unitList.elementAt(i);
			if (finger.target != null) finger.OrientTurretToTarget();
		}
		
		time += 1;
		
		GlobalFuncs.gui.repaint();
	}

	public static void updateLOSFriendly() {
		GlobalFuncs.scenMap.unshadeAll();
		for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
			finger.FindLOSToEnemies();
		}		
		
		
		GlobalFuncs.gui.repaint();		
	}
	
	public static void ClockLoop() {
		// GUI_NB.GCO("Clock time is now " + DisplayTimeFull() + " with time rate " + ClockControl.PrintTimeScale());
		GlobalFuncs.gui.BasicInfoPane.repaint();
		
	}
	
	
	/** 
	 * IncrementMs increments the clock by a given number of milliseconds.
	 * @param msinc
	 */
	public static void IncrementMs(int msinc) {
        ms += msinc;
	    ClockSimplify();
	}
	
	/**
	 *  IncrementSec increments the clock by a given number of seconds.
	 * @param secinc
	 */
	public static void IncrementSec(int secinc) {  
        second += secinc;
        ClockSimplify();	    	    
	}
	
	/**
	 *  ClockSimplify checks to see if any of the clock variables have\
	 *  exceeded their maximum bound.  If so, it will automatically increment
	 *  the next most significant variable and so on.
	 */
	public static void ClockSimplify() {
	    // Checks milliseconds
	    while (ms >= 1000) {
	        second ++;
	        ms -= 1000;
	    }
	    
	    // Checks seconds
	    while (second >= 60) {
	        minute ++;
	        second -= 60;
	    }
	    
	    // Checks minutes
	    while (minute >= 60) {
	        hour ++;
	        minute -= 60;
	    }
	    
	    // Checks hours - does not keep track of actual date so just resets hours to 0.
	    while (hour >= 24) {
	        hour -= 24;
	    }	    	    
	}
	
	/**
	 * SetTime sets the current time (Hour, minute, second) - assumes milliseconds are 0.
	 * Use the four-argument version if you need to set the current millisecond time.
	 * @param newhour
	 * @param newminute
	 * @param newsecond
	 */
	//
	public static void SetTime(int newhour, int newminute, int newsecond) {
	    SetTime(newhour, newminute, newsecond, 0);
	}
	
	/** 
	 * SetTime sets the current time (Hour, minute, second, millisecond)
	 * @param newhour
	 * @param newminute
	 * @param newsecond
	 * @param newms
	 */
	public static void SetTime(int newhour, int newminute, int newsecond, int newms) {
	    try {
	        if (newhour < 0 | newhour >= 24) throw new HourBoundsException(newhour);
	        hour = newhour;
	        
	        if (newminute < 0 | newminute >= 60) throw new MinuteBoundsException(newminute);
	        minute = newminute;
	        
	        if (newsecond < 0 | newsecond >= 60) throw new SecondBoundsException(newsecond);
	        second = newsecond;
	        
	        if (newms < 0 | newms >= 1000) throw new MsBoundsException(newms);
	        ms = newms;
	    }
	    catch (HourBoundsException exception) {}
	    catch (MinuteBoundsException exception) {}
	    catch (SecondBoundsException exception) {}
	    catch (MsBoundsException exception) {}
	}
	
	/**
	 *  DisplayTimeFull returns a string with the current time in the following format:
	 *  "23:30:00.000"
	 * @return
	 */ 
	public static String DisplayTimeFull() {
	    // zero_three decimal format includes the decimal place automatically
	    return (two_zero.format(hour) + ":" + two_zero.format(minute) + ":" +
	            two_zero.format(second) + zero_three.format(ms / 1000.0));
	}
	
	/**
	 * DisplayTimeNorm returns a string with the current time in the following
	 * format: "23:30:00.0"
	 * @return
	 */
	public static String DisplayTimeNorm() {
	    return (two_zero.format(hour) + ":" + two_zero.format(minute) + ":" +
	            two_zero.format(second) + "." + (ms / 100));
	}
	
	/** 
	 * DisplayTime returns a string with the current time,
	 * but does not include information about miliseconds
	 * Format is as follows:
	 * "23:30:00"
	 * @return
	 */  
	public static String DisplayTime() {
	    return (two_zero.format(hour) + ":" + two_zero.format(minute) + ":" +
	            two_zero.format(second));
	}
	
    // doSilentCycle does a silent clock cycle - i.e. updates the locations
    // of all the units for a time delta, but does not display it to the screen.
    public static void doSilentCycle (int delta) {
        IncrementMs(delta);
     
    }
    
    
    // doFullCycle not only updates the locations in memory, but also their
    // locations on the screen.
    public static void doFullCycle (int delta) {
        doSilentCycle(delta);     
    }
}