package gui;

import hex.HexOff;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import bbms.DebugFuncs;
import bbms.GlobalFuncs;
import clock.Clock;

public class GUIKeyboard {

	/**
	 * Initialize keyboard commands once the map loads
	 */
	@SuppressWarnings("serial")
	public static void initializeKeyCommands() {
		
		GUI_NB.GCO("Keyboard commands initialized.");
		InputMap imap = GlobalFuncs.gui.MainDisplay.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap amap = GlobalFuncs.gui.MainDisplay.getActionMap();
		
		imap.clear();
		amap.clear();
		
		KeyStroke k = KeyStroke.getKeyStroke('w');
		imap.put(k,  "scroll up");
		AbstractAction scrollUp = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				GlobalFuncs.gui.GMD.mapDisplayY -= 2;
				GlobalFuncs.gui.repaint();
				// GUI_NB.GCO("Scroll Up, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};
		amap.put("scroll up", scrollUp);
		
		k = KeyStroke.getKeyStroke('a');
		imap.put(k, "scroll left");
		AbstractAction scrollLeft = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				GlobalFuncs.gui.GMD.mapDisplayX -= 2;
				GlobalFuncs.gui.repaint();
				// GUI_NB.GCO("Scroll Left, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};		
		amap.put("scroll left",  scrollLeft);
		
		k = KeyStroke.getKeyStroke('s');
		imap.put(k, "scroll down");
		AbstractAction scrollDown = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				GlobalFuncs.gui.GMD.mapDisplayY += 2;
				GlobalFuncs.gui.repaint();
				// GUI_NB.GCO("Scroll Down, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);				
			}
		};
		amap.put("scroll down", scrollDown);
		
		k = KeyStroke.getKeyStroke('d');
		imap.put(k, "scroll right");
		AbstractAction scrollRight = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				GlobalFuncs.gui.GMD.mapDisplayX += 2;
				GlobalFuncs.gui.repaint();
				// GUI_NB.GCO("Scroll Right, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};
		amap.put("scroll right", scrollRight);
		
		
		k = KeyStroke.getKeyStroke('v');
		imap.put(k, "toggle visibility");
		AbstractAction toggleVisibility = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				GlobalFuncs.showShaded = !GlobalFuncs.showShaded;
				GlobalFuncs.gui.repaint();
				if (GlobalFuncs.showShaded) GUI_NB.GCO("Shaded hexes toggled to ON");
				else GUI_NB.GCO("Shaded hexes toggled to OFF");
			}
		};
		amap.put("toggle visibility", toggleVisibility);	
		
		k = KeyStroke.getKeyStroke('[');
		imap.put(k, "decrease rotation");
		AbstractAction debugDecRotate = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (GlobalFuncs.selectedUnit != null) {
					if (GlobalFuncs.RotateHull) GlobalFuncs.selectedUnit.hullOrientation = GlobalFuncs.normalizeAngle(GlobalFuncs.selectedUnit.hullOrientation - 10);
					else GlobalFuncs.selectedUnit.turretOrientation = GlobalFuncs.normalizeAngle(GlobalFuncs.selectedUnit.turretOrientation - 10);
					DebugFuncs.rotateDebugDisplay();
				} else GUI_NB.GCO("No unit selected!");			
			}
		};
		amap.put("decrease rotation", debugDecRotate);
		
		k = KeyStroke.getKeyStroke(']');
		imap.put(k, "increase rotation");
		AbstractAction debugIncRotate = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (GlobalFuncs.selectedUnit != null) {
					if (GlobalFuncs.RotateHull) GlobalFuncs.selectedUnit.hullOrientation = GlobalFuncs.normalizeAngle(GlobalFuncs.selectedUnit.hullOrientation + 10);
					else GlobalFuncs.selectedUnit.turretOrientation = GlobalFuncs.normalizeAngle(GlobalFuncs.selectedUnit.turretOrientation + 10);
					DebugFuncs.rotateDebugDisplay();
				} else GUI_NB.GCO("No unit selected!");
			}
		};
		amap.put("increase rotation", debugIncRotate);
		
		k = KeyStroke.getKeyStroke('4');
		imap.put(k, "shift rotate left");
		AbstractAction debugLeftTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				DebugFuncs.debugRotateX -= 1;
				DebugFuncs.rotateDebugDisplay();
			}
		};
		amap.put("shift rotate left", debugLeftTarget);
		
		k = KeyStroke.getKeyStroke('6');
		imap.put(k, "shift rotate right");
		AbstractAction debugRightTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				DebugFuncs.debugRotateX += 1;
				DebugFuncs.rotateDebugDisplay();
			}
		};
		amap.put("shift rotate right", debugRightTarget);
		
		k = KeyStroke.getKeyStroke('8');
		imap.put(k, "shift rotate up");
		AbstractAction debugUpTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				DebugFuncs.debugRotateY -= 1;
				DebugFuncs.rotateDebugDisplay();
			}
		};
		amap.put("shift rotate up", debugUpTarget);
		
		k = KeyStroke.getKeyStroke('2');
		imap.put(k, "shift rotate down");
		AbstractAction debugDownTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				DebugFuncs.debugRotateY += 1;
				DebugFuncs.rotateDebugDisplay();
			}
		};
		amap.put("shift rotate down", debugDownTarget);
		
		k = KeyStroke.getKeyStroke('t');
		imap.put(k, "toggle rotation");
		AbstractAction toggleRotation = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				GlobalFuncs.RotateHull = !GlobalFuncs.RotateHull;
				if (GlobalFuncs.RotateHull) GUI_NB.GCO("Rotating hull");
				else GUI_NB.GCO("Rotating turret");
			}
		};
		amap.put("toggle rotation", toggleRotation);
		
		k = KeyStroke.getKeyStroke('f');
		imap.put(k, "aim unit");
		AbstractAction aimUnit = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				if (GlobalFuncs.selectedUnit != null) {
					if (GlobalFuncs.selectedUnit.target != null) {
						if (GlobalFuncs.RotateHull) GlobalFuncs.selectedUnit.OrientHullTo(GlobalFuncs.selectedUnit.target.location.x, GlobalFuncs.selectedUnit.target.location.y);
						else GlobalFuncs.selectedUnit.OrientTurretTo(GlobalFuncs.selectedUnit.target.location.x,  GlobalFuncs.selectedUnit.target.location.y);
						GlobalFuncs.gui.repaint();
						GUI_NB.GCO("Orienting on target");
					}
				}
			}
		};
		amap.put("aim unit", aimUnit);
		
		k = KeyStroke.getKeyStroke('l');
		imap.put(k, "find LOS");
		AbstractAction findLOS = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {	
				GUI_NB.GCO("Find LOS");
				if (GlobalFuncs.selectedUnit != null && GlobalFuncs.selectedUnit.location != GlobalFuncs.highlightedHex) {
					GlobalFuncs.selectedUnit.DisplayLOSTo(GlobalFuncs.highlightedHex.x, GlobalFuncs.highlightedHex.y, true);					
				}
			}
		};
		amap.put("find LOS", findLOS);
		
		k = KeyStroke.getKeyStroke('c');
		imap.put(k, "clear shading");
		AbstractAction clearShading = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {	
				GUI_NB.GCO("Clearing shaded and texted hexes.");
				GlobalFuncs.scenMap.unshadeAll();
				GlobalFuncs.scenMap.clearTextAll();
				
			}
		};
		amap.put("clear shading", clearShading);
		
		k = KeyStroke.getKeyStroke('e');
		imap.put(k, "display LOS to enemies");
		AbstractAction LOStoEnemies = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (GlobalFuncs.selectedUnit != null) {
					GUI_NB.GCO("Displaying LOS to enemies.");
					GlobalFuncs.selectedUnit.DisplayLOSToEnemies();
				}
				else {
					GUI_NB.GCO("ERROR: No unit selected!");
				}											
			}
		};
		amap.put("display LOS to enemies", LOStoEnemies);
		
	
		
		
		k = KeyStroke.getKeyStroke(';');
		imap.put(k, "add waypoint key");
		AbstractAction addWP = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (GlobalFuncs.selectedUnit != null && GlobalFuncs.highlightedHex != null) {	
					int x = GlobalFuncs.highlightedHex.x;
					int y = GlobalFuncs.highlightedHex.y;
					HexOff lastWP = GlobalFuncs.selectedUnit.waypointList.getLastWaypoint();
					
					if (x == GlobalFuncs.selectedUnit.location.x && y == GlobalFuncs.selectedUnit.location.y) {
						GUI_NB.GCO("Can't add waypoint since highlighted hex is the unit's current location.");
					}
					else if (x != lastWP.getX() || y != lastWP.getY()) 
					{
						GlobalFuncs.selectedUnit.waypointList.addWaypoint(GlobalFuncs.highlightedHex.x, GlobalFuncs.highlightedHex.y);
						GUI_NB.GCO("Added waypoint at (" + x + ", " + y + ")");
						GUI_NB.GCO(GlobalFuncs.selectedUnit.waypointList.displayWaypoints());						
					}
					else {
						GUI_NB.GCO("Can't add waypoint since highlighted hex is the unit's last waypoint.");
					}
				}
			}
		};
		amap.put("add waypoint key", addWP);
		
		k = KeyStroke.getKeyStroke(':');
		imap.put(k, "remove waypoint key");
		AbstractAction removeWP = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (GlobalFuncs.selectedUnit != null) {
					if (GlobalFuncs.selectedUnit.waypointList.waypointList.size() > 0) {
						GlobalFuncs.selectedUnit.waypointList.removeFirstWaypoint();
						GUI_NB.GCO("Removed the next waypoint of the unit.");
						GUI_NB.GCO(GlobalFuncs.selectedUnit.waypointList.displayWaypoints());
					}
					else {
						GUI_NB.GCO("Can't remove waypoint as this unit doesn't have any.");
					}
					
				}
			}
		};
		amap.put("remove waypoint key", removeWP);
		
		k = KeyStroke.getKeyStroke('p');
		imap.put(k, "view waypoints");
		AbstractAction viewWP = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (GlobalFuncs.selectedUnit != null) {
					if (GlobalFuncs.selectedUnit.waypointList.waypointList.size() > 0) {
						GlobalFuncs.selectedUnit.DisplayWaypoints();
						GUI_NB.GCO(GlobalFuncs.selectedUnit.waypointList.displayWaypoints());
					}
					else {
						GUI_NB.GCO("This unit has no waypoints.");
					}
					
				}
			}
		};
		amap.put("view waypoints", viewWP);
				
		k = KeyStroke.getKeyStroke('L');
		imap.put(k, "full LOS");
		AbstractAction fullLOS = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (GlobalFuncs.selectedUnit != null) {
					GlobalFuncs.selectedUnit.DisplayLOSToRange(GlobalFuncs.scenMap.getXDim());
					// HexOff.HexRing(highlightedHex.x, highlightedHex.y, 3);
				}
			}
		};
		amap.put("full LOS", fullLOS);
		
		
		k = KeyStroke.getKeyStroke('x');
		imap.put(k, "test key");
		AbstractAction testX = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {							
				Clock.moveAllUnits();
				Clock.updateLOSFriendly();
			}
		};
		amap.put("test key", testX);
	}

}
