package gavin;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class GavinBot extends AdvancedRobot
{
	Target target = new Target();
	public class Target {
		
	}

    public void run() 
    {
    	setAdjustGunForRobotTurn( true ); 
        setAdjustRadarForGunTurn( true ); 
        this.setColors(Color.blue, Color.black, Color.white, Color.blue, Color.cyan);
        
        
    } 

    public void onScannedRobot( ScannedRobotEvent e ) 
    {

    }
}
