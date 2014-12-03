package unit;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import hex.HexMap;
import hex.HexOff;

public class WaypointList {
	
	public LinkedList<HexOff> waypointList;
	
	public WaypointList() {
		waypointList = new LinkedList<HexOff>();
	}
	
	
	
	public boolean addWaypoint(int x, int y) {
		if (!HexMap.checkMapBounds(x, y)) return false;
		
		HexOff newWayPoint = new HexOff(x, y);
		waypointList.addLast(newWayPoint);		
		return true;
	}
	
	public boolean addFirstWaypoint(int x, int y) {
		if (!HexMap.checkMapBounds(x, y)) return false;
		
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
	
	public HexOff getFirstWaypoint() {
		if (waypointList.size() == 0) return new HexOff(-1, -1);
		HexOff currentWP = waypointList.getFirst();
		
		return currentWP;
	}
	
	public HexOff getLastWaypoint() {
		if (waypointList.size() == 0) return new HexOff(-1, -1);
		HexOff lastWP = waypointList.getLast();
		
		return lastWP;
	}
	
	public String displayWaypoints() {
		String output = "Total size: " + waypointList.size() + "\n";
				
		for (int i = 0; i < waypointList.size(); i++) {			
			HexOff finger = waypointList.get(i);
			output = output + "Waypoint " + i + ": (" + finger.getX() + ", " + finger.getY() + ")\n"; 
		}
		
		return output;
	}
}
