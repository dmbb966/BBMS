package hex;

import java.awt.Color;
import java.util.Vector;

import terrain.TerrainEnum;
import bbms.GlobalFuncs;

public class HexMap {
	
	int xDim, yDim;
	Hex[][] hexArray;
	
	Vector<hex.Hex> shadedHexList;
	
	public HexMap(int x, int y) {
		xDim = x;
		yDim = y;
		hexArray = new Hex[xDim][yDim];
		shadedHexList = new Vector<Hex>();
		GenerateMap();
	}
	
	public void shadeHex(Hex h, Color c) {
		h.shadedColor = c;
		h.shaded = true;
		shadedHexList.add(h);
	}
	
	public void unshadeHex(Hex h) {
		h.shaded = false;
		shadedHexList.remove(h);
	}
	
	public void displayShadedList() {
		bbms.GUI_NB.GCO("There are " + shadedHexList.size() + " hexes shaded.");
		for (int i = 0; i < shadedHexList.size(); i++) {
			bbms.GUI_NB.GCO("Hex " + i + " at " + shadedHexList.elementAt(i).x + ", " + shadedHexList.elementAt(i).y);
		}
	}
	
	public void unshadeAll() {
		for (int i = shadedHexList.size() - 1; i >= 0; i--) {
			unshadeHex(shadedHexList.elementAt(i));
		}
		bbms.GlobalFuncs.gui.repaint();
	}
	
	/**
	 * NOTE: For now, does not change the default elevation from 0
	 */
	public void GenerateMap() {
		for (int y = 0; y < yDim; y++) {
			for (int x = 0; x < xDim; x++) {
				int r = GlobalFuncs.randRange(1, 3);
				TerrainEnum tType = TerrainEnum.CLEAR;		// Default
				switch(r){
					case 1:
						tType = TerrainEnum.CLEAR;						
						break;
					case 2:
						tType = TerrainEnum.T_GRASS;						
						break;
					case 3:
						tType = TerrainEnum.TREES;
						break;
				};
				hexArray[x][y] = new Hex(x, y, tType, 0);
				// hexArray[x][y].GCODisplay();
			}
		}
	}
	
	public Hex getHex(int getX, int getY) {
		if (getX >= xDim || getX < 0 || getY >= yDim || getY < 0) {
			// GUI_NB.GCO("ERROR: Cannot get hex at " + getX + ", " + getY + " because map dimensions are " + xDim + " x " + yDim);
			return new Hex(getX, getY, TerrainEnum.INVALID, 0);
		}
		
		return hexArray[getX][getY];
	}
	
	public int getXDim() {
		return xDim;
	}
	
	public int getYDim() {
		return yDim;
	}

}
