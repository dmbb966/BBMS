package unit;

import java.util.ArrayDeque;
import java.util.Deque;

import hex.HexOff;
import bbms.GlobalFuncs;

public class WaypointList {
	
	public Deque<HexOff> waypointList;
	
	public WaypointList() {
		waypointList = new ArrayDeque<HexOff>();
	}
	
	
	
	public boolean addWaypoint(int x, int y) {
		if (!GlobalFuncs.checkMapBounds(x, y)) return false;
		
		HexOff newWayPoint = new HexOff(x, y);
		waypointList.addLast(newWayPoint);		
		return true;
	}
	
	public boolean addFirstWaypoint(int x, int y) {
		if (!GlobalFuncs.checkMapBounds(x, y)) return false;
		
		HexOff newWayPoint = new HexOff(x, y);
		waypointList.addFirst(newWayPoint);		
		return true;
	}
	
	public boolean removeLastWaypoint() {
		if (waypointList.size() == 0) return false;
		
		waypointList.removeLast();
		return true;
	}
	
	public boolean removeFirstWaypoint() {
		if (waypointList.size() == 0) return false;
		
		waypointList.removeFirst();
		return true;
	}
	
	public String displayWaypoints() {
		String output = "Total size: " + waypointList.size() + "\n";
		Deque<HexOff> clone = waypointList;
		output = output + "Total size: " + clone.size() + "\n";
		
		for (int i = 0; i < waypointList.size(); i++) {
			output = output + "Total size: " + clone.size() + "\n";
			HexOff finger = clone.removeFirst();
			output = output + "Waypoint " + i + ": (" + finger.getX() + ", " + finger.getY() + ")\n"; 
		}
		
		return output;
	}
}
