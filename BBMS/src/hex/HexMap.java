package hex;

import java.awt.Color;
import java.util.Vector;

import terrain.TerrainEnum;
import bbms.GlobalFuncs;

public class HexMap {
	
	private int randPool;
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
		if (h.shaded) return;
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
	
	public TerrainEnum chooseTerrain(int r) {
		randPool = r;
		if (TerrainEval(randPool, GlobalFuncs.chanceClear)) return TerrainEnum.CLEAR;
		if (TerrainEval(randPool, GlobalFuncs.chanceHighGrass)) return TerrainEnum.T_GRASS;
		if (TerrainEval(randPool, GlobalFuncs.chanceTrees)) return TerrainEnum.TREES;
		return TerrainEnum.INVALID;					
	}
	
	public boolean TerrainEval(int r, int threshold) {
		if (r <= threshold) return true;
		else {
			randPool -= threshold;
			return false;
		}
	}
	
	/**
	 * NOTE: For now, does not change the default elevation from 0
	 */
	public void GenerateMap() {
		for (int y = 0; y < yDim; y++) {
			for (int x = 0; x < xDim; x++) {
				TerrainEnum tType = chooseTerrain(GlobalFuncs.randRange(1, GlobalFuncs.totalWeight));
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
