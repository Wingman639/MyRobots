package gavin;

import java.awt.Color;
import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;


public class GavinBot extends AdvancedRobot
{
	int firepower = 1;
	Target target = new Target();
	
	public class Target {
		public long timeStamp = 0;
		public double bearing = 0;
		public double velocity = 0;
		public double distance = 0;
		public double heading = 0;
		public double direction = 0;
		public double x = 0, y = 0;
		public Point2D.Double p = new Point2D.Double(0, 0);
		
		public void update( ScannedRobotEvent e, AdvancedRobot me) {
			timeStamp = me.getTime();
			bearing = e.getBearingRadians();
			velocity = e.getVelocity();
			distance = e.getDistance();
			heading = e.getHeadingRadians();
			direction = bearing + me.getHeadingRadians();
			x = me.getX() + Math.sin( direction ) * distance; 
            y= me.getY() + Math.cos( direction ) * distance; 
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
    	//fireToNextTargetPosition(new Point2D.Double(target.x, target.y));
    	fireToNextTargetPosition( guessPoint() );
    }
    
    public void lockRadarOnTarget() {
    	double angleDiff = minAngleRadians(target.direction - getRadarHeadingRadians());
    	setTurnRadarRightRadians( angleDiff * 1.5 );
    }
    
    public void fireToNextTargetPosition( Point2D.Double p) {
    	double offset = getGunHeadingRadians() - (Math.PI/2 - Math.atan2(p.y -getY(), p.x - getX()));
    	setTurnGunLeftRadians( minAngleRadians(offset) );
    	setFire(firepower);
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
    
    public double bulletFlyDurationTime( double distance ) {
    	return distance / (20 -(3 * firepower));
    }
    
    public double targetMovingLength() {
    	return target.velocity * bulletFlyDurationTime( target.distance );
    }
    
    public Point2D.Double guessPoint() {
    	double xDiff, yDiff;
    	double lengthDiff = targetMovingLength();
    	xDiff = Math.sin(target.heading) * lengthDiff;
    	yDiff = Math.cos(target.heading) * lengthDiff;
    	return new Point2D.Double(target.x + xDiff, target.y + yDiff);
    }
}
