package terrain;

import unit.MoveClass;
import bbms.GlobalFuncs;

public class TreesTerrain extends TerrainType{
	
	@Override
	public int getMoveCost(MoveClass mClass) {
		
		switch (mClass) {
		
		case FOOT: return 3;
		case WHEEL: return 9;
		case AT_WHEEL: return 8;
		case TRACK: return 6;
		
		case BOAT: return -1;
		default: return -2;
		
		}		
	}

	@Override
	public String displayType() {
		return "Trees";
	}

	@Override
	public char displayChar() {
		return 'f';
	}

	@Override
	public int generateDensity() {
		return GlobalFuncs.randRange(25, 36);
	}

	@Override
	public int generateObsHeight() {
		return GlobalFuncs.randRange(8, 29);
	}

	@Override
	public int generateHeight() {
		return 0;
	}
	
	@Override
	public TerrainEnum getTerrainEnum() {
		return TerrainEnum.TREES;
	}

}
