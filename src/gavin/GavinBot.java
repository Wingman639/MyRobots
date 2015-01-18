package gavin;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;


public class GavinBot extends AdvancedRobot
{
	long interval = 5;
	Target target = new Target();
	
	public class Target {
		public int saveCount = 15;
		public long timeStamp = 0;
		public double bearing = 0;
		public double[] bearingRing = new double[saveCount]; 
		public double absoluteDirectionRadains = 0;
		public double offsetRadains = 0;
		
		public void update( ScannedRobotEvent e, AdvancedRobot me) {
			timeStamp = me.getTime();
			bearing = e.getBearingRadians();
			absoluteDirectionRadains = bearing + me.getHeadingRadians();
			calcuateNextBearing( me );
		}
		
		public void calcuateNextBearing( AdvancedRobot me ) {
			int index = (int) (timeStamp % saveCount);
			bearingRing[index] = bearing;
			int prePositionIndex = (index + 1) % saveCount;
			offsetRadains = bearing - bearingRing[prePositionIndex];
		}
	}

    public void run() 
    {
    	setAdjustGunForRobotTurn( true ); 
        setAdjustRadarForGunTurn( true ); 
        this.setColors(Color.blue, Color.black, Color.white, Color.white, Color.cyan);
        
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
    	fireToNextTargetPosition();
    }
    
    public void lockRadarOnTarget() {
    	double angleDiff = minAngleRadians(target.absoluteDirectionRadains - getRadarHeadingRadians());
    	setTurnRadarRightRadians( angleDiff * 1.5 );
    }
    
    public void fireToNextTargetPosition() {
    	double angleDiff = minAngleRadians(target.absoluteDirectionRadains + target.offsetRadains - getGunHeadingRadians());
    	setTurnGunRightRadians( angleDiff );
    	setFire(1);
    	out.printf("(%f, %f)", getRadarHeadingRadians(), getGunHeadingRadians());
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
