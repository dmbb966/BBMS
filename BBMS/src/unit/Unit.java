package unit;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bbms.GUI_NB;
import bbms.GlobalFuncs;

public class Unit {

	public hex.Hex location;
	public hex.HexOff HullOffset = new hex.HexOff(0, 0);
	public hex.HexOff TurretOffset = new hex.HexOff(0, 0);
	public hex.HexOff TurretRing = new hex.HexOff(0, 0);
	
	public double hullOrientation;
	public double turretOrientation;	// Relative to the hull
	
	// Used for determining hull and turret graphic files
	public String type;
	public String callsign;
	
	// Unique number
	public int unitID;
	
	// What side its on
	public SideEnum side;
	
	public Unit (hex.Hex locn, SideEnum s, String givenType, String givenCallsign) {
		location = locn;
		side = s;
		type = givenType;
		
		if (s == SideEnum.ENEMY) {
			hullOrientation = 270.0;
			turretOrientation = 0.0;
		} else if (s == SideEnum.FRIENDLY){
			hullOrientation = 90.0;
			turretOrientation = 0.0;
		} else {
			hullOrientation = 0.0;
			turretOrientation = 0.0;
		}
		
		
		if (type == "M1A2") {
			HullOffset = new hex.HexOff(27, 12);
			TurretRing = new hex.HexOff(30 - HullOffset.getX(), 13 - HullOffset.getY());
			TurretOffset = new hex.HexOff(25, 11);
		} else if (type == "T-72") {
			HullOffset = new hex.HexOff(25, 12);
			TurretRing = new hex.HexOff(28 - HullOffset.getX(), 11 - HullOffset.getY());
			TurretOffset = new hex.HexOff(13, 10);
		}
		
		callsign = givenCallsign;
		
		unitID = GlobalFuncs.getNewUnitCount();
		
		//GlobalFuncs.unitList.add(this);
		GlobalFuncs.unitList.addElement(this);
	}
	
	public String DispUnitInfo() {
		return "Unit " + unitID + " is type: " + type + ", callsign " + callsign + " at location " + 
					location.x + ", " + location.y + " on side " + side + "\n with turret and hull orientation " + 
				turretOrientation + " and " + hullOrientation;				
		
	}
	
	public void DrawUnit(int xi, int yi, Graphics g) {				
		// Loads the appropriate hex icon
		String hullPath = "src/unit/graphics/" + type + "_H.png";
		String turretPath = "src/unit/graphics/" + type + "_T.png";
		
		// GUI_NB.GCO("Hull: " + hullPath + " || Turret: " + turretPath);
		
		File hullFile = new File(hullPath);
		File turretFile = new File(turretPath);
		try {
			Image img = ImageIO.read(hullFile);
			/* Graphics2D g2d = (Graphics2D)g;
			AffineTransform backup = g2d.getTransform();
			AffineTransform trans = new AffineTransform();
			trans.rotate(0.1, 5, 5);
			g2d.transform(trans);
			g2d.drawImage(img,  xi - HullOffset.getX(),  yi - HullOffset.getY(), null);
			g2d.setTransform(backup);
			
			
			// g.drawImage(img,  xi - HullOffset.getX(),  yi - HullOffset.getY(), null);
			
			/* img = ImageIO.read(turretFile);
			g.drawImage(img,  xi - TurretOffset.getX() + TurretRing.getX(),  
					yi - TurretOffset.getY() + TurretRing.getY(),  null);
			*/
			
		} catch (IOException ie) {
			System.out.println(ie.getMessage());
			GUI_NB.GCO(ie.getMessage());
		}				
	}
}
