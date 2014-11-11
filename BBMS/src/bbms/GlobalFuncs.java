package bbms;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import unit.Unit;
import bbms.MersenneTwister;
import hex.*;

public class GlobalFuncs {
	
	public static GUI_NB gui;
	public static HexMap scenMap;
	public static boolean mapInitialized = false;
	public static int placeUnit = 0;
	public static boolean showShaded = false;
	public static boolean showHighlighted = false;
	
	public static double debugRotateHull = 0.0;
	public static double debugRotateTurret = 0.0;
	public static int debugRotateX = 0;
	public static int debugRotateY = 0;
	public static boolean RotateHull = true;
	
	public static Vector<unit.Unit> unitList = new Vector<Unit>();
	public static Hex highlightedHex = null; 
	public static Unit selectedUnit = null;
	public static Hex selectedHex = null;
	
	private static int unitCount = 0;

	/**
	 * Random number object, uses the Mersenne Twister algorithm, coded in Java by Sean Luke (http://cs.gmu.edu/~sean/research/)
	 */
	public static MersenneTwister randGen = new MersenneTwister();
	
	/**
	 * Uses the Mersenne Twister to generate a random number between a minimum and maximum range (inclusive)
	 * Only uses integers.
	 */
	public static int randRange(int min, int max) {
		int delta = max - min + 1;
		return min + randGen.nextInt(delta);
	}
	
	/**
	 * Uses the Mersenne TWister to generate a random number given a mean and variance.  
	 * Only uses integers.
	 */
	public static int randMean (int mean, int var) {
		int min = mean - var;
		return min + randGen.nextInt(var * 2);
	}
	
	public static void initializeMap (int x, int y) {
		scenMap = new HexMap(x, y);
		mapInitialized = true;
		unitCount = 0;
		selectedUnit = null;
		highlightedHex = null;
		unitList = new Vector<Unit>();
		GUI_NB.GCO("Generating main map.");		
	}
	
	/**
	 * If s is less than length, will return a new string with sufficient blank spaces concatanated on to the end
	 * to bring the string up to the desired length.  Returns the original string otherwise.
	 */
	public static String whiteFill (String s, int len) {
		if (s.length() >= len) return s;
		else {
			int whiteFill = len - s.length();
			for (int i = 0; i < whiteFill; i++) {
				s = s.concat(" ");
			}
			
			return s;
		}
	}
	
	public static int getUnitCount() {
		return unitCount;
	}
	
	public static int getNewUnitCount() {
		return unitCount += 1;
	}
	
	/**
	 * Initialize keyboard commands once the map loads
	 */
	public static void initializeKeyCommands() {
		
		GUI_NB.GCO("Keyboard commands initialized.");
		InputMap imap = gui.BasicInfoPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap amap = gui.BasicInfoPane.getActionMap();
		
		KeyStroke k = KeyStroke.getKeyStroke('w');
		imap.put(k,  "scroll up");
		AbstractAction scrollUp = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				gui.GMD.mapDisplayY -= 2;
				gui.repaint();
				GUI_NB.GCO("Scroll Up, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};
		amap.put("scroll up", scrollUp);
		
		k = KeyStroke.getKeyStroke('a');
		imap.put(k, "scroll left");
		AbstractAction scrollLeft = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				gui.GMD.mapDisplayX -= 2;
				gui.repaint();
				GUI_NB.GCO("Scroll Left, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};		
		amap.put("scroll left",  scrollLeft);
		
		k = KeyStroke.getKeyStroke('s');
		imap.put(k, "scroll down");
		AbstractAction scrollDown = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				gui.GMD.mapDisplayY += 2;
				gui.repaint();
				GUI_NB.GCO("Scroll Down, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);				
			}
		};
		amap.put("scroll down", scrollDown);
		
		k = KeyStroke.getKeyStroke('d');
		imap.put(k, "scroll right");
		AbstractAction scrollRight = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				gui.GMD.mapDisplayX += 2;
				gui.repaint();
				GUI_NB.GCO("Scroll Right, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};
		amap.put("scroll right", scrollRight);
		
		
		k = KeyStroke.getKeyStroke('v');
		imap.put(k, "toggle visibility");
		AbstractAction toggleVisibility = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				showShaded = !showShaded;
				gui.repaint();
				if (showShaded) GUI_NB.GCO("Shaded hexes toggled to ON");
				else GUI_NB.GCO("Shaded hexes toggled to OFF");
			}
		};
		amap.put("toggle visibility", toggleVisibility);
		
		k = KeyStroke.getKeyStroke('h');
		imap.put(k, "toggle highlight");
		AbstractAction toggleHighlighted = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				showHighlighted = !showHighlighted;
				
				gui.repaint();
				if (showHighlighted) {
					GUI_NB.GCO("Highlighted hex toggled to ON");
				}
				else {
					GUI_NB.GCO("Highlighted hex toggled to OFF");
				}
			}
		};
		amap.put("toggle highlight", toggleHighlighted);
		
		k = KeyStroke.getKeyStroke('[');
		imap.put(k, "decrease rotation");
		AbstractAction debugDecRotate = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (selectedUnit != null) {
					if (RotateHull) selectedUnit.hullOrientation = normalizeAngle(selectedUnit.hullOrientation - 10);
					else selectedUnit.turretOrientation = normalizeAngle(selectedUnit.turretOrientation - 10);
					rotateDebugDisplay();
				} else GUI_NB.GCO("No unit selected!");			
			}
		};
		amap.put("decrease rotation", debugDecRotate);
		
		k = KeyStroke.getKeyStroke(']');
		imap.put(k, "increase rotation");
		AbstractAction debugIncRotate = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (selectedUnit != null) {
					if (RotateHull) selectedUnit.hullOrientation = normalizeAngle(selectedUnit.hullOrientation + 10);
					else selectedUnit.turretOrientation = normalizeAngle(selectedUnit.turretOrientation + 10);
					rotateDebugDisplay();
				} else GUI_NB.GCO("No unit selected!");
			}
		};
		amap.put("increase rotation", debugIncRotate);
		
		k = KeyStroke.getKeyStroke('4');
		imap.put(k, "shift rotate left");
		AbstractAction debugLeftTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				debugRotateX -= 1;
				rotateDebugDisplay();
			}
		};
		amap.put("shift rotate left", debugLeftTarget);
		
		k = KeyStroke.getKeyStroke('6');
		imap.put(k, "shift rotate right");
		AbstractAction debugRightTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				debugRotateX += 1;
				rotateDebugDisplay();
			}
		};
		amap.put("shift rotate right", debugRightTarget);
		
		k = KeyStroke.getKeyStroke('8');
		imap.put(k, "shift rotate up");
		AbstractAction debugUpTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				debugRotateY -= 1;
				rotateDebugDisplay();
			}
		};
		amap.put("shift rotate up", debugUpTarget);
		
		k = KeyStroke.getKeyStroke('2');
		imap.put(k, "shift rotate down");
		AbstractAction debugDownTarget = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				debugRotateY += 1;
				rotateDebugDisplay();
			}
		};
		amap.put("shift rotate down", debugDownTarget);
		
		k = KeyStroke.getKeyStroke('t');
		imap.put(k, "toggle rotation");
		AbstractAction toggleRotation = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				RotateHull = !RotateHull;
				if (RotateHull) GUI_NB.GCO("Rotating hull");
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
						if (RotateHull) selectedUnit.OrientHullTo(selectedUnit.target.location.x, selectedUnit.target.location.y);
						else selectedUnit.OrientTurretTo(selectedUnit.target.location.x,  selectedUnit.target.location.y);
						gui.repaint();
						GUI_NB.GCO("Orienting on target");
					}
				}
			}
		};
		amap.put("aim unit", aimUnit);
				
		// gui.BasicInfoPane.requestFocus();
	}
	
	public static void rotateDebugDisplay() {
		// GUI_NB.GCO("Rotation debug: Angle " + String.format("%.1f", debugRotateHull) + " and " + String.format("%.1f", debugRotateTurret) + " with center at " + debugRotateX + ", " + debugRotateY);
		
		gui.repaint();
	}
	
	public static int normalizeAngle(int a) {
		if (a >= 360) return normalizeAngle(a -= 360);
		else if (a < 0) return normalizeAngle(a += 360);
		else return a;
	}
	
	public static double normalizeAngle(double a) {
		if (a >= 360) return normalizeAngle(a -= 360);
		else if (a < 0) return normalizeAngle(a += 360);
		else return a;
	}
	

}
