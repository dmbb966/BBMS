package terrain;

import unit.MoveClass;

public class InvalidTerrain extends TerrainType{
	
	@Override
	public int getMoveCost(MoveClass mClass) {
		
		switch (mClass) {
		
		case FOOT: return 2;
		case WHEEL: return 3;
		case AT_WHEEL: return 2;
		case TRACK: return 2;
		
		case BOAT: return -1;
		default: return -2;
		
		}		
	}

	@Override
	public String displayType() {
		return "";
	}

	@Override
	public char displayChar() {
		return 'x';
	}

	@Override
	public int generateDensity() {
		return 0;
	}

	@Override
	public int generateObsHeight() {
		return 0;
	}

	@Override
	public int generateHeight() {
		return 0;
	}

	@Override
	public TerrainEnum getTerrainEnum() {
		return TerrainEnum.INVALID;
	}
	
	

}
