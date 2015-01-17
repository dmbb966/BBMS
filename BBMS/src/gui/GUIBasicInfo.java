package gui;

import java.awt.Graphics;

import javax.swing.JPanel;

import bbms.GlobalFuncs;

@SuppressWarnings("serial")
public class GUIBasicInfo extends JPanel {
	
	static String hexCoords;
	static String terrainType;
	static String elev;
	static String obsc;
	static String dens;
	static String obsH;
	
	public static int mouseX = 0;
	public static int mouseY = 0;
	
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
		}			
	}
	
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
				
		if (GlobalFuncs.mapInitialized) {
			// First line: time data
			g.drawString("Clock " + clock.Clock.time, 10, 30);
			
			// Second line: Hex info part 1
			g.drawString(hexCoords, 10, 45);
			g.drawString(terrainType, 85, 45);
			g.drawString(elev, 165, 45);
			
			// Third line: Hex info part 2
			g.drawString(obsc, 10, 60);
			g.drawString(dens, 85, 60);
			g.drawString(obsH, 165, 60);
			
			// Fourth line: Debug info - mouse cursor location
			g.drawString("Mouse at (" + mouseX + ", " + mouseY + ")", 10, 75);
		}				
	}
	
}
