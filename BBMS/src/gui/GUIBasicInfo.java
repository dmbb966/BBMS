package gui;

import java.awt.Graphics;

import javax.swing.JPanel;

import bbms.GlobalFuncs;

@SuppressWarnings("serial")
public class GUIBasicInfo extends JPanel {
	
	int x = 0;
	int y = 0;
	
	public GUIBasicInfo() {
		
	}
	
	/**
	 * Updates the basic hex info display with info for hex (x, y)
	 */
	public void UpdateHexInfo(int x, int y) {
		 
		
		hex.Hex h = GlobalFuncs.scenMap.getHex(x, y);
		if (h != null) {
			String hexCoords = "Hex: (" + x + ", " + y + ")";
			hexCoords = GlobalFuncs.whiteFill(hexCoords,  20);
			
			String terrainType = h.tType.displayType();
			terrainType = GlobalFuncs.whiteFill(terrainType, 20);
			
			String elev = "Elev: " + h.elevation + "m";
			elev = GlobalFuncs.whiteFill(elev, 15);
			
			
						
			// GlobalFuncs.gui.BI_UpperDisp.setText(hexCoords + terrainType + elev);
			
			
			String obsc = "Obsc: " + h.obscuration;
			obsc = GlobalFuncs.whiteFill(obsc, 20);
			
			String dens = "Density: " + h.density;
			dens = GlobalFuncs.whiteFill(dens, 20);
			
			String obsH = "ObsH: " + h.obsHeight + "m";
			obsH = GlobalFuncs.whiteFill(obsH, 15);
			
			// GlobalFuncs.gui.BI_LowerDisp.setText(obsc + dens + obsH);
			
			// GlobalFuncs.gui.BI_Hex.setText(GlobalFuncs.whiteFill(output, 15));
			
			// output = "Density: " + h.density;			
			// GlobalFuncs.gui.BI_HexDens.setText(GlobalFuncs.whiteFill(output, 12));
			
			// output = "Elev: " + h.elevation + "m";
			// GlobalFuncs.gui.BI_HexElev.setText(GlobalFuncs.whiteFill(output, 10));
			
			// GlobalFuncs.gui.BI_HexObsc.setText("Obsc: " + h.obscuration);
			
			//output = "ObsH: " + h.obsHeight + "m";
			// GlobalFuncs.gui.BI_HexOHeight.setText(GlobalFuncs.whiteFill(output, 10));
			
			
			// GlobalFuncs.gui.BI_HexType.setText(h.tType.displayType());						
		}			
	}
	
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		
		GlobalFuncs.gui.GCO("Clock update: " + clock.Clock.time + " at (" + x + ", " + y + ")");
		g.drawString("Clock " + clock.Clock.time, 10, 30);		
		g.drawString("Next", 10, 45);
	}
	
}
