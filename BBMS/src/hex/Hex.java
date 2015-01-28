package hex;

import gui.GUI_NB;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bbms.GlobalFuncs;
import terrain.*;
import unit.MoveClass;


public class Hex {
	
	public int x;
	public int y;
	public TerrainType tType;	// Type of terrain
	public TerrainEnum tEnum;
	public int elevation;		// Relative height of the ground (m)
	public int obsHeight;		// Additional height from buildings/obstacles
	public int density;			// Obstructs line of sight if cumulative density >30
	public int vapor;			// 0-255, used in the gas diffusion model
	public int vaporOut;		// How much the vapor level in this hex will change at the next iteration.
	public int vaporIn;
	public int deltaVapor;
	
	public boolean shaded;		// Will the hex be drawn shaded or not?
	public Color shadedColor;
	
	public boolean displayText;
	public String hexText;
	public Color textColor;
	
	public boolean highlighted;	// Is this hex highlighted (apart from shading)
	
	public int obscuration;	// Level of visual obscuration on the hex (adds to density, but can dissipate over time)	
	
	public unit.Unit HexUnit = null;
	
	// TODO: Include unit list of all units in this hex
	// TODO: Include a cover value which affects the deadliness of various types of weapons
	
	// Constructor with random height, obsHeight, and density
	public Hex (int xi, int yi, TerrainEnum iTerrain, int iElev) {
		this(xi, yi, iTerrain, 
				iElev + iTerrain.tType.generateHeight(), 
				iTerrain.tType.generateObsHeight(),
				iTerrain.tType.generateDensity(),
				0, 25500);			
	}
	
	// Constructor with specific height, obsHeight, density, and obscuration
	public Hex (int xi, int yi, TerrainEnum iTerrain, int iElev, int iObsHeight, int iDensity, int iObsc, int iVapor) {
		x = xi;
		y = yi;		
				
		shaded = false;
		shadedColor = Color.WHITE;
		highlighted = false;	
		
		displayText = false;
		hexText = "";
		textColor = Color.WHITE;
		tEnum = iTerrain;				
		tType = tEnum.tType;		// There's probably a more efficient way of doing this.
				
		elevation = iElev;		
		obsHeight = iObsHeight;
		density = iDensity;				
		
		obscuration = iObsc;		
		vapor = iVapor;
		//vapor = GlobalFuncs.randRange(0,  100);
		vaporIn = 0;
		vaporOut = 0;
	}
	
	/**
	 * Clones hex data EXCEPT terrain data (e.g. elevation, obs. height, density)
	 * @param h
	 */
	public void CloneHexData(Hex h) {
		shaded = h.shaded;
		shadedColor = h.shadedColor;
		highlighted = h.highlighted;
		
		displayText = h.displayText;
		hexText = h.hexText;
		textColor = h.textColor;
		
		obscuration = h.obscuration;
		vapor = h.vapor;
		deltaVapor = h.deltaVapor;
	}
	
	public void DisplayInfo() {
		System.out.print ("Hex: (" + x + ", " + y + ") is terrain type: " + tType.displayType() + " (" + tType.displayChar() + ")\n");
		System.out.print ("Elevation: " + elevation + "   Obs Height: " + obsHeight + "    Density: " + density + "    Obscur: " + obscuration + "\n");		
	}
	
	public void GCODisplay() {
		gui.GUI_NB.GCO("Hex: (" + x + ", " + y + ") is terrain type: " + tType.displayType() + " (" + tType.displayChar() + ")");
		gui.GUI_NB.GCO("Elevation: " + elevation + "   Obs Height: " + obsHeight + "    Density: " + density + "    Obscur: " + obscuration);
	}
	
	/**
	 * Sets the vapor component of this hex, between 0 and 255 inclusive.
	 * @param vi
	 */
	public void SetVapor(int vi) {
		if (vi >= 0 && vi <= 25500) vapor = vi;							
	}
	
	public void UpdateVapor() {
		vapor += vaporIn;
		vapor -= vaporOut;
		deltaVapor = vaporIn + vaporOut;
		vaporIn = 0;
		vaporOut = 0;
	}
	
	/**
	 * DEFAULTS to MoveClass.TRACK
	 */
	public void CalcVapor() {

		
		for (int direction = 0; direction < 6; direction++) {
			HexOff target = new HexOff(x, y).findNeighbor(direction);
			Hex tgtHex = GlobalFuncs.scenMap.getHex(target.x, target.y);
			if (tgtHex.tEnum != TerrainEnum.INVALID) {
				int moveCost = Math.max(this.tType.getMoveCost(MoveClass.TRACK), 
						tgtHex.tType.getMoveCost(MoveClass.TRACK));
				double vaporXferRate = 0.16 / moveCost;
				int dVapor = vapor - tgtHex.vapor;				

				
				// Nothing "sucks" in this universe.  Vapor moves from high pressure to low pressure only.
				if (dVapor > 0) {
					int vaporXferAmount = (int)(dVapor * vaporXferRate); //Math.max((int)(dVapor * vaporXferRate), 1);
					vaporOut += vaporXferAmount;
					tgtHex.vaporIn += vaporXferAmount;					
				}		
				
				// DEBUG
				
				/*
				if (GlobalFuncs.highlightedHex != null) {					
					if (GlobalFuncs.highlightedHex.x == x && GlobalFuncs.highlightedHex.y == y) {
						GUI_NB.GCO("Direction " + direction + " delta Vapor: " + dVapor + " Cumulative: " + vaporOut + " Xfer Rate: " + vaporXferRate);
					}
				}
				
				*/

			}			
		}
		

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
			background = new File(tType.getTerrainEnum().backgroundFile);
			if (tType.getTerrainEnum().foregroundFile != null) {
				foreground = new File(tType.getTerrainEnum().foregroundFile);
			}
						
			BufferedImage img = ImageIO.read(background);
			g.drawImage(img,  x,  y, null);
			if (foreground != null) {
				img = ImageIO.read(foreground);
				g.drawImage(img,  x,  y,  null);
			}
			
			if (displayText) {
				Color oldBrush = g.getColor();
				g.setColor(textColor);
				g.drawString(hexText, xi - hexText.length() * 3, yi + 6);
				
				g.setColor(oldBrush);
			}
			
			if (GlobalFuncs.showVapor){
				Color oldBrush = g.getColor();
				g.setColor(textColor);
				String vaporText = String.valueOf(vapor);				
				g.drawString(vaporText,  xi - vaporText.length() * 3,  yi - 6);
				
				vaporText = String.valueOf(deltaVapor);
				g.drawString(vaporText, xi - vaporText.length() * 3, yi + 6);
				
				g.setColor(oldBrush);
			}
			
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			GUI_NB.GCO(ie.getMessage());
		}				
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
	
	public HexOff toHO() {
		HexOff converted = new HexOff(x, y);
		return converted;
	}		
	
	// NOTE: Does NOT save hex text 
	public String saveHex() {
		String output = tEnum.id + ", " + elevation + ", " + obsHeight + ", " + density + ", " + obscuration + ", " + vapor;
		
		return output;
	}
	
}
