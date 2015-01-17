package clock;

import bbms.GlobalFuncs;
import unit.Unit;

public class Clock {
	
	public static int time = 0;

	/**
	 * Also updates LOS
	 */
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

}
