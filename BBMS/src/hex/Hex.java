package hex;

import terrain.*;


public class Hex {
	
	HexOff loc;			// The current location (odd-r offset hex)
	TerrainType tType;	// Type of terrain
	int elevation;		// Relative height of the ground (m)
	int obsHeight;		// Additional height from buildings/obstacles
	int density;		// Obstructs line of sight if cumulative density >30
	
	int obscuration;	// Level of visual obscuration on the hex (adds to density, but can dissipate over time)		
	
	// TODO: Include unit list of all units in this hex
	// TODO: Include a cover value which affects the deadliness of various types of weapons
	
	public Hex (int x, int y, TerrainEnum iTerrain, int iElev) {
		loc = new HexOff(x, y);		
		
		switch (iTerrain) {
		case CLEAR: tType = new ClearTerrain(); break;
		case TREES: tType = new TreesTerrain(); break;
		case T_GRASS: tType = new TallGrassTerrain(); break;
		}
		
		elevation = iElev + tType.generateHeight();		
		obsHeight = tType.generateObsHeight();
		density = tType.generateDensity();				
		
		obscuration = 0;		
	}
	
	public void DisplayInfo() {
		System.out.print ("Hex: (" + loc.x + ", " + loc.y + ") is terrain type: " + tType.displayType() + " (" + tType.displayChar() + ")\n");
		System.out.print ("Elevation: " + elevation + "   Obs Height: " + obsHeight + "    Density: " + density + "    Obscur: " + obscuration + "\n");		
	}
	
}
