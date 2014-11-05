package hex;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bbms.GUI_NB;
import bbms.GlobalFuncs;
import terrain.*;


public class Hex {
	
	public int x;
	public int y;
	public TerrainType tType;	// Type of terrain
	public int elevation;		// Relative height of the ground (m)
	public int obsHeight;		// Additional height from buildings/obstacles
	public int density;		// Obstructs line of sight if cumulative density >30
	
	public boolean shaded;	// Will the hex be drawn shaded or not?
	
	public int obscuration;	// Level of visual obscuration on the hex (adds to density, but can dissipate over time)	
	
	public unit.Unit HexUnit = null;
	
	// TODO: Include unit list of all units in this hex
	// TODO: Include a cover value which affects the deadliness of various types of weapons
	
	public Hex (int xi, int yi, TerrainEnum iTerrain, int iElev) {
		x = xi;
		y = yi;		
				
		shaded = false;
		if (GlobalFuncs.randRange(0, 1) == 0) shaded = true;
		
		switch (iTerrain) {
		case CLEAR: tType = new ClearTerrain(); break;
		case TREES: tType = new TreesTerrain(); break;
		case T_GRASS: tType = new TallGrassTerrain(); break;
		case INVALID: tType = new InvalidTerrain(); break;
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
			case INVALID:
				background = new File("src/hex/graphics/Pavement-Z4.png");
			};
			//background = new File("src/hex/graphics/test.bmp");
			Image img = ImageIO.read(background);
			//img = Transparency.makeColorTransparent(img,  new Color(0).white);
/*
			// Create a buffered image with transparency
		    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		    // Draw the image on to the buffered image
		    Graphics2D bGr = bimage.createGraphics();
		    bGr.drawImage(img, 0, 0, null);
		    bGr.dispose();			
			RescaleOp op = new RescaleOp(1.0f, 0, null);
			BufferedImage oimg = op.filter(bimage,  null);
			// BufferedImage bimg = (BufferedImage) img;
			// bimg = op.filter(bimg, null);
	*/		
			
			
			BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = bimg.createGraphics();
			g2d.drawImage(img, 0, 0, null);
			g2d.dispose();
			if (shaded) {
				float scaleFactor = 0.5f;
				RescaleOp op = new RescaleOp(scaleFactor, 0, null);
				bimg = op.filter(bimg, null);				
			}  
			g.drawImage(bimg,  x,  y, null);
			/* if (foreground != null) {
				img = ImageIO.read(foreground);
				g.drawImage(img,  x,  y,  null);
			} */
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
