package bbms;

import hex.HexMap;
import hex.HexOff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUIMainDisp extends JPanel {
	
	private int squareX = 50;
	private int squareY = 50;
	private int squareW = 20;
	private int squareH = 20;
	private static int defaultHexSize = 30;		
	
	private Polygon poly;
	private Polygon[] polyMap = {};
	
	public GUIMainDisp() {
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// moveSquare(e.getX(), e.getY());
				// drawHex(e.getX(), e.getY(), 20);
				// drawHexMap(e.getX(), e.getY(), 3, 3, 40);
				// GUI_NB.GCO("Repainting.");
				// repaint();
				GlobalFuncs.gui.BI_Hex.setText("Hellow!");
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				// moveSquare(e.getX(), e.getY());
			}
		});
		
		
	}
	
	private static hex.HexAx pixelToHex(int x, int y, int offsetX, int offsetY)
	{
		// Courtesy of http://www.redblobgames.com/grids/hexagons/
		// This finds the approximate axial coordinates
		double q = (1.0/3.0 * Math.sqrt(3) * (x + offsetX) - 1.0/3.0 * (y + offsetY)) / defaultHexSize;
		double r = (2.0/3.0 * (y + offsetY)) / defaultHexSize;
					
		// Rounds to precise hex 
		double z = -q - r;
		// GUI_NB.GCO(hex.HexAx.RoundAx(q,  z,  r).ConvertToOff().DisplayHexStr());
		return hex.HexAx.RoundAx(q,  z,  r); //.ConvertToOff();
	}
	
	private static hex.HexOff pixelToHexOff(int x, int y, int offsetX, int offsetY) {
		hex.HexAx interim = pixelToHex(x, y, offsetX, offsetY);
		return interim.ConvertToOff();
	}
	
	public static void MouseMotionEvents(java.awt.event.MouseEvent e)
	{
		// GUI_NB.GCO("Mouse moved to: (" + e.getX() + ", " + e.getY() + ")");
		
		if (GlobalFuncs.mapInitialized) {			
			hex.HexOff cursorHexOff = pixelToHexOff(e.getX(), e.getY(), -defaultHexSize, -defaultHexSize);
			
			/*
			hex.Hex h = GlobalFuncs.scenMap.getHex(cursorHexOff.getX(),  cursorHexOff.getY());
			if (h != null) h.GCODisplay(); */
			
			GlobalFuncs.gui.BI_Hex.setText("Curs: " + e.getX() + ", " + e.getY());
			GUIBasicInfo.UpdateHexInfo(cursorHexOff.getX(), cursorHexOff.getY());

		}
		
		// GUI_NB.GCO("Cursor Hex is: " + cursorHex.DisplayHexStr() + " with cursor at: (" + e.getX() + ", " + e.getY() + ")");			
	}
	
	public static void MouseClickedEvents(java.awt.event.MouseEvent e)
	{
		
	}
	
	public void drawHex(int x, int y, int size) {
		int[] xPoly = new int[6];
		int[] yPoly = new int[6];
		
		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI / 6 * (i + 0.5);			
			xPoly[i] = (int) (x + size * Math.cos(angle));
			yPoly[i] = (int) (y + size * Math.sin(angle));;
		}
		
		for (int i = 0; i < 5; i++) {
			
			GUI_NB.GCO("Coordinate " + i + " is: (" + xPoly[i] + ", " + yPoly[i] + ")");
		}
		
		poly = new Polygon(xPoly, yPoly, xPoly.length);
		
				
	}
	
	public Polygon genHex(int x, int y, int size) {
		int[] xPoly = new int[6];
		int[] yPoly = new int[6];		
		
		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI / 6 * (i + 0.5);			
			xPoly[i] = (int) (x + size * Math.cos(angle));
			yPoly[i] = (int) (y + size * Math.sin(angle));;
		}
			
		return new Polygon(xPoly, yPoly, xPoly.length);								
	}
	
	public void drawHexMap(int xi, int yi, int sizeX, int sizeY, int hexSize) {
		
		polyMap = new Polygon[sizeX * sizeY];
		int hWidth = (int) (Math.sqrt(3) * hexSize);
		int hHeight = (int) (1.5 * hexSize);
		int index = 0;
		
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				index = x + (y * sizeX);
				
				// Even-numbered row - hexes are aligned all the way to the right
				if (y % 2 == 0) {
					polyMap[index] = genHex(xi + (hWidth * x), yi + (hHeight * y), hexSize);
				}
				// Odd-numbered row - hexes are offset to the right
				else {
					polyMap[index] = genHex(xi + (int)(hWidth * (x + 0.5)), yi + (hHeight * y), hexSize); 
				}
			}
		}				
	}
	
	public void drawHexMapComposite(HexMap hMap, int hexSize, Graphics g) {
		if (hMap == null) return;
		
		int hWidth = (int) (Math.sqrt(3) * hexSize);
		int hHeight = (int) (1.5 * hexSize);
		
		// Number of hexes to display
		int hexesX = (710 / hWidth) + 1;
		int hexesY = (530 / hHeight) + 1;
		
		// Number of hexes to interate over
		int xi = 0;
		int yi = 0;
		int xf = Math.min(xi + hexesX, hMap.getXDim());
		int yf = Math.min(yi + hexesY, hMap.getYDim());
		int xd = xf - xi;
		int yd = yf - yi;
		
		// Specific points as it iterates over the display
		int xPoint = 0;
		int yPoint = 0;
		
		for (int y = 0; y < yd; y++) {
			for (int x = 0; x < xd; x++) {
				yPoint = hexSize + (hHeight * y);
				// Even-numbered rows: hexes are aligned all the way to the right
				if (y % 2 == 0) {
					xPoint = hexSize + (hWidth * x);
				}
				// Odd-numbered rows: hexes are offset to the right
				else {
					xPoint = hexSize + (int)(hWidth * (x + 0.5));
				}
				
				hMap.getHex(x + xi, y + yi).DrawHex(xPoint, yPoint, hexSize, g);
				g.drawPolygon(genHex(xPoint, yPoint, hexSize));
			}
		}
	}
	
	public void drawHexMapComposite(int sizeX, int sizeY, int hexSize, Graphics g) {		
		int hexX = 0;
		int hexY = 0;
		int hWidth = (int) (Math.sqrt(3) * hexSize);
		int hHeight = (int) (1.5 * hexSize);
		int xi = hexSize;
		int yi = hexSize;
		
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				
				hexY = yi + (hHeight * y);
				// Even-numbered row - hexes are aligned all the way to the right
				if (y % 2 == 0) {
					hexX = xi + (hWidth * x);														
				}
				// Odd-numbered row - hexes are offset to the right
				else {
					hexX = xi + (int)(hWidth * (x + 0.5));		
										
				}
				
				try {
					int r = GlobalFuncs.randRange(1, 3);
					File input = null;
					File input2 = null;
					switch(r){
						case 1:
							input = new File("src/hex/graphics/Grassland1-Z4.png");
							break;
						case 2:
							input = new File("src/hex/graphics/HighGrass1-Z4.png");
							break;
						case 3:
							input = new File("src/hex/graphics/Grassland1-Z4.png");
							input2 = new File("src/hex/graphics/Trees1-Z4.png");
							break;
					};
										
					Image image = ImageIO.read(input);
					g.drawImage(image,  hexX - (hWidth / 2) - 1,  hexY - (hHeight / 2) - 8, null);
					if (input2 != null) {
						image = ImageIO.read(input2);
						g.drawImage(image,  hexX - (hWidth / 2) - 1,  hexY - (hHeight / 2) - 8, null);
					}
					
					GUI_NB.GCO("hexY: " + hexY + "  || hHeight: " + hHeight + "  || actualen: " + (hexY - (hHeight / 2) - 8));
				} catch (IOException ie) {
					System.out.println(ie.getMessage());
				}
				
				g.drawPolygon(genHex(hexX, hexY, hexSize));
				
				g.drawString(x + ", " + y, hexX - (int)(hexSize / 3), hexY);
			}
		}				
	}
	
	public void moveSquare(int x, int y) {
		int OFFSET = 1;
		if ((squareX != x) || (squareY != y)) {
			repaint(squareX, squareY, squareW+OFFSET, squareH+OFFSET);
			squareX = x;
			squareY = y;
			repaint(squareX, squareY, squareW+OFFSET, squareH+OFFSET);
		}
	}
	
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		
		// Draw text		
		//g.drawString("Custom panel.",  10, 20);		
		
		/*g.setColor(Color.RED);
		g.fillRect(squareX, squareY, squareW, squareH);
		g.setColor(Color.BLACK);
		g.drawRect(squareX, squareY, squareW, squareH); */
		
		drawHexMapComposite(GlobalFuncs.scenMap, 30, g);
		// drawHexMapComposite(15, 15, defaultHexSize, g);
	}

}
