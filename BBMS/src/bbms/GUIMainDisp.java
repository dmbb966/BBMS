package bbms;

import hex.Hex;
import hex.HexMap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import unit.Unit;

@SuppressWarnings("serial")
public class GUIMainDisp extends JPanel {
	
	private int squareX = 50;
	private int squareY = 50;
	private int squareW = 20;
	private int squareH = 20;
	private static int defaultHexSize = 30;		
	
	// ULH corner of the display frame is centered on these hexes
	public int mapDisplayX = 0;
	public int mapDisplayY = 0;
	
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
				// GlobalFuncs.gui.BI_Hex.setText("Hellow!");
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
		hex.HexOff result = interim.ConvertToOff();
		
		return new hex.HexOff(result.getX() + GlobalFuncs.gui.GMD.mapDisplayX, result.getY() + GlobalFuncs.gui.GMD.mapDisplayY);
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
	
    public static void KeyReleasedEvents (java.awt.event.KeyEvent evt)
    {
    	GUI_NB.GCO("Key released");
        
    }
	
	public void MouseClickedEvents(java.awt.event.MouseEvent e)
	{
		if (!GlobalFuncs.mapInitialized) return;
		
		hex.HexOff cursorHexOff;
		hex.Hex h;
		switch(GlobalFuncs.placeUnit) {
		case 0:
			// Selects units if there are any in the hex
			cursorHexOff = pixelToHexOff(e.getX(), e.getY(), -defaultHexSize, -defaultHexSize);
			Hex clickedHex = GlobalFuncs.scenMap.getHex(cursorHexOff.getX(), cursorHexOff.getY());
			
			// Left button - select unit
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
				if (clickedHex.HexUnit == null) {
					GUI_NB.GCO("Hex " + clickedHex.x + ", " + clickedHex.y + " has no units.");
					GlobalFuncs.highlightedHex = clickedHex;
				}
				else {
					GUI_NB.GCO("Hex " + clickedHex.x + ", " + clickedHex.y + " has units.");
					GlobalFuncs.highlightedHex = clickedHex;
					GlobalFuncs.selectedUnit = clickedHex.HexUnit;
					GUI_NB.GCO(GlobalFuncs.selectedUnit.DispUnitInfo());
					
				}
			}
			
			
			// RIght button
			if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
				if (GlobalFuncs.selectedUnit != null) {
					if (clickedHex.HexUnit == null) {
						if (GlobalFuncs.RotateHull){							
							GlobalFuncs.selectedUnit.OrientHullTo(clickedHex.x, clickedHex.y);
						} else {							
							GlobalFuncs.selectedUnit.OrientTurretTo(clickedHex.x, clickedHex.y);
						}
						
						GlobalFuncs.gui.repaint();
						
					} else if(clickedHex.HexUnit == GlobalFuncs.selectedUnit) {
						GUI_NB.GCO("You clicked on yourself!");
					} else {
						GlobalFuncs.selectedUnit.target = clickedHex.HexUnit;
						GlobalFuncs.selectedUnit.OrientTurretTo(GlobalFuncs.selectedUnit.target.location.x, GlobalFuncs.selectedUnit.target.location.y);
						GUI_NB.GCO("New target selected, aiming weapon system.");
					}					
				}
			}
			

		
			break;
		case 1:
			// Places M1A2 at cursor hex
			cursorHexOff = pixelToHexOff(e.getX(), e.getY(), -defaultHexSize, -defaultHexSize);
			h = GlobalFuncs.scenMap.getHex(cursorHexOff.getX(), cursorHexOff.getY());
			GUI_NB.GCO("Adding M1A2");
			if (h != null && h.HexUnit == null) {
				h.HexUnit = new unit.Unit(h, unit.SideEnum.FRIENDLY, "M1A2", "Dealer 66");	
				GUI_NB.GCO(h.HexUnit.DispUnitInfo());
			}			
			break;
		case 2:
			// Places T-72 at cursor hex
			GUI_NB.GCO("Adding T-72");
			cursorHexOff = pixelToHexOff(e.getX(), e.getY(), -defaultHexSize, -defaultHexSize);
			h = GlobalFuncs.scenMap.getHex(cursorHexOff.getX(), cursorHexOff.getY());
			if (h != null && h.HexUnit == null) {
				h.HexUnit = new unit.Unit(h, unit.SideEnum.ENEMY, "T-72", "Warmonger 5");
				GUI_NB.GCO(h.HexUnit.DispUnitInfo());
				
			}
			break;
		}
		
		//GlobalFuncs.gui.validate();
		GlobalFuncs.gui.repaint();
		
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
		
		// Number of hexes to iterate over		
		int xd = hexesX; //Math.min(hexesX, hMap.getXDim());
		int yd = hexesY; //Math.min(hexesY, hMap.getYDim());		
		
		// Specific points as it iterates over the display
		int xPoint = 0;
		int yPoint = 0;	
		
		// TODO - I have some objects crossed here...
		mapDisplayX = GlobalFuncs.gui.GMD.mapDisplayX;
		mapDisplayY = GlobalFuncs.gui.GMD.mapDisplayY;
		
		// GUI_NB.GCO("DEBUG: Map display is at " + mapDisplayX + ", " + mapDisplayY);
		// GUI_NB.GCO("DEBUG: Object map display " + GlobalFuncs.gui.GMD.mapDisplayX + ", " + GlobalFuncs.gui.GMD.mapDisplayY);
		
		for (int y = 0; y < yd; y++) {
			for (int x = 0; x < xd; x++) {
				xPoint = (int) (hexSize * Math.sqrt(3.0) * (x + 0.5 * (y & 1))) + defaultHexSize;
				yPoint = (int) (1.5 * hexSize * y) + defaultHexSize;
				
				Hex currentHex = hMap.getHex(x + mapDisplayX, y + mapDisplayY);
				currentHex.DrawHex(xPoint, yPoint, hexSize, g);
				
				g.drawPolygon(genHex(xPoint, yPoint, hexSize));
				
			}
		}
		
		// Second loop if you are displaying shaded and highlighted hexes
		// TODO: This can be implemented a lot more efficiently.
		if (GlobalFuncs.showShaded) {		
			for (int y = 0; y < yd; y++) {
				for (int x = 0; x < xd; x++) {
					xPoint = (int) (hexSize * Math.sqrt(3.0) * (x + 0.5 * (y & 1))) + defaultHexSize;
					yPoint = (int) (1.5 * hexSize * y) + defaultHexSize;
					
					Hex currentHex = hMap.getHex(x + mapDisplayX, y + mapDisplayY);
					if (currentHex.shaded) {
						g.setColor(currentHex.shadedColor);
						g.drawPolygon(genHex(xPoint, yPoint, hexSize));
					}
				}
			}
		}
		
		// If there is a highlighted hex, it will highlight it last
		if (GlobalFuncs.highlightedHex != null) {
			int x = GlobalFuncs.highlightedHex.x - mapDisplayX;
			int y = GlobalFuncs.highlightedHex.y - mapDisplayY;
			
			xPoint = (int) (hexSize * Math.sqrt(3.0) * (x + 0.5 * (y & 1))) + defaultHexSize;
			yPoint = (int) (1.5 * hexSize * y) + defaultHexSize;
			
			g.setColor(Color.YELLOW);
			g.drawPolygon(genHex(xPoint, yPoint, hexSize));
		}
		
		// Highlights current unit hex
		if (GlobalFuncs.selectedUnit != null) {
			int x = GlobalFuncs.selectedUnit.location.x - mapDisplayX;
			int y = GlobalFuncs.selectedUnit.location.y - mapDisplayY;
			
			xPoint = (int) (hexSize * Math.sqrt(3.0) * (x + 0.5 * (y & 1))) + defaultHexSize;
			yPoint = (int) (1.5 * hexSize * y) + defaultHexSize;
			
			g.setColor(Color.GREEN);
			g.drawPolygon(genHex(xPoint, yPoint, hexSize));
		}
		
		// Highlights current unit's target
		if (GlobalFuncs.selectedUnit != null) {
			if (GlobalFuncs.selectedUnit.target != null) {
				int x = GlobalFuncs.selectedUnit.target.location.x - mapDisplayX;
				int y = GlobalFuncs.selectedUnit.target.location.y - mapDisplayY;
				
				xPoint = (int) (hexSize * Math.sqrt(3.0) * (x + 0.5 * (y & 1))) + defaultHexSize;
				yPoint = (int) (1.5 * hexSize * y) + defaultHexSize;
				
				g.setColor(Color.RED);
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
	
	public void drawUnits(Graphics g, int hexSize) {
		unit.Unit u;
		for (int i = 0; i < GlobalFuncs.unitList.size(); i++) {
			u = (Unit) GlobalFuncs.unitList.elementAt(i);
			// GUI_NB.GCO(u.DispUnitInfo());
			
			
			// u.DrawUnit(xi, yi, g);
			
			int hWidth = (int) (Math.sqrt(3) * hexSize);
			int hHeight = (int) (1.5 * hexSize);
			
			// Number of hexes to display
			int hexesX = (710 / hWidth) + 1;
			int hexesY = (530 / hHeight) + 1;
			
			// Specific points as it iterates over the display
			int xPoint = 0;
			int yPoint = 0;
			
			int unitX = u.location.x;
			int unitY = u.location.y;
			
			if (unitX >= mapDisplayX - 1 && unitX <= mapDisplayX + hexesX) {
				if (unitY >= mapDisplayY - 1 && unitY <= mapDisplayY + hexesY) {
					// Unit icon is within the display bounds, will draw it now.
					int relativeHexX = unitX - mapDisplayX;
					int relativeHexY = unitY - mapDisplayY;
					xPoint = (int) (hexSize * Math.sqrt(3.0) * (relativeHexX + 0.5 * (relativeHexY & 1))) + defaultHexSize;
					yPoint = (int) (1.5 * hexSize * relativeHexY) + defaultHexSize;
					
					u.DrawUnit(xPoint, yPoint, g);
				}
			}
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
		drawUnits(g, 30);
		// drawHexMapComposite(15, 15, defaultHexSize, g);
	}

}
