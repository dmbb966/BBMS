package hex;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bbms.GUI_NB;
import terrain.*;


public class Hex {
	
	public int x;
	public int y;
	public TerrainType tType;	// Type of terrain
	public int elevation;		// Relative height of the ground (m)
	public int obsHeight;		// Additional height from buildings/obstacles
	public int density;		// Obstructs line of sight if cumulative density >30
	
	public int obscuration;	// Level of visual obscuration on the hex (adds to density, but can dissipate over time)	
	
	public unit.Unit HexUnit = null;
	
	// TODO: Include unit list of all units in this hex
	// TODO: Include a cover value which affects the deadliness of various types of weapons
	
	public Hex (int xi, int yi, TerrainEnum iTerrain, int iElev) {
		x = xi;
		y = yi;		
		
		switch (iTerrain) {
		case CLEAR: tType = new ClearTerrain(); break;
		case TREES: tType = new TreesTerrain(); break;
		case T_GRASS: tType = new TallGrassTerrain(); break;
		}
		
		elevation = iElev + tType.generateHeight();		
		obsHeight = tType.generateObsHeight();
		density = tType.generateDensity();				
		
		obscuration = 0;		
	}
	
	public void DisplayInfo() {
		System.out.print ("Hex: (" + x + ", " + y + ") is terrain type: " + tType.displayType() + " (" + tType.displayChar() + ")\n");
		System.out.print ("Elevation: " + elevation + "   Obs Height: " + obsHeight + "    Density: " + density + "    Obscur: " + obscuration + "\n");		
	}
	
	public void GCODisplay() {
		bbms.GUI_NB.GCO("Hex: (" + x + ", " + y + ") is terrain type: " + tType.displayType() + " (" + tType.displayChar() + ")");
		bbms.GUI_NB.GCO("Elevation: " + elevation + "   Obs Height: " + obsHeight + "    Density: " + density + "    Obscur: " + obscuration);
	}
	
	/**
	 * Draws this hex (to include terrain and any units in the hex) to the GUI Main Display.
	 * Size is the size of the hex in pixels
	 */
	public void DrawHex(int xi, int yi, int size, Graphics g) {
		// Loads the appropriate hex icon
		File background = null;
		File foreground = null;
		
		int hWidth = (int) (Math.sqrt(3) * size);
		int hHeight = (int) (1.5 * size);
		
		int x = xi - (hWidth / 2) - 1;
		int y = yi - (hHeight / 2) - 8;
		
		try {
			switch(tType.getTerrainEnum()){
			case CLEAR:
				background = new File("src/hex/graphics/Grassland1-Z4.png"); 
				break;
			case T_GRASS:
				background = new File("src/hex/graphics/HighGrass1-Z4.png");
				break;
			case TREES:
				background = new File("src/hex/graphics/Grassland1-Z4.png");
				foreground = new File("src/hex/graphics/Trees1-Z4.png");
				break;
			};
			
			Image img = ImageIO.read(background);
			g.drawImage(img,  x,  y, null);
			if (foreground != null) {
				img = ImageIO.read(foreground);
				g.drawImage(img,  x,  y,  null);
			}
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			GUI_NB.GCO(ie.getMessage());
		}				
		
		/*
		if (HexUnit != null) {
			HexUnit.DrawUnit(xi, yi, g);
		} */
	}
}
