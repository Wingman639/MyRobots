package gavin;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class GavinBot extends AdvancedRobot
{
	Target target = new Target();
	
	public class Target {
		public ScannedRobotEvent e = null;
		public double absoluteDirectionRadains = 0;
		
		public void update( ScannedRobotEvent inE, AdvancedRobot me) {
			e = inE;
			absoluteDirectionRadains = e.getBearingRadians() + me.getHeadingRadians();
		}
	}

    public void run() 
    {
    	setAdjustGunForRobotTurn( true ); 
        setAdjustRadarForGunTurn( true ); 
        this.setColors(Color.blue, Color.black, Color.white, Color.blue, Color.cyan);
        
        while (true) {
        	if (target.e == null) {
        		setTurnRadarRight(360);
        		execute();
        	}
        	else {
        		execute();
        	}
        }
    } 

    public void onScannedRobot( ScannedRobotEvent e ) 
    {
    	target.update(e, this);
    	lockRadarOnTarget();
    }
    
    public void lockRadarOnTarget() {
    	double angerDiff = minAngleRadians(target.absoluteDirectionRadains - this.getRadarHeadingRadians());
    	setTurnRadarRightRadians( angerDiff * 1.5 );
    }
    
    public double minAngleRadians( double angle ) {
    	if (angle > 2 * Math.PI) {
    		angle = angle % (2 * Math.PI);
    	}
    	if (angle > Math.PI ) {
    		return 2 * Math.PI - angle;
    	}
    	if (angle < -Math.PI) {
    		return 2 * Math.PI + angle;
    	}
    	return angle;
    }
}
