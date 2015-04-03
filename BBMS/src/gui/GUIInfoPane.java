package gui;

import hex.Hex;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import clock.Clock;
import clock.ClockControl;
import bbms.GlobalFuncs;

@SuppressWarnings("serial")
public class GUIInfoPane extends JPanel {
		
	public static int mouseX = 0;
	public static int mouseY = 0;
	public static int uniMouseX = 0;
	public static int uniMouseY = 0;
	
	public static DetailedInfoEnum mode = DetailedInfoEnum.UNIT;
	
	private final int start = 25;
	private final int spacing = 15;
	
	public GUIInfoPane() {
		
	}	
	
	/**
	 * Changes the title border to the designated string
	 * @param s
	 */
	public static void renamePane(String s) {
		TitledBorder newB = BorderFactory.createTitledBorder(s);
		GlobalFuncs.gui.DetailedInfoPane.setBorder(newB);
	}
	
	/**
	 * Changes the display mode of the detailed info pane
	 * @param d
	 */
	public static void changePaneMode(DetailedInfoEnum d) {
		switch (d) {
		case UNIT:
			renamePane("Selected Unit Information");
			mode = DetailedInfoEnum.UNIT;
			break;
		case VAPOR:
			renamePane("Vapor Model Information");
			mode = DetailedInfoEnum.VAPOR;
			break;
		case DEBUG:
			renamePane("Debug Information");
			mode = DetailedInfoEnum.DEBUG;
			break;
		}
	}
	
	/**
	 * Updates information when in "Unit" mode
	 * @param g
	 */
	private void paintUnitMode(Graphics g) {
		if (GlobalFuncs.selectedUnit == null) return;
		
		int row = start;
		
		g.drawString("Callsign: " + GlobalFuncs.selectedUnit.callsign + "  (" + GlobalFuncs.selectedUnit.side + ")", 10, row);
		row += spacing;
		
		g.drawString("Unit Type: " + GlobalFuncs.selectedUnit.type, 10, row);
		row += spacing;
		
		g.drawString("Orientation: " + GlobalFuncs.selectedUnit.subHexDirection + " with progress: " + GlobalFuncs.selectedUnit.subHexLocation, 10, row);
		row += spacing;
		
		g.drawString("Hull azimuth: " + GlobalFuncs.selectedUnit.hullOrientation, 10, row);
		row += spacing;
		
		g.drawString("Turret azimuth: " + GlobalFuncs.selectedUnit.turretOrientation, 10, row);
		
		
	}
	
	/**
	 * Updates information when in "Vapor" mode
	 * @param g
	 */
	private void paintVaporMode(Graphics g) {
		int row = start;
		
		g.drawString("Flow rate: " + String.format("%.2f", GlobalFuncs.flowRate), 10, row);
		row += spacing;
		
		g.drawString("Source/Sink Max Delta: " + GlobalFuncs.maxDelta, 10, row);
		row += spacing;
		
		g.drawString("Ticks Stable: " + GlobalFuncs.ticksStable, 10, row);
		row += 2 * spacing;
		
		
		g.drawString("Total Vapor: " + String.format("%,d", GlobalFuncs.totalVapor), 10, row);
		row += spacing;
		
		g.drawString("Total Vapor Delta: " + GlobalFuncs.totalVaporDelta, 10, row);
		
		
		
		
	}
	
	/**
	 * Updates information when in "Debug" mode
	 * @param g
	 */
	private void paintDebugMode(Graphics g) {
		int row = start;
		
		// Display unit information
		if (GlobalFuncs.selectedUnit != null) {	
			g.drawString("Callsign: " + GlobalFuncs.selectedUnit.callsign + "  (" + GlobalFuncs.selectedUnit.side + ")", 10, row);
			row += spacing;
			
			g.drawString("Unit Type: " + GlobalFuncs.selectedUnit.type, 10, row);
			row += spacing;
			
			g.drawString("Orientation: " + GlobalFuncs.selectedUnit.subHexDirection + " with progress: " + GlobalFuncs.selectedUnit.subHexLocation, 10, row);
			row += spacing;
			
			g.drawString("Hull azimuth: " + GlobalFuncs.selectedUnit.hullOrientation, 10, row);
			row += spacing;
			
			g.drawString("Turret azimuth: " + GlobalFuncs.selectedUnit.turretOrientation, 10, row);
			row += spacing;
			
			if (GlobalFuncs.selectedUnit.org == null) {
				g.drawString("No neural net loaded.", 10,  row);				
			} else {
				g.drawString("Organism #" + GlobalFuncs.selectedUnit.org.genome.genome_id + " of species #" + 
						GlobalFuncs.selectedUnit.org.species.id, 10, row);
			}
			row += spacing;
			
			g.drawString("OrgType: " + GlobalFuncs.selectedUnit.orgType, 10, row);
			row += spacing;
			
			g.drawString("FitType: " + GlobalFuncs.selectedUnit.fitType, 10, row);
					
					
			
			row += spacing * 2;
		}
		

		// Display summary NN information
		if (GlobalFuncs.currentPop == null) {
			g.drawString("No JNEAT Population loaded.",  10, row);			
		}
		else {
			g.drawString("JNEAT pop has " + GlobalFuncs.currentPop.organisms.size() + " orgs in " + 
					GlobalFuncs.currentPop.species.size() + " species.", 10, row);			
		}
		
	}
		
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
	
		
		if (GlobalFuncs.mapInitialized){
			switch (mode) {
			case UNIT:
				paintUnitMode(g);
				break;
			case VAPOR:
				paintVaporMode(g);
				break;
			case DEBUG:
				paintDebugMode(g);
				break;
			}
		}				
	}
	
}
