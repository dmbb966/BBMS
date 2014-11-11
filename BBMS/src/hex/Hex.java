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
import java.awt.image.IndexColorModel;
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
	
	public boolean shaded;		// Will the hex be drawn shaded or not?
	public boolean highlighted;	// Is this hex highlighted (apart from shading)
	
	public int obscuration;	// Level of visual obscuration on the hex (adds to density, but can dissipate over time)	
	
	public unit.Unit HexUnit = null;
	
	// TODO: Include unit list of all units in this hex
	// TODO: Include a cover value which affects the deadliness of various types of weapons
	
	public Hex (int xi, int yi, TerrainEnum iTerrain, int iElev) {
		x = xi;
		y = yi;		
				
		shaded = false;
		highlighted = false;
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
				background = new File("src/hex/graphics/Pavement-Z44.png");
			};
			//background = new File("src/hex/graphics/test.bmp");
			BufferedImage img = ImageIO.read(background);
			// BufferedImage index = convertType(img, BufferedImage.TYPE_BYTE_INDEXED);
			// index = rescale(index, 0.5f, 0);
			
			// RescaleOp op = new RescaleOp(1.0f, 0, null);
			// img = op.filter(img, null);
			
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
			
			/*
			BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = bimg.createGraphics();
			g2d.drawImage(img, 0, 0, null);
			g2d.dispose();
			if (shaded) {
				float scaleFactor = 0.5f;
				RescaleOp op = new RescaleOp(scaleFactor, 0, null);
				bimg = op.filter(bimg, null);				
			}  
			*/
			
			g.drawImage(img,  x,  y, null);
			if (foreground != null) {
				img = ImageIO.read(foreground);
				g.drawImage(img,  x,  y,  null);
			}
			
			/* if (shaded) {
				g.drawString("S", x, y);
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

	// https://community.oracle.com/thread/1269537?start=0&tstart=0
	public static BufferedImage convertType(BufferedImage img, int typeByteIndexed) {
		if (img.getType() == typeByteIndexed) return img;
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), typeByteIndexed);
		Graphics2D g2d = result.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		//g2d.drawRenderedImage(img,  null);
		g2d.dispose();
		return result;
	}
	
	// https://community.oracle.com/thread/1269537?start=0&tstart=0
	public static IndexColorModel rescale (IndexColorModel icm, float scaleFactor, float offset) {
		int size = icm.getMapSize();
		byte[] reds = new byte[size];
		byte[] greens = new byte[size];
		byte[] blues = new byte[size];
		byte[] alphas = new byte[size];
		
		icm.getReds(reds);
		icm.getGreens(greens);
		icm.getBlues(blues);
		icm.getAlphas(alphas);
		
		rescale(reds, scaleFactor, offset);
		rescale(greens, scaleFactor, offset);
		rescale(blues, scaleFactor, offset);
		
		return new IndexColorModel(8, size, reds, greens, blues, alphas);
	}
	
	// https://community.oracle.com/thread/1269537?start=0&tstart=0
	public static void rescale(byte[] comps, float scaleFactor, float offset) {
		for (int i = 0; i < comps.length; i++) {
			int comp = 0xff & comps[i];			
			int newComp = Math.round(comp * scaleFactor + offset);
			if (newComp < 0) newComp = 0;
			else if (newComp > 255) newComp = 255;
			comps[i] = (byte) newComp;
		}
	}
	
	// https://community.oracle.com/thread/1269537?start=0&tstart=0
	public static BufferedImage rescale (BufferedImage indexed, float scaleFactor, float offset) {
		IndexColorModel icm = (IndexColorModel) indexed.getColorModel();
		return new BufferedImage(rescale(icm, scaleFactor, offset), indexed.getRaster(), false, null);
	}
}
