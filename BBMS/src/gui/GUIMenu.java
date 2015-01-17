package gui;

import hex.Hex;

import java.awt.event.*;

import javax.swing.*;

import bbms.GlobalFuncs;
import clock.Clock;
import terrain.TerrainEnum;
import unit.WaypointList;

@SuppressWarnings("serial")
public class GUIMenu extends JMenuBar{
	
	public static class NewMap implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Initializing a 30x30 map");
			GlobalFuncs.initializeMap(30, 30);	
		}
	}
	
	public static class SaveGame implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (GlobalFuncs.saveState()) GUI_NB.GCO("Save successful.");
			else GUI_NB.GCO("Save failed.");		
		}
	}
	
	public static class LoadGame implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (GlobalFuncs.loadState()) GUI_NB.GCO("Load successful.");
			else GUI_NB.GCO("Load failed.");	
		}
	}
	
	public static class ExitGame implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("File - Exit");
			GUI_NB.GCO("File - Exit");
			System.exit(0);	
		}
	}	
	
	/** Sets the mode so that clicking on the map will now select a unit or highlight the hex
	 */
	public static class ModeSelectUnit implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now select units.");
			GlobalFuncs.placeUnit = 0;
		}
	}
	
	/** Sets the mode so that clicking on an empty hex will add a friendly M1A2 tank
	 */
	public static class ModeAddM1A2 implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now place a M1A2");
			GlobalFuncs.placeUnit = 1;
		}
	}
	
	/** Sets the mode so that clicking on an empty hex will add an enemy T72 tank
	 */
	public static class ModeAddT72 implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now place a T-72");
			GlobalFuncs.placeUnit = 2;
		}
	}
	
	/** Outputs all spotting this turn to the GUI console
	 */
	public static class DisplaySpotsThisTurn implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			spotting.SpotRecords x = GlobalFuncs.allSpots.getReportsTime(Clock.time);
			GUI_NB.GCO(x.DisplayRecords());
		}
	}
	
	/** Outputs all spotting data to the GUI console
	 */
	public static class DisplayAllSpots implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			spotting.SpotRecords x = GlobalFuncs.allSpots.getReportsTime(0, Clock.time);
			GUI_NB.GCO(x.DisplayRecords());
		}
	}
	
	/** Outputs all spotting data for the selected unit to the GUI console
	 */
	public static class DisplayAllSpotsForUnit implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (GlobalFuncs.selectedUnit != null) {
				spotting.SpotRecords x = GlobalFuncs.allSpots.getReportsTarget(GlobalFuncs.selectedUnit);
				GUI_NB.GCO(x.DisplayRecords());
			}
			else {
				GUI_NB.GCO("ERROR: No unit selected!");
			}
			
		}
	}

	
	/** Displays version info
	 */
	public static class HelpAbout implements ActionListener {
		public void actionPerformed(ActionEvent event) {				
			System.out.println("Bare Bones Military Simulator Experimental Version");
			GUI_NB.GCO("Bare Bones Military Simulator Experimental Version");
		}
	}
	

	
	public void HelpMenu() {		
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		this.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Keyboard Shortcuts", KeyEvent.VK_K);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		menuItem.setAction(new GUIKeyboard.HelpKeyboardShortcuts());
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("About", KeyEvent.VK_A);		
		menuItem.addActionListener(new HelpAbout());
		menu.add(menuItem);					
	}

	
	public GUIMenu() {
		FileMenu();
		SetupMenu();
		ActionsMenu();
		HelpMenu();		

	}

	
	public void FileMenu() {		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		this.add(menu);
		
		JMenuItem menuItem = new JMenuItem("New", KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new NewMap());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new SaveGame());
		menu.add(menuItem);	

		menuItem = new JMenuItem("Load", KeyEvent.VK_L);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new LoadGame());
		menu.add(menuItem);	
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ExitGame());
		menu.add(menuItem);
	}
	
	
	public void SetupMenu() {
		JMenu menu = new JMenu("Setup");
		menu.setMnemonic(KeyEvent.VK_S);
		this.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Mode: Select Unit");		
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ModeSelectUnit());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Place M1A2");
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ModeAddM1A2());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Place T-72");		
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ModeAddT72());
		menu.add(menuItem);
	}
	
	
	public void ActionsMenu() {
		JMenu menu = new JMenu("Actions");
		menu.setMnemonic(KeyEvent.VK_A);			
		this.add(menu);						
		
		JMenuItem menuItem = new JMenuItem("Display spots for this turn");		
		menuItem.addActionListener(new DisplaySpotsThisTurn());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Display spots for all time");		
		menuItem.addActionListener(new DisplayAllSpots());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Display spots for selected unit");		
		menuItem.addActionListener(new DisplayAllSpotsForUnit());
		menu.add(menuItem);
		
	}
}
