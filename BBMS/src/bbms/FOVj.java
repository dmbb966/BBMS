package bbms;

import hex.Hex;
import hex.HexAx;
import hex.HexCube;
import hex.HexOff;
import terrain.TerrainEnum;

import javax.swing.UIManager;


public class FOVj {
	
	public static void main(String[] args) {		
			        
        JWindowLook();				// Creates a "Windows" look and feel to the windows                             
		System.out.println("Welcome to the Bare Bones Military Simulator (BBMS)\n");
			
		//test1();
		//test2();
		//test3();
		//test4();
		//test5();
		//test6();
		test7();
	}
	
    public static void JWindowLook() {
        // Sets the windows to have a "Windows" look and feel to them.
        try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.err.println("ERROR: Cannot load Windows-like displays.");
            e.printStackTrace();
        }
    }
	
	/**
	 * test1() runs a series of tests, generating one hex object of each type (offset, cubic, axial) and displaying the coordinates.
	 */
	public static void test1() {
		HexOff testO = new HexOff(3, 14);
		HexCube testC = new HexCube(1, 2, 3);
		HexAx testA = new HexAx(9, 99);
		
		testO.DisplayHex();
		testO.DisplayType();
		
		System.out.println("\n");
		
		testC.DisplayHex();
		testC.DisplayType();
		
		System.out.println("\n");
		
		testA.DisplayHex();
		testA.DisplayType();
		
		System.out.println("\n\nTest complete.\n\n");		
	}
	
	/**
	 * test2() runs conversion tests between the different hex types (offset, cubic, axial)
	 */
	public static void test2() {
		HexAx initial = new HexAx(-1, 3);		
		
		initial.DisplayType();
		initial.DisplayHex();
		
		HexCube next = initial.ConvertToCube();
		
		System.out.println("");
		
		next.DisplayType();
		next.DisplayHex();
		
		HexOff dann = next.ConvertToOff();
		
		System.out.println("");
		
		dann.DisplayType();
		dann.DisplayHex();
		
		initial = dann.ConvertToAx();
		
		System.out.println("");
		initial.DisplayType();
		initial.DisplayHex();
	}
	
	/**
	 * test3() runs distance calculations using each of the different hex types (offset, cubic, axial)
	 */
	public static void test3() {
		HexOff origin = new HexOff(65, 58);
		HexOff target = new HexOff(67, 59);
		
		origin.DisplayType();
		origin.DisplayHex();
		target.DisplayHex();
		System.out.println("Distance is " + origin.DistFrom(target) + " at azimuth " + origin.AzimuthTo(target));
		
		HexAx originAx = origin.ConvertToAx();
		HexAx targetAx = target.ConvertToAx();
		
		originAx.DisplayType();
		originAx.DisplayHex();
		targetAx.DisplayHex();
		System.out.println("Distance is " + originAx.DistFrom(targetAx) + " at azimuth " + originAx.AzimuthTo(targetAx));
		
		HexCube originCube = origin.ConvertToCube();
		HexCube targetCube = target.ConvertToCube();
		
		originCube.DisplayType();
		originCube.DisplayHex();
		targetCube.DisplayHex();
		System.out.println("Distance is " + originCube.DistFrom(targetCube) + " at azimuth " + originCube.AzimuthTo(targetCube));
	}
	
	/**
	 * test4() tests the hexes found along a line between two points
	 */
	public static void test4() {
		HexOff origin = new HexOff(17, 18);
		HexOff target = new HexOff(43, 45);
		
		System.out.print("Distance: " + origin.DistFrom(target) + " at azimuth: " + origin.AzimuthTo(target) + "\n");
		HexCube.HexesBetween(origin.ConvertToCube(), target.ConvertToCube());
	}
	
	/**
	 * test5() tests casting from an origin hex
	 */
	public static void test5() {
		HexOff origin = new HexOff(17, 18);		
		double azimuth = 45;
		int range = 9;
		
		HexOff.HexCast(origin, azimuth, range);
	}
	
	/**
	 * test6() tests finding adjacent hexes
	 */
	public static void test6() {
		HexOff origin = new HexOff(2, 2);
		origin.DisplayHex();
		for (int i = 0; i < 6; i++)
		{
			System.out.print ("Neighbor in direction " + i + " is ");
			origin.findNeighbor(i).DisplayHex();
		}
	}
	
	/**
	 * test7() tests map generation
	 */
	public static void test7() {
		int xDim = 4;
		int yDim = 4;
		
		for (int y = 0; y < yDim; y++) {
			for (int x = 0; x < xDim; x++) {
				TerrainEnum tType = TerrainEnum.T_GRASS;
				Hex tHex = new Hex(x, y, tType, 0);
				tHex.DisplayInfo();
			}
		}
	}

}
