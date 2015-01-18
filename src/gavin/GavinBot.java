package gavin;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class GavinBot extends AdvancedRobot
{
	Target target = new Target();
	
	public class Target {
		public long timeStamp = 0;
		public double bearing = 0;
		public double absoluteDirectionRadains = 0;
		
		public void update( ScannedRobotEvent e, AdvancedRobot me) {
			bearing = e.getBearingRadians();
			absoluteDirectionRadains = bearing + me.getHeadingRadians();
			timeStamp = me.getTime();
		}
	}

    public void run() 
    {
    	setAdjustGunForRobotTurn( true ); 
        setAdjustRadarForGunTurn( true ); 
        this.setColors(Color.blue, Color.black, Color.white, Color.blue, Color.cyan);
        
        while (true) {
        	if ( timePassed(target.timeStamp) > 5 ) {
        		setTurnRadarRight(360); 
        	}
        	execute();
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
	    if ( angle < -Math.PI ) {
	    	angle += 2*Math.PI; 
	    }	    
	    else if ( angle > Math.PI ) 
	        angle -= 2*Math.PI; 
	    return angle; 
    }
    
    public long timePassed( long oldTime ) {
    	return getTime() - oldTime;
    }
}
