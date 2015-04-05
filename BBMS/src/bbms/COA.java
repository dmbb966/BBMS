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
	
	public void SaveCOA() {
		this.unitList = (Vector<Unit>) GlobalFuncs.unitList.clone();
	}
	
	public void LoadCOA() {
		// First remove existing units
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			Unit finger = GlobalFuncs.unitList.elementAt(i);
			finger.RemovefromSide(finger.side);
		}
		
		GlobalFuncs.unitList = this.unitList;
		// Add to appropriate lists
		GlobalFuncs.enemyUnitList = new Vector<Unit>();
		GlobalFuncs.friendlyUnitList = new Vector<Unit>();
		
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			Unit finger = GlobalFuncs.unitList.elementAt(i);
			finger.AddtoSide(finger.side);
		}
		
		GUI_NB.GCO("COA " + name + " successfully loaded.");
	}
	
	public COA(BufferedReader buf) {
		
	}
	
	public String SaveToFile() {
		StringBuffer buf = new StringBuffer("");
		
		
		
		return buf.toString();
	}
	
	public String PrintCOA() {
		StringBuffer buf = new StringBuffer("");
		
		buf.append("COA >" + name + "<\n\n");
		
		for (int i = 0; i < this.unitList.size(); i++) {
			Unit finger = this.unitList.elementAt(i);
			
			buf.append(finger.SaveUnit());
		}
		
		return buf.toString();
	}

}
