package clock;

import gui.GUI_NB;

// ClockThread version 3.0 - automatically updates the clock IAW
// the calculated ClockCalc and ClockDisplay intervals.
public class ClockThread implements Runnable {
  
    private int pausedSleepCheck = 100;
    private int clockDelay = 1000;
	
    // Initialize clock date to the constructed value
    public ClockThread(){
        
    }
    
    public void run() {       
        
        GUI_NB.GCO("Running clock thread.");
        try {
        	do {
	            while (ClockControl.paused) Thread.sleep(pausedSleepCheck);
	            
	            // Do stuff here
	            Clock.ClockLoop();      
	            Clock.IncrementMs((int)(1000 * ClockControl.NumTimeScale()));
	                                                       
	            // Waits at the end of a clock cycle - in final version, should
	            // take the longest of either this, or the time necessary to update
	            // and display all locations.
	            Thread.sleep(clockDelay);    
	            
            } while (1 > 0); // Loops forever
        }         
        catch (Exception exc) {
            GUI_NB.GCO("Clock thread was interrupted.");            
            System.out.println (exc);
            exc.printStackTrace();
        }
    }    
}
