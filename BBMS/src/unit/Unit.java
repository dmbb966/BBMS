package unit;

import gui.GUI_NB;
import hex.Hex;
import hex.HexAx;
import hex.HexMap;
import hex.HexOff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Formatter;
import java.util.Locale;
import java.util.Vector;

import javax.imageio.ImageIO;

import clock.Clock;
import clock.ClockControl;
import clock.ClockThread;
import terrain.TerrainEnum;
import bbms.GlobalFuncs;

public class Unit {

	public hex.Hex location;
	public hex.HexOff HullOffset = new hex.HexOff(0, 0);
	public hex.HexOff TurretOffset = new hex.HexOff(0, 0);
	public hex.HexOff TurretRing = new hex.HexOff(0, 0);
	
	public double hullOrientation;
	public double turretOrientation;	// Relative to the hull
	
	public int movePoints = 0;
	public int moveRate = 0;	// Scale of 0-100 with 100 = 100% movement speed
	public MoveClass moveMode = MoveClass.FOOT;
	
	public int subHexDirection = 1;		// Axial direction the unit is traveling in, range is 1-6
	public int subHexLocation = 0;		// 0 is the center of the hex.  HexMap defines the maximum size.
	
	public boolean spotted = false;		// Has the unit been seen by the enemy?
		
	public WaypointList waypointList = new WaypointList();
	
	public Unit target;
	// public boolean trackTarget;
	
	// Used for determining hull and turret graphic files
	public String type;
	public String callsign;
	
	// Unique number
	public int unitID;
	
	// What side its on
	public SideEnum side;
	
	public Unit (hex.Hex locn, SideEnum s, String givenType, String givenCallsign, double hullOrient, double turretOrient, WaypointList wpL, boolean spot) {
		location = locn;
		side = s;
		type = givenType;
		target = null;
		// trackTarget = true;
		
		spotted = spot;
		
		hullOrientation = hullOrient;
		turretOrientation = turretOrient;
		if (wpL != null) waypointList = wpL;
		
		switch (s) {
			case ENEMY:
				GlobalFuncs.enemyUnitList.addElement(this);				
				break;
			case FRIENDLY:
				GlobalFuncs.friendlyUnitList.addElement(this);
				spotted = true;
				break;								
		}
		
		
		
		if (type.matches("M1A2")) {			
			HullOffset = new hex.HexOff(27, 12);
			TurretRing = new hex.HexOff(30 - HullOffset.getX(), 12 - HullOffset.getY());
			TurretOffset = new hex.HexOff(25, 11);
			movePoints = 22;
			moveRate = 100;
			moveMode = MoveClass.TRACK;
		} else if (type.matches("T-72")) {
			HullOffset = new hex.HexOff(25, 12);
			TurretRing = new hex.HexOff(28 - HullOffset.getX(), 11 - HullOffset.getY());
			TurretOffset = new hex.HexOff(13, 10);
			movePoints = 20;
			moveRate = 100;
			moveMode = MoveClass.TRACK;			
		}
		
		callsign = givenCallsign;		
		unitID = GlobalFuncs.getNewUnitCount();
			
		GlobalFuncs.unitList.addElement(this);
	}
		
	public String DispUnitInfo() {
		return "Unit " + unitID + " is type: " + type + ", callsign " + callsign + " at location " + 
					location.x + ", " + location.y + " on side " + side + "\n with turret and hull orientation " + 
				String.format("%.2f", turretOrientation) + " and " + String.format("%.2f", hullOrientation) + 
				"\n Hull offsets: " + HullOffset.getX() + " and " + HullOffset.getY();
	}
	
	public String DispSubHexMovement() {
		return subHexDirection + " / " + subHexLocation;
	}
	
	public void DrawUnit(int xi, int yi, Graphics g) {
		if (GlobalFuncs.showFOW && !spotted) return;
		
		// Loads the appropriate hex icon
		String hullPath = "src/unit/graphics/" + type + "_H.png";
		String turretPath = "src/unit/graphics/" + type + "_T.png";
		
		// GUI_NB.GCO("Hull: " + hullPath + " || Turret: " + turretPath);
		
		File hullFile = new File(hullPath);
		File turretFile = new File(turretPath);
		try {
			Image img = ImageIO.read(hullFile);
			Graphics2D g2d = (Graphics2D)g;
			AffineTransform backup = g2d.getTransform();
			AffineTransform trans = new AffineTransform();
			trans.rotate(Math.toRadians(hullOrientation - 90), xi, yi);		// Translates cardinal degrees to that of the coord system
			g2d.transform(trans);
			g2d.drawImage(img,  xi - HullOffset.getX(),  yi - HullOffset.getY(), null);
			g2d.setTransform(backup);
			
			
			// g.drawImage(img,  xi - HullOffset.getX(),  yi - HullOffset.getY(), null);
			
			// No translation needed since turret orientation is relative to the hull direction, 0 degrees = 12 oclock
			trans.rotate(Math.toRadians(turretOrientation), xi + TurretRing.getX(), yi + TurretRing.getY());
			g2d.transform(trans);
			img = ImageIO.read(turretFile);
			g2d.drawImage(img,  xi - TurretOffset.getX() + TurretRing.getX(),  
					yi - TurretOffset.getY() + TurretRing.getY(),  null);
			
			g2d.setTransform(backup);
			
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			GUI_NB.GCO(ie.getMessage());
		}				
	}
	
	public double getAzimuth(int x1, int y1, int x2, int y2) {
		HexOff currentHex = new HexOff(x1, y1);
		HexOff targetHex = new HexOff(x2, y2);
		return HexOff.AzimuthOff(currentHex,  targetHex);
	}
	
	/**
	 * Orients the hull to the specified hex; if there is no target, the turret remains in its current position.
	 * If the unit has a target, the turret will remain on the target
	 */
	public void OrientHullTo(int x, int y) {		
		hullOrientation = getAzimuth(location.x, location.y, x, y);
		if (target != null) {			
			turretOrientation = getAzimuth(location.x, location.y, target.location.x, target.location.y) - hullOrientation;
		}
	}
	
	/**
	 * Orients the turret to the specified hex and leaves the hull in place.  Takes the turret off the selected target,
	 * if applicable.
	 */
	public void OrientTurretTo(int x, int y) {
		turretOrientation = getAzimuth(location.x, location.y, x, y) - hullOrientation;
		// trackTarget = false;
	}
	
	/**
	 * Orients the turret to the specified target
	 */
	public void OrientTurretToTarget() {
		if (target != null) {
			OrientTurretTo(target.location.x, target.location.y);			
		}		
	}
	
	/**
	 * Displays the LOS from the current unit to a given x, y coordinate.
	 * If "clear" is set to true, clears all shaded hexes before drawing.
	 */
	public void DisplayLOSTo(int x, int y, boolean clear) {
		HexOff currentHex = new HexOff(location.x, location.y);
		HexOff targetHex = new HexOff(x, y);
		Vector<hex.Hex> hexList = HexOff.HexesBetween(currentHex, targetHex);
		if (clear) GlobalFuncs.scenMap.unshadeAll();
				
		int visibility = 0;
		// Does not count the hex the unit itself is in, hence, i starts at 1
		for (int i = 1; i < hexList.size(); i++) {
			Hex h = hexList.elementAt(i);			
			if (visibility <= 15) {
				GlobalFuncs.scenMap.shadeHex(h,  Color.GREEN);							
			} else if (visibility <= 30) {
				GlobalFuncs.scenMap.shadeHex(h, Color.YELLOW);
			} else GlobalFuncs.scenMap.shadeHex(h, Color.RED);						
			// GUI_NB.GCO("Setting hex " + h.x + ", " + h.y + " to shaded with visibility " + visibility);
			visibility += h.density;
		}
		GlobalFuncs.gui.repaint();
	}
	
	public boolean HasLOSTo(int x, int y) {
		HexOff currentHex = new HexOff(location.x, location.y);
		HexOff targetHex = new HexOff(x, y);
		Vector<hex.Hex> hexList = HexOff.HexesBetween(currentHex, targetHex);
		
		int visibility = 0;
		// Does not count the hex the unit itself is in, hence, i starts at 1
		for (int i = 1; i < hexList.size(); i++) {
			Hex h = hexList.elementAt(i);			
			if (visibility > 30) return false;			
			
			visibility += h.density;
		}
		
		return true;
	}
	
	public void DisplayLOSToTarget() {
		DisplayLOSTo(target.location.x, target.location.y, true);
	}
	
	public void DisplayLOSToEnemies() {
		Vector<unit.Unit> enemyList;
		Unit selected;
		
		if (this.side == SideEnum.ENEMY) enemyList = GlobalFuncs.friendlyUnitList;
		else enemyList = GlobalFuncs.enemyUnitList;
		
		if (enemyList.size() == 0) GUI_NB.GCO("ERROR!  There are no opposing units on the map!");
		else {
			for (int i = 0; i < enemyList.size(); i++) {
				selected = enemyList.elementAt(i);
				DisplayLOSTo(selected.location.x, selected.location.y, false);
				// GUI_NB.GCO(currentTarget.DispUnitInfo());
			}
		}
	}
	
	public void FindLOSToEnemies() {
		Vector<unit.Unit> enemyList;
		Unit selected;
		
		if (this.side == SideEnum.ENEMY) enemyList = GlobalFuncs.friendlyUnitList;
		else enemyList = GlobalFuncs.enemyUnitList;
		
		if (enemyList.size() != 0) {
			for (int i = 0; i < enemyList.size(); i++) {
				selected = enemyList.elementAt(i);
				if (HasLOSTo(selected.location.x, selected.location.y)) {
					spotting.SpotReport SPOTREP = new spotting.SpotReport(Clock.time, this, selected, selected.location.toHO());
					GlobalFuncs.allSpots.addReport(SPOTREP);
					selected.spotted = true;
					// GUI_NB.GCO("Added spot record: " + SPOTREP.displaySPOTREP());
					// GUI_NB.GCO("Checking record: " + GlobalFuncs.allSpots.getReport(GlobalFuncs.allSpots.records.size() - 1).displaySPOTREP());
					
					if (GlobalFuncs.showLOS) DisplayLOSTo(selected.location.x, selected.location.y, false);
					// GUI_NB.GCO(callsign + " has LOS to " + selected.callsign + " at time " + Clock.time);										
				}				
			}
		}				
	}
	
	public void DisplayLOSToRange(int range) {
		Vector<Hex> ring = HexOff.HexRing(location.x, location.y, range);
		
		for (int i = 0; i < ring.size(); i++) {
			Hex finger = ring.elementAt(i);
			DisplayLOSTo(finger.x, finger.y, false);
		}
	}
	
	public void DisplayWaypoints() {
		GlobalFuncs.scenMap.clearTextAll();
		for (int i = 0; i < waypointList.waypointList.size(); i++) {
			HexOff staticFinger = waypointList.waypointList.get(i);
			Hex finger = GlobalFuncs.scenMap.getHex(staticFinger.getX(), staticFinger.getY());
			
			GlobalFuncs.scenMap.setHexText(finger, "WP " + (i + 1), Color.WHITE);
		}
	}
	
	/**
	 * Moves the unit one hex in the direction specified.
	 * Direction is an integer with 1 = 30 degrees, 2 = 90 degrees, etc. in a clockwise pattern
	 * @param direction
	 */
	public void MoveUnit(int direction) {
		if (direction < 0 || direction > 5) return;
		
		HexOff newLoc = HexOff.NeighborOff(location.toHO(), direction);
		Hex destination = GlobalFuncs.scenMap.getHex(newLoc.getX(), newLoc.getY());
		
		// GUI_NB.GCO("Current loc: (" + location.x + ", " + location.y + ").  Dest loc: (" + newLoc.getX() + ", " + newLoc.getY() + ")");
		
		if (destination.tEnum != TerrainEnum.INVALID) {
			// Orients vehicle to destination hex
			OrientHullTo(destination.x, destination.y);
			
			// TODO There will be a bug if two units occupy the same space simultaneously, but it isn't debilitating.
			// Moves vehicle
			location.HexUnit = null;
			location = destination;
			location.HexUnit = this;
		}	
	}
	
	/**
	 * Moves the unit according to its move rate and the terrain in the direction specified.
	 * Direction is an integer with 1 = 30 degrees, 2 = 90 degrees, etc. in a clockwise pattern.
	 * Automatically resets unit to undetected state.
	 * @param direction
	 */	
	public void MoveUnitSubHex(int direction) {
		if (direction < 0 || direction > 5) return;

		
		if (subHexLocation == 0) {
			//GUI_NB.GCO("At center of hex.  Orienting to desired direction.");
			subHexDirection = direction;
		}
		
		// Checks to see if direction is compatible
		if (direction % 3 == subHexDirection % 3) {			
			if (direction == subHexDirection) {
				//GUI_NB.GCO("Directions are identical.  Moving.");
				subHexLocation += CalcMoveRate();				
				if (subHexLocation > HexMap.SUBHEX_SIZE) {
					int overshoot = subHexLocation - HexMap.SUBHEX_SIZE;
					MoveUnit(direction);
					subHexDirection += 3;
					if (subHexDirection > 6) subHexDirection -= 6;
					subHexLocation = HexMap.SUBHEX_SIZE - overshoot;
				}
			}
			else {
				//GUI_NB.GCO("Directions are opposite.  Moving to the center.");
				subHexLocation -= CalcMoveRate();
				if (subHexLocation < 0) {
					subHexLocation *= -1;
					subHexDirection += 3;
					if (subHexDirection > 6) subHexDirection -= 6;
				}
			}
		} else {
			//GUI_NB.GCO("Directions are incompatible.  Continuing move to center and will halt.");
			subHexLocation -= CalcMoveRate();
			if (subHexLocation < 0) subHexLocation = 0;
		}
		
	}
	
	/**
	 * Moves the unit one hex towards the waypoint
	 */
	public void MoveToWaypoint() {
		if (side != SideEnum.FRIENDLY) spotted = false;
		if (waypointList.getFirstWaypoint().getX() < 0) return;		// Invalid waypoint or no waypoint set
		
		HexAx nextWP = waypointList.getFirstWaypoint().ConvertToAx();		
		HexAx currentLoc = location.toHO().ConvertToAx();		
		
		int deltaX = nextWP.getX() - currentLoc.getX();
		int deltaY = nextWP.getY() - currentLoc.getY();
		
		// GUI_NB.GCO("Loc: " + currentLoc.getX() + ", " + currentLoc.getY() + 
		//		" Dest: " + nextWP.getX() + ", " + nextWP.getY() + "  || Delta: " + deltaX + ", " + deltaY);
		
		int moveDirection = -1;
		
		if (Math.abs(deltaX) == Math.abs(deltaY)) {
			// Move direction will be either 0 (NE) or 3 (SW)
			if (deltaX > deltaY) moveDirection = 0;
			if (deltaX < deltaY) moveDirection = 3;
			if (deltaX == deltaY) {
				if (deltaX > 0) moveDirection = 1;
				else moveDirection = 4;
			}
		}
		else if (Math.abs(deltaX) > Math.abs(deltaY)){
			if (deltaX > 0) moveDirection = 1;
			else moveDirection = 4;
		}
		else {
			if (deltaY > 0) moveDirection = 2;
			else moveDirection = 5;
		}
					
		// GUI_NB.GCO("Move dir: " + moveDirection);
		if (moveDirection < 0) return;
		
		// --------------------------------------------------------
		// MoveUnit(moveDirection);				
		MoveUnitSubHex(moveDirection);
		
		if (location.toHO().ConvertToAx().getX() == nextWP.getX() && location.toHO().ConvertToAx().getY() == nextWP.getY()) {
			// GUI_NB.GCO("Waypoint reached.");
			waypointList.removeFirstWaypoint();			
		}
	}
	
	public String SaveUnit() {				
		int spot = 0;
		if (spotted) spot = 1;
		String output = unitID + ", " +
						callsign + ", " +
						location.x + ", " + location.y + ", " +
						String.format("%.2f", hullOrientation) + ", " + 
						String.format("%.2f", turretOrientation) + ", " + 
						type + ", " + 
						side + ", " +
						spot + ", " + 
						waypointList.saveWaypoints();				
		
		return output;
	}	
	
	public String SaveTarget() {
		String tgtStr = "_";
		if (target != null) tgtStr = String.format("%d", target.unitID);
		
		String output = unitID + ", " + tgtStr;
		
		return output;
	}
	
	public int CalcMoveRate() {
		int moveCost = location.tType.getMoveCost(moveMode);
		return (HexMap.SUBHEX_SIZE * movePoints * moveRate * ClockControl.CLOCK_STEP) / (100 * 60 * moveCost * 1000);
	}
	
	 
}
