package bbms;

import gui.GUI_NB;

import java.io.BufferedReader;
import java.util.Vector;

import unit.Unit;

public class COA {
	public Vector<unit.Unit> unitList = new Vector<Unit>();
	
	public String name = "";
	
	public COA(String n) {
		name = n;
	}
	
	public void LoadCOA() {
		// First remove existing units
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			Unit finger = GlobalFuncs.unitList.elementAt(i);
			finger.RemovefromSide(finger.side);
			finger.location.HexUnit = null;
		}
		
		GlobalFuncs.selectedUnit = null;
		GlobalFuncs.selectedHex = null;
		
		GlobalFuncs.unitList = this.unitList;
		// Add to appropriate lists
		GlobalFuncs.enemyUnitList = new Vector<Unit>();
		GlobalFuncs.friendlyUnitList = new Vector<Unit>();
		
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			Unit finger = GlobalFuncs.unitList.elementAt(i);
			finger.AddtoSide(finger.side);
			finger.location.HexUnit = finger;
		}
		
		GUI_NB.GCO("COA " + name + " successfully loaded.");
	}
	
	public COA(BufferedReader buf) {
		
	}
	
	public String PrintCOA() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("COA >" + name + "<\n");
		
		for (int i = 0; i < this.unitList.size(); i++) {
			Unit finger = this.unitList.elementAt(i);
			
			buf.append(finger.SaveUnit() + "\n");
		}
		buf.append("\n");
		
		return buf.toString();
	}

}
