package bbms;

import gui.GUI_NB;

import java.io.BufferedReader;
import java.io.IOException;
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
			GlobalFuncs.scenMap.getHex(finger.location.toHO()).HexUnit = finger;
			//finger.location.HexUnit = finger;
		}
		
		GUI_NB.GCO("COA " + name + " successfully loaded.");
	}
	
	@SuppressWarnings("unchecked")
	public COA(COA parent, String name) {
		this.unitList = (Vector<Unit>) parent.unitList.clone();
		this.name = name;
	}
	
	public COA(BufferedReader buf, String readR) {
		// First line (string readL) contains COA name
		this.name = readR.substring(5, readR.length() - 1);
		
		try {
			String readL = buf.readLine();
			while (!readL.contentEquals("")) {
				GUI_NB.GCO("String: >" + readL + "<");
				if (readL.startsWith(">Last")) {return;}
				if (!readL.startsWith("#")) {
					Unit finger = new Unit(readL);
					this.unitList.addElement(finger);	
				}
				
				readL = buf.readLine();
			}	
		}  catch (IOException e) {
			e.printStackTrace();
		}
		
		GUI_NB.GCO("Loaded COA " + name);
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
