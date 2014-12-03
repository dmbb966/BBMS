package bbms;

import unit.Unit;

public class Clock {
	
	public static int time = 0;

	/**
	 * Also updates LOS
	 */
	public static void moveAllUnits() {
		
		for (int i = 0; i < GlobalFuncs.friendlyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.friendlyUnitList.elementAt(i);
			finger.MoveToWaypoint();			
		}
		
		for (int i = 0; i < GlobalFuncs.enemyUnitList.size(); i++) {
			Unit finger = GlobalFuncs.enemyUnitList.elementAt(i);
			finger.MoveToWaypoint();
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
