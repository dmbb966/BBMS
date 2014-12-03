package spotting;

import java.util.Vector;

import bbms.GUI_NB;

public class SpotRecords {
	public Vector<SpotReport> records;
	
	public SpotRecords() {
		records = new Vector<SpotReport>();
	}
	
	public SpotReport getReport(int i) {
		if (i < 0 || i > records.size()) return null;
		return records.elementAt(i);		
	}
	
	public void addReport(SpotReport s) {
		records.addElement(s);
	}
	
	/**
	 * Returns all spot reports for a given time 
	 */
	public SpotRecords getReportsTime(int t) {
		return getReportsTime(t, t);
	}
	
	/**
	 * Returns all spot reports for a given time interval, [t1, t2]
	 */
	public SpotRecords getReportsTime(int t1, int t2) {
		SpotRecords result = new SpotRecords();
				
		if (t2 < t1) {
			int temp = t1;
			t1 = t2;
			t2 = temp;
		}
		
		if (t1 < 0) return result;
		
		for (int i = 0; i < records.size(); i++) {
			SpotReport finger = records.elementAt(i);
			if (finger.timeSpotted >= t1 && finger.timeSpotted <= t2) {
				result.addReport(finger);
				// GUI_NB.GCO("Adding record " + i + " for time " + finger.timeSpotted);
				// GUI_NB.GCO("Record is: " + finger.displaySPOTREP());
			}
		}
		
		return result;		
	}
	
	/**
	 * Displays all records, each record on a separate line 
	 */
	public String DisplayRecords() {
		String result = "";
		for (int i = 0; i < records.size(); i++) {
			result += records.elementAt(i).displaySPOTREP() + "\n";
		}
		
		return result;
	}

}
