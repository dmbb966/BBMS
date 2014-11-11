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
	
	public static Vector<unit.Unit> unitList = new Vector<Unit>();
	
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
				gui.GMD.mapDisplayY--;
				gui.repaint();
				GUI_NB.GCO("Scroll Up, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};
		amap.put("scroll up", scrollUp);
		
		k = KeyStroke.getKeyStroke('a');
		imap.put(k, "scroll left");
		AbstractAction scrollLeft = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				gui.GMD.mapDisplayX--;
				gui.repaint();
				GUI_NB.GCO("Scroll Left, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);
			}
		};		
		amap.put("scroll left",  scrollLeft);
		
		k = KeyStroke.getKeyStroke('s');
		imap.put(k, "scroll down");
		AbstractAction scrollDown = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				gui.GMD.mapDisplayY++;
				gui.repaint();
				GUI_NB.GCO("Scroll Down, displaying at " + gui.GMD.mapDisplayX + ", " + gui.GMD.mapDisplayY);				
			}
		};
		amap.put("scroll down", scrollDown);
		
		k = KeyStroke.getKeyStroke('d');
		imap.put(k, "scroll right");
		AbstractAction scrollRight = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {				
				gui.GMD.mapDisplayX++;
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
					GUI_NB.GCO("Highlighted hexes (1, 1) toggled to ON");
					scenMap.getHex(1, 1).highlighted = true;
				}
				else {
					GUI_NB.GCO("Highlighted hexes toggled to OFF");
					scenMap.getHex(1, 1).highlighted = false;
				}
			}
		};
		amap.put("toggle highlight", toggleHighlighted);
				
		// gui.BasicInfoPane.requestFocus();
	}
	
	

}
