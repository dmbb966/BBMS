package hex;

import gui.GUI_NB;

import java.awt.Color;
import java.nio.file.Path;
import java.util.Vector;

import clock.Clock;
import terrain.TerrainEnum;
import utilities.FIO;
import bbms.GlobalFuncs;

public class HexMap {
	
	private int randPool;
	int xDim, yDim;
	public Hex[][] hexArray;
	
	Vector<hex.Hex> shadedHexList;
	Vector<hex.Hex> textHexList;
	Vector<hex.Hex> vaporSourceList;
	Vector<hex.Hex> vaporSinkList; 
	public static int chanceTrees = 0;
	public static int chanceHighGrass = 0;
	public static int chanceClear = 60;
	public static int totalWeight = chanceClear + chanceHighGrass + chanceTrees;
	
	public static final int SUBHEX_SIZE = 5000;		// Sub units in one 50m hex
	
	public HexMap(int x, int y, boolean cleanMap) {
		xDim = x;
		yDim = y;
		hexArray = new Hex[xDim][yDim];
		shadedHexList = new Vector<Hex>();
		textHexList = new Vector<Hex>();
		vaporSourceList = new Vector<Hex>();
		vaporSinkList = new Vector<Hex>();
		if (!cleanMap) GenerateMap();
	}
	
	public HexMap(int x, int y) {
		this(x, y, true);
	}
	
	public void calcAllVapor() {
		for (int y = 0; y < yDim; y++) {
			for (int x = 0; x < xDim; x++) {
				Hex finger = hexArray[x][y];
				finger.CalcVapor();
			}
		}
	}
	
	/** 
	 * Checks vapor sources and sinks to determine the anticipated change for the next turn.
	 */
	public void updateVaporSS() {
		for (int i = 0; i < vaporSinkList.size(); i++) {
			Hex finger = vaporSinkList.elementAt(i);
			int DV = finger.ReturnVaporCalc();
			if (DV > GlobalFuncs.maxDelta) GlobalFuncs.maxDelta = DV; 					
		}
		
		for (int i = 0; i < vaporSourceList.size(); i++) {
			Hex finger = vaporSourceList.elementAt(i);
			int DV = finger.ReturnVaporCalc();			
			if (DV > GlobalFuncs.maxDelta) GlobalFuncs.maxDelta = DV;
		}
	}
	
	/**
	 * Calculates the flow rate based on the max change at source and sink.
	 * Intended to speed up the gas diffusion process.  Max acceleration is capped at x3
	 * @return
	 */
	public void recalcFlowRate() {
		if (GlobalFuncs.reduceRate) {
			GlobalFuncs.reduceRate = false;
			GlobalFuncs.flowRate -= GlobalFuncs.flowStep * 5.0;
		}
		else if (GlobalFuncs.ticksStable % GlobalFuncs.flowCheck == GlobalFuncs.flowCheck - 1) {
			if (GlobalFuncs.flowRate < GlobalFuncs.flowRateCap) GlobalFuncs.flowRate += GlobalFuncs.flowStep;						
		}
	}
	
	public void updateAllVapor() {
		
		long oldTV = GlobalFuncs.totalVapor;
		GlobalFuncs.totalVapor = 0;
		
		for (int y = 0; y < yDim; y++) {
			for (int x = 0; x < xDim; x++) {
				Hex finger = hexArray[x][y];
				finger.UpdateVapor();
			}
		}
		
		GlobalFuncs.totalVaporDelta = (int)(oldTV - GlobalFuncs.totalVapor);		
		
		UpdateSourceSink();
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
	
	public void setHexText(Hex h, String s, Color c) {
		h.textColor = c;
		h.hexText = s;
		if (h.displayText) return;
		h.displayText = true;
		textHexList.add(h);		
	}	
	
	public void showHexText(Hex h) {
		setHexText(h, h.hexText, h.textColor);
	}
	
	public void hideHexText(Hex h) {
		h.displayText = false;
		textHexList.remove(h);
	}
	
	public void removeHexText(Hex h) {
		h.displayText = false;
		h.hexText = "";
		h.textColor = Color.WHITE;
		textHexList.remove(h);
	}
	
	public void displayShadedList() {
		gui.GUI_NB.GCO("There are " + shadedHexList.size() + " hexes shaded.");
		for (int i = 0; i < shadedHexList.size(); i++) {
			gui.GUI_NB.GCO("Hex " + i + " at " + shadedHexList.elementAt(i).x + ", " + shadedHexList.elementAt(i).y);
		}
	}
	
	public void unshadeAll() {
		for (int i = shadedHexList.size() - 1; i >= 0; i--) {
			unshadeHex(shadedHexList.elementAt(i));
		}
		bbms.GlobalFuncs.gui.repaint();
	}
	
	public void hideTextAll() {
		for (int i = textHexList.size() - 1; i >= 0; i--) {
			hideHexText(textHexList.elementAt(i));
		}
		bbms.GlobalFuncs.gui.repaint();
	}
	
	public void clearTextAll() {
		for (int i = textHexList.size() - 1; i >= 0; i--) {
			removeHexText(textHexList.elementAt(i));
		}
		bbms.GlobalFuncs.gui.repaint();
	}
	
	public TerrainEnum chooseTerrain(int r) {
		randPool = r;
		if (TerrainEval(randPool, HexMap.chanceClear)) return TerrainEnum.CLEAR;
		if (TerrainEval(randPool, HexMap.chanceHighGrass)) return TerrainEnum.T_GRASS;
		if (TerrainEval(randPool, HexMap.chanceTrees)) return TerrainEnum.TREES;
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
				TerrainEnum tType = chooseTerrain(GlobalFuncs.randRange(1, HexMap.totalWeight));
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
	
	public void storeHex(int x, int y, Hex in) {
		hexArray[x][y] = in;
	}
	
	public Hex getHex(HexOff h) {
		return getHex(h.x, h.y);
	}
	
	public int getXDim() {
		return xDim;
	}
	
	public int getYDim() {
		return yDim;
	}

	public static boolean checkMapBounds(int x, int y) {
		if (!GlobalFuncs.mapInitialized) return false;
		
		if (x < 0 || y < 0) return false;
		
		if (x > GlobalFuncs.scenMap.getXDim() || y > GlobalFuncs.scenMap.getYDim()) return false;
		
		return true;
	}
	
	public void saveMap(Path p) {		
		// Stores map characteristics
		FIO.overwriteFile(p, "# Map and environment characteristics: x size, y size, map view x, map view y, clock time");
		FIO.appendFile(p, xDim + ", " + yDim + ", " + 
						GlobalFuncs.gui.GMD.mapDisplayX + ", " + GlobalFuncs.gui.GMD.mapDisplayY + ", " + 
						Clock.time + "\n");

		// Stores hex information 
		FIO.appendFile(p, "# Hex data, stored rows");
		FIO.appendFile(p, "# Format is: TerrainEnumID, elevation, obstacle height, density, obscuration, vapor, deltaVapor, vaporType");
		
		for (int y = 0; y < yDim; y++) {
			FIO.appendFile(p, "\n# Row " + y);
			for (int x = 0; x < xDim; x++) {
				FIO.appendFile(p, hexArray[x][y].saveHex());
			}
		}
		
	}
	
	/** Sets all vapor source hexes to maximum and drains all source sink hexes.
	 * 
	 */
	public void UpdateSourceSink() {
		for (int i = 0; i < vaporSourceList.size(); i++) {
			Hex finger = vaporSourceList.elementAt(i);
			finger.vapor = 25500;
		}
		
		for (int i = 0; i < vaporSinkList.size(); i++) {
			Hex finger = vaporSinkList.elementAt(i);
			finger.vapor = 0;
		}
	}

}
