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
		int row = start;
		
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
