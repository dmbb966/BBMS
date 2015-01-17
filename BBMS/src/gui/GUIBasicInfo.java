package gui;

import java.awt.Graphics;

import javax.swing.JPanel;

import clock.Clock;
import clock.ClockControl;
import bbms.GlobalFuncs;

@SuppressWarnings("serial")
public class GUIBasicInfo extends JPanel {
	
	static String hexCoords = "";
	static String terrainType = "";
	static String elev = "";
	static String obsc = "";
	static String dens = "";
	static String obsH = "";
	
	static String unitCallsign = "";
	static String unitType = ""; 	
	
	public static int mouseX = 0;
	public static int mouseY = 0;
	
	private final int start = 25;
	private final int spacing = 15;
	
	public GUIBasicInfo() {
		
	}
	
	/**
	 * Updates the basic hex info display with info for hex (x, y)
	 */
	public static void UpdateHexInfo(int x, int y) {
		
		hex.Hex h = GlobalFuncs.scenMap.getHex(x, y);
		if (h != null) {
			hexCoords = "Hex: (" + x + ", " + y + ")";						
			terrainType = h.tType.displayType();						
			elev = "Elev: " + h.elevation + "m";																							
			obsc = "Obsc: " + h.obscuration;			
			dens = "Density: " + h.density;						
			obsH = "ObsH: " + h.obsHeight + "m";	
			
			if (h.HexUnit != null) {
				unitCallsign = h.HexUnit.callsign;
				unitType = h.HexUnit.type;
			}
			else {
				unitCallsign = "";
				unitType = "";
			}
		}			
	}
		
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		
		int row = start;
		
				
		if (GlobalFuncs.mapInitialized) {
			// First line: time data
			//g.drawString("Clock " + clock.Clock.time, 10, start);
			g.drawString(Clock.DisplayTime(), 10, row);
			g.drawString(ClockControl.PrintTimeScale(), 85, row);
			
			row += spacing;
			
			// Second line: Hex info part 1
			g.drawString(hexCoords, 10, row);
			g.drawString(terrainType, 85, row);
			g.drawString(elev, 165, row);
			
			row += spacing;
			
			// Third line: Hex info part 2
			g.drawString(obsc, 10, row);
			g.drawString(dens, 85, row);
			g.drawString(obsH, 165, row);
			
			row += (spacing * 2);
			
			// Fourth line: Unit info
			g.drawString(unitCallsign, 10, row);
			g.drawString(unitType, 100, row);
			
			row += spacing;			
			// Fourth line: Debug info - mouse cursor location
			g.drawString("Mouse at (" + mouseX + ", " + mouseY + ")", 10, row);
		}				
	}
	
}
