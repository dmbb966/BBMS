package unit;

import java.util.Vector;

import bbms.GlobalFuncs;
import gui.GUI_NB;
import hex.Hex;

public enum OrganismTypeEnum {
	/** Used for testing purposes: a single sensor input going to a single output */
	SIMPLE_SINGLE,
	/** Six sensors each covering a 60-degree arc around the agent, goes to a single output */
	SIX_DIRECTIONAL;
	
	public double SenseFlowSingle(Unit u) {		
		Vector<Hex> visibleHexes = u.GetLOSToRange(GlobalFuncs.visibility);
		double sumDV = 0;
		
		GUI_NB.GCO("Beginning flow check.");
		
		for (int i = 0; i < visibleHexes.size(); i++){
			Hex finger = visibleHexes.elementAt(i);
			finger.DisplayInfo();
			sumDV += finger.deltaVapor;
		}
		
		GUI_NB.GCO("Check complete.  Visible hex size is: " + visibleHexes.size() + " with total DV: " + sumDV);
		
		return sumDV;
	}	
}

