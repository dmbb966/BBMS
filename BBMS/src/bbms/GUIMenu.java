package bbms;

import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class GUIMenu extends JMenuBar{
	
	public GUIMenu() {
		// File Menu		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);			
		add(menu);
		
		JMenuItem menuItem = new JMenuItem("New", KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				GUI_NB.GCO("Initializing a 30x30 map");
				GlobalFuncs.initializeMap(30, 30);
				GUIKeyboard.initializeKeyCommands();
			}
		});
		
		menu.add(menuItem);	
		
		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (GlobalFuncs.saveState()) GUI_NB.GCO("Save successful.");
				else GUI_NB.GCO("Save failed.");				
			}
		});
		menu.add(menuItem);	
		
		menuItem = new JMenuItem("Load", KeyEvent.VK_L);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (GlobalFuncs.loadState()) GUI_NB.GCO("Load successful.");
				else GUI_NB.GCO("Load failed.");
				
			}
		});
		menu.add(menuItem);	
		
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("File - Exit");
				GUI_NB.GCO("File - Exit");
				System.exit(0);
			}
		});
		menu.add(menuItem);
		
		
		// Test Menu
		menu = new JMenu("Setup");
		menu.setMnemonic(KeyEvent.VK_S);			
		add(menu);
		
		menuItem = new JMenuItem("Mode: Place M1A2", KeyEvent.VK_2);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				GUI_NB.GCO("Clicking will now place a M1A2");
				GlobalFuncs.placeUnit = 1;
			}
		});
		menu.add(menuItem);
		 
		menuItem = new JMenuItem("Mode: Place T-72", KeyEvent.VK_3);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				GUI_NB.GCO("Clicking will now place a T-72.");
				GlobalFuncs.placeUnit = 2;
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Select Unit", KeyEvent.VK_4);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				GUI_NB.GCO("Clicking will now select units.");
				GlobalFuncs.placeUnit = 0;
			}
		});
		menu.add(menuItem);
		
		
		// Actions Menu
		menu = new JMenu("Actions");
		menu.setMnemonic(KeyEvent.VK_A);			
		add(menu);		
		
		menuItem = new JMenuItem("Move all units and update LOS", KeyEvent.VK_1);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {								
				Clock.moveAllUnits();
				Clock.updateLOSFriendly();
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Display spots for this turn", KeyEvent.VK_2);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {								
				spotting.SpotRecords x = GlobalFuncs.allSpots.getReportsTime(Clock.time);
				GUI_NB.GCO(x.DisplayRecords());
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Display spots for all time", KeyEvent.VK_3);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {								
				spotting.SpotRecords x = GlobalFuncs.allSpots.getReportsTime(0, Clock.time);
				GUI_NB.GCO(x.DisplayRecords());
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Display spots for selected unit", KeyEvent.VK_4);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {		
				if (GlobalFuncs.selectedUnit != null) {
					spotting.SpotRecords x = GlobalFuncs.allSpots.getReportsTarget(GlobalFuncs.selectedUnit);
					GUI_NB.GCO(x.DisplayRecords());
				}
				
			}
		});
		menu.add(menuItem);

		
		// Help Menu
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);			
		add(menu);
		
		menuItem = new JMenuItem("Keyboard Shortcuts", KeyEvent.VK_K);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {				
				GUI_NB.GCO("W/A/S/D - Scroll the map");
				GUI_NB.GCO("V - Toggle shaded hex visibility");				
				GUI_NB.GCO("NUMPAD arrows - shift rotation target");
				GUI_NB.GCO("[] - shift rotation");
				GUI_NB.GCO("T - switches between rotating turret and hull");
				GUI_NB.GCO("F - orients the turret to target");
				GUI_NB.GCO("L - finds LOS to the selected hex (CAPS = 360 view)");
				GUI_NB.GCO("C - clears any shaded hexes");
				GUI_NB.GCO("E - Display LOS to all enemies of selected unit");
				GUI_NB.GCO("P - Displays waypoint list for the current unit");
				GUI_NB.GCO("; - Adds a waypoint for the currently selected unit");
				GUI_NB.GCO(": - Removes the next waypoint for the currently selected unit");
				GUI_NB.GCO("X - Test key, varies from build to build");
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("About", KeyEvent.VK_A);		
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Bare Bones Military Simulator Experimental Version");
				GUI_NB.GCO("Bare Bones Military Simulator Experimental Version");
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Reinitialize keyboard", KeyEvent.VK_K);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				GUIKeyboard.initializeKeyCommands();
			}
		});
		menu.add(menuItem);
	}
}
