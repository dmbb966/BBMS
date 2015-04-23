package unit;

import java.util.Vector;

import bbms.GlobalFuncs;
import gui.GUI_NB;
import hex.Hex;

public enum OrganismTypeEnum {
	/** Used for testing purposes: a single sensor input going to a single output */
	SIMPLE_SINGLE,
	/** As Simple-Single, but has a separate sensor for the hex the unit is in. */
	SIMPLE_DUAL,
	/** Six sensors each covering a 60-degree arc around the agent, goes to a single output and a seventh for the hex it is in */
	SIX_DIRECTIONAL;
	
	/** Senses the vapor DV for all hexes that it can see.  Will NOT count DV in the "friendly" zone */
	public static double SenseFlowFOV(Hex origin) {		
		Vector<Hex> visibleHexes = Unit.GetLOSToRange(origin, GlobalFuncs.visibility);
		double sumDV = 0;
		
		// GUI_NB.GCO("Beginning flow check.");
		
		for (int i = 0; i < visibleHexes.size(); i++){
			Hex finger = visibleHexes.elementAt(i);
			// finger.DisplayInfo();
			if (!GlobalFuncs.scenMap.inFriendlyZone(finger)) sumDV += finger.deltaVapor;
		}
		
		// GUI_NB.GCO("Check complete.  Visible hex size is: " + visibleHexes.size() + " with total DV: " + sumDV);
		
		return sumDV;
	}	
	
	/** Returns the normalized delta vapor of the specified hex*/
	public static double SenseFlowLocation(Hex origin) {			
		return (double) origin.deltaVapor / GlobalFuncs.maxsingleDV;
	}
	
	public static double[] SenseFlow60(Hex origin) {
		double[] ret = {0, 0, 0, 0, 0, 0};
		
		Vector<Hex> visibleHexes = Unit.GetLOSToRange(origin,  GlobalFuncs.visibility);
		for (int i = 0; i < visibleHexes.size(); i++) {
			Hex finger = visibleHexes.elementAt(i);
			int direction = origin.DirectionTo(finger);
			
			ret[direction] += finger.deltaVapor;
		}
		
		for (int i = 0; i < 6; i++) {
			double temp = ret[i];
			ret[i] = ret[i] / GlobalFuncs.maxSpottedDV60;
			// GUI_NB.GCO("Original value: " + temp + "/tNormalized value: " + ret[i]);
		}
		
		return ret;
	}
	
	public static double NormalizedSenseFlowSingle(Hex origin) {
		return SenseFlowFOV(origin) / GlobalFuncs.maxSpottedDV;
	}
}

