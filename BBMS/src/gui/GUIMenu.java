package gui;

import hex.Hex;

import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;

import javax.swing.*;

import jneat.Population;
import bbms.GlobalFuncs;
import clock.Clock;
import terrain.TerrainEnum;
import unit.WaypointList;
import utilities.FIO;
import gui.DialogNewPop;

@SuppressWarnings("serial")
public class GUIMenu extends JMenuBar{
	
	public static class NewMap implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Initializing a 30x30 map");
			GlobalFuncs.initializeMap(30, 30);	
		}
	}
	
	public static class NewBlankMap implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Initializing a blank 30x30 map");
			GlobalFuncs.initializeMap(30, 30, true);	
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
	
	public static class SavePopulation implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (GlobalFuncs.currentPop == null) {
				GUI_NB.GCO("ERROR: There is no population to save!");
				return;
			}
			
			DialogFileName d = new DialogFileName(GlobalFuncs.gui, true, "Save Population");
			d.setVisible(true);
			
			String fullPath = "src/saves/" + GlobalFuncs.tempStr;
			File f = new File(fullPath);
			if (!f.exists()) FIO.newFile(fullPath);
			Path p = f.toPath();
			
			GUI_NB.GCO("Saving JNEAT population to: " + fullPath);
			GlobalFuncs.currentPop.SavePopulationToFile(p);
		}
	}
	
	public static class LoadPopulation implements ActionListener {
		public void actionPerformed(ActionEvent event) {			
			DialogFileName d = new DialogFileName(GlobalFuncs.gui, true, "Load Population");
			d.setVisible(true);
			
			String fullPath = "src/saves/" + GlobalFuncs.tempStr;
			
			File f = new File(fullPath);
			if (!f.exists()) {
				GUI_NB.GCO("ERROR: " + fullPath + " does not exist!");
			} else {
				GUI_NB.GCO("Loading JNEAT population from: " + fullPath);
				Path p = f.toPath();
				
				GlobalFuncs.currentPop = new Population(p);
			}			
		}
	}
	
	public static class LoadScenario implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			DialogFileName d = new DialogFileName(GlobalFuncs.gui, true, "Load Scenario");
			d.setVisible(true);
			
			String fullPath = "src/saves/" + GlobalFuncs.tempStr;
			
			File f = new File(fullPath);
			if (!f.exists()) {
				GUI_NB.GCO("ERROR: " + fullPath + " does not exist!");
			} else {
				GUI_NB.GCO("Loading Scenario from: " + fullPath);
				Path p = f.toPath();
				
				GUI_NB.GCO("Functinoality not implemented yet.");
			}
		}
	}
	
	public static class SaveScenario implements ActionListener {
		public void actionPerformed(ActionEvent event) {		
			DialogFileName d = new DialogFileName(GlobalFuncs.gui, true, "Save Scenario");
			d.setVisible(true);
			
			String fullPath = "src/saves/" + GlobalFuncs.tempStr;
			File f = new File(fullPath);
			if (!f.exists()) FIO.newFile(fullPath);
			Path p = f.toPath();
			
			GUI_NB.GCO("Saving scenario to: " + fullPath);			
			
			GUI_NB.GCO("Functinoality not implemented yet.");
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
	
	/** Sets the mode so that clicking will change the given terrain to CLEAR
	 */
	public static class ModePaintClear implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now set terrain to CLEAR");
			GlobalFuncs.placeUnit = 10;
		}
	}
	
	/** Sets the mode so that clicking will change the given terrain to TREES
	 */
	public static class ModePaintTrees implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now set terrain to TREES");
			GlobalFuncs.placeUnit = 11;
		}
	}
	
	/** Sets the mode so that clicking will change the given terrain to TALL GRASS
	 */
	public static class ModePaintTallGrass implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now set terrain to TALL GRASS");
			GlobalFuncs.placeUnit = 12;
		}
	}
	
	/** Sets the mode so that clicking will change the given hex to a vapor source
	 */
	public static class ModeSetVaporSource implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now set hex to VAPOR SOURCE");
			GlobalFuncs.placeUnit = 21;
		}
	}
	
	/** Sets the mode so that clicking will change the given hex to a vapor sink
	 */
	public static class ModeSetVaporSink implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now set hex to VAPOR SINK");
			GlobalFuncs.placeUnit = 22;
		}
	}
	
	/** Sets the mode so that clicking will remove a vapor source or sink
	 */
	public static class ModeSetVaporNorm implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Clicking will now REMOVE vapor source or sink");
			GlobalFuncs.placeUnit = 20;
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
	
	/** Sets the display mode to UNIT in the main display and info panes
	 */
	public static class DisplayModeUnit implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Display Mode set to UNIT");
			GlobalFuncs.showVapor = false;
			GlobalFuncs.showLOS = true;
			GlobalFuncs.showFOW = true;
			GUIInfoPane.changePaneMode(DetailedInfoEnum.UNIT);
		}
	}
	
	/** Sets the display mode to VAPOR in the main display and info panes
	 */
	public static class DisplayModeVapor implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Display Mode set to VAPOR");
			GlobalFuncs.showVapor = true;
			GlobalFuncs.showLOS = false;
			GlobalFuncs.showFOW = false;
			GUIInfoPane.changePaneMode(DetailedInfoEnum.VAPOR);
		}
	}
	
	/** Sets the display mode to DEBUG in the main display and info panes
	 */
	public static class DisplayModeDebug implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Display Mode set to DEBUG");
			GlobalFuncs.showVapor = true;
			GlobalFuncs.showLOS = false;
			GlobalFuncs.showFOW = false;
			GUIInfoPane.changePaneMode(DetailedInfoEnum.DEBUG);
		}
	}
	
	/** Predicts vapor equilibrium based on linear interpolation between source and sink
	 */
	public static class PredictVapor implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Predicting vapor equilibrium.");
			GlobalFuncs.scenMap.predictVaporMap();
		}
	}
	
	/** Resets vapor levels in all hexes to starting amounts (full density)
	 */
	public static class ResetVapor implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GlobalFuncs.scenMap.StandardVaporMap();
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
	
	/** Copies the contents of the GUI Console to the clipboard
	 */
	public static class CopyConsole implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GUIConsole.selectAll();
			GUI_NB.GUIConsole.copy();
			GUI_NB.GUIConsole.select(0, 0);
			GUI_NB.GCO("Copyied to clipboard.");
		}
	}
	
	/** Test the population dialog */
	public static class PopDialog implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			GUI_NB.GCO("Displaying dialog.");
			DialogNewPop dialog = new DialogNewPop(GlobalFuncs.gui, true);
			// dialog.setLocationRelativeTo(GlobalFuncs.gui);
			dialog.setVisible(true);
		}
	}
	

	


	
	public GUIMenu() {
		GenerateMenu();		
	}
	
	public void GenerateMenu() {
		FileMenu();
		
		if (GlobalFuncs.mapInitialized) {
			DisplayMenu();
			SetupMenu();
			ActionsMenu();
		}
		
		HelpMenu();
	}
	
	public void DisplayMenu() {
		JMenu menu = new JMenu("Display");
		menu.setMnemonic(KeyEvent.VK_D);
		this.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Unit Display Mode");
		menuItem.addActionListener(new DisplayModeUnit());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Vapor Display Mode");
		menuItem.addActionListener(new DisplayModeVapor());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Debug Display Mode");
		menuItem.addActionListener(new DisplayModeDebug());
		menu.add(menuItem);
		
	}

	
	public void FileMenu() {		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		this.add(menu);
		
		JMenuItem menuItem = new JMenuItem("New Rand Map", KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new NewMap());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("New Blank Map", KeyEvent.VK_B);
		menuItem.addActionListener(new NewBlankMap());
		menu.add(menuItem);
		
		if (bbms.GlobalFuncs.mapInitialized) {
			menuItem = new JMenuItem("Save", KeyEvent.VK_S);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
			menuItem.addActionListener(new SaveGame());
			menu.add(menuItem);	
		}

		menuItem = new JMenuItem("Load", KeyEvent.VK_L);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new LoadGame());
		menu.add(menuItem);	
		
		if (bbms.GlobalFuncs.mapInitialized) {
			menu.addSeparator();
			
			menuItem = new JMenuItem("Save JNEAT Population");
			menuItem.addActionListener(new SavePopulation()); 
			menu.add(menuItem);
			
			menuItem = new JMenuItem("Load JNEAT Population");
			menuItem.addActionListener(new LoadPopulation());
			menu.add(menuItem);
			
			menu.addSeparator();
			
			menuItem = new JMenuItem("Save Scenario");
			menuItem.addActionListener(new SaveScenario());
			menu.add(menuItem);
						
		}
		
		menuItem = new JMenuItem("Load Scenario");
		menuItem.addActionListener(new LoadScenario());
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
		
		menuItem = new JMenuItem("Mode: Set Clear Terrain");		
		menuItem.addActionListener(new ModePaintClear());				
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Set Tree Terrain");		
		menuItem.addActionListener(new ModePaintTrees());				
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Set Tall Grass Terrain");		
		menuItem.addActionListener(new ModePaintTallGrass());				
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Set Vapor Source");
		menuItem.addActionListener(new ModeSetVaporSource());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Set Vapor Sink");
		menuItem.addActionListener(new ModeSetVaporSink());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Mode: Remove Vapor Source/Sink");
		menuItem.addActionListener(new ModeSetVaporNorm());
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
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Predict vapor equilibrium");
		menuItem.addActionListener(new PredictVapor());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Reset vapor density to default");
		menuItem.addActionListener(new ResetVapor());
		menu.add(menuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Copy console output to clipboard");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuItem.addActionListener(new CopyConsole());
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Test Population Generation");
		menuItem.addActionListener(new PopDialog());;
		menu.add(menuItem);
		
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
}
