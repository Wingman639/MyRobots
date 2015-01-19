package gavin;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.HitRobotEvent;
import gavin.Target;

import java.awt.Color;
import java.awt.geom.Point2D;

public class GavinJuniorBot extends AdvancedRobot {

	int firepower = 1;
	Target target = new Target();
	int moveLength = 200;


	public void run() {
    	setAdjustGunForRobotTurn( true ); 
        setAdjustRadarForGunTurn( true ); 
        this.setColors(Color.blue, Color.black, Color.white, Color.white, Color.cyan);
        
        while (true) {
        	if ( timePassed(target.timeStamp) > 5 ) {
        		setTurnRadarRightRadians(Math.PI * 2); 
        	}
        	keepMoving();
        	execute();
        }
	}


	public void onScannedRobot(ScannedRobotEvent e) {
    	target.update(e, this);
    	lockRadarOnTarget();
    	fireToNextTargetPosition( guessPoint() );
	}


	public void onHitWall() {
		stop();
		back(50);

	}	
	
	public void onHitRobot(HitRobotEvent e) {
		stop();
        setTurnRightRadians(-Math.PI/2);
        setBack(20);
	}
	
	public double toAngle(double x, double y, double fieldWidth, double fieldHeight){
		double angle = 0;
		if (x > fieldWidth/2 && y > fieldHeight/2) {
            angle = 225;
        } else if (x > fieldWidth/2 && y < fieldHeight/2) {
            angle = 315;
        } else if (x < fieldWidth/2 && y < fieldHeight/2) {
            angle = 45;
        } else if (x < fieldWidth/2 && y > fieldHeight/2) {
            angle = 135;
        } 
        return Math.toRadians(angle);
	}
	
	public double getRunAngle(){
		double toAngleRadians = toAngle(getX(), getY(), getBattleFieldWidth(), getBattleFieldHeight());
		double runAngle = toAngleRadians - getHeadingRadians();
		return minAngleRadians( runAngle );
	}

	
	public void keepMoving() {
		double runAngle = getRunAngle();
		turnRightRadians( runAngle / 2 );
		setTurnRightRadians( runAngle / 2 );
		setAhead(moveLength);
	}
	
    public long timePassed( long oldTime ) {
    	return getTime() - oldTime;
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
