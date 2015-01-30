package gavin;

import robocode.AdvancedRobot;
import java.awt.geom.Point2D;
import robocode.ScannedRobotEvent;
import robocode.HitRobotEvent;
import java.awt.Color;
import gavin.Target;


public class GavinJuniorBot extends AdvancedRobot {
	final double PI = Math.PI;
	int firePower = 1;
	Target target = new Target();
	int moveLength = 200;


	public void run() {
    	initRobot();
        
        while (true) {
        	if ( isTargetLost() ) {
        		radarScanAround();
        	}
        	keepMoving();
        	execute();
        }
	}
	
	private void initRobot() {
		setAdjustGunForRobotTurn( true ); 
        setAdjustRadarForGunTurn( true ); 
        this.setColors(Color.gray, Color.blue, Color.yellow, Color.white, Color.cyan);
	}
	
	
	private boolean isTargetLost() {
		return timePassed(target.timeStamp) > 5;
	}
	
	
	private void radarScanAround() {
		setTurnRadarRightRadians(Math.PI * 2); 
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
	
	private double toAngle(double x, double y, double fieldWidth, double fieldHeight){
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
	
	private double getRunAngle(){
		double toAngleRadians = toAngle(getX(), getY(), getBattleFieldWidth(), getBattleFieldHeight());
		double runAngle = toAngleRadians - getHeadingRadians();
		return minAngleRadians( runAngle );
	}

	
	private void keepMoving() {
		double runAngle = getRunAngle();
		turnRightRadians( runAngle / 2 );
		setTurnRightRadians( runAngle / 2 );
		setAhead(moveLength);
	}
	
	private long timePassed( long oldTime ) {
    	return getTime() - oldTime;
    }

	private void lockRadarOnTarget() {
    	double angleDiff = minAngleRadians(target.direction - getRadarHeadingRadians());
    	setTurnRadarRightRadians( angleDiff * 1.5 );
    }
    
    private void fireToNextTargetPosition( Point2D.Double p) {
    	double offset = getGunHeadingRadians() - (Math.PI/2 - Math.atan2(p.y -getY(), p.x - getX()));
    	setTurnGunLeftRadians( minAngleRadians(offset) );
    	setFire(firePower);
    }
    
    private double minAngleRadians( double angle ) {
		if (angle > PI)
			angle -= 2*PI;
		if (angle < -PI)
			angle += 2*PI;
		return angle;
    }
    
    private double bulletFlyDurationTime( double distance ) {
    	return distance / (20 -(3 * firePower));
    }
    /*
    private double targetMovingLength() {
    	return target.velocity * bulletFlyDurationTime( target.distance );
    }
    
    
    private Point2D.Double guessPoint() {
    	double xDiff, yDiff;
    	double lengthDiff = targetMovingLength();
    	xDiff = Math.sin(target.heading) * lengthDiff;
    	yDiff = Math.cos(target.heading) * lengthDiff;
    	return new Point2D.Double(target.x + xDiff, target.y + yDiff);
    }*/
    
    private Point2D.Double guessPoint() {
    	double nextTime;
    	Point2D.Double p;
		p = new Point2D.Double(target.x, target.y);
		for (int i = 0; i < 10; i++){
       		nextTime = bulletFlyDurationTime( distance(getX(), getY(), p.x, p.y) );
        	p = target.guessPosition(nextTime);
		}
		return p;
    }
    
    private double distance(double x1, double y1, double x2, double y2) {
    	double xDiff = x2 - x1;
    	double yDiff = y2 - y1;
    	return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
    
}
