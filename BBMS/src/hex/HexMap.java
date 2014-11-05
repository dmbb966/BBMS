package hex;

import terrain.TerrainEnum;
import bbms.GlobalFuncs;

public class HexMap {
	
	int xDim, yDim;
	Hex[][] hexArray;
	
	public HexMap(int x, int y) {
		xDim = x;
		yDim = y;
		hexArray = new Hex[xDim][yDim];
		GenerateMap();
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
