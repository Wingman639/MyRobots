package gavin;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.HitRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class GavinJuniorBot extends AdvancedRobot {

	/**
	 * run: TestFirstJuniorRobot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here

		// Some color codes: blue, yellow, black, white, red, pink, brown, grey, orange...
		// Sets these colors (robot parts): body, gun, radar, bullet, scan_arc
		

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			
			double runAngle = getRunAngle();
			
			turnRight( runAngle / 2 );
			setTurnRight( runAngle / 2 );
			setAhead(200);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		/*
		boolean goodToFire = false;
		double power = 1;
		if (e.getDistance() < 300) { power = 3; }
        if (e.getDistance() < 150) { goodToFire = true;}
        if (e.getVelocity() < 5){ goodToFire = true;}
        else if (getDirectionDiff(e) < 0.03) { goodToFire = true; }
        
        double angle = minTurnAngle( getGunAngleToTurn(e.getBearing()));
        //double angle2 = goodToFire ? guessAdjustAngle(e) : 0;
        turnGunLeft( angle );
        
        if (getGunHeat() < 0.01 && goodToFire){
        	fire(power);
        }
        setTurnGunRight(-5);
        */
		double gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);
		scan();
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet() {
		// Replace the next line with any behavior you would like
		//back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall() {
		// Replace the next line with any behavior you would like
		out.println("hit wall, heading " + getHeading());
		stop();
		back(50);
		/*
		back(100);
		turnRight( getBackAngle() );
		ahead(100);
		*/
	}	
	
	public void onHitRobot(HitRobotEvent e) {
		stop();
        if (isEnemy(e)) {
            double angle = minTurnAngle(getGunAngleToTurn(e.getBearing()));
            turnGunLeft( angle );
            fire(5);
        } 
        setTurnRight(-90);
        setBack(20);
	}
	
	public double minTurnAngle(double angle){
		double minAngle = angle % 360;
		if (minAngle > 180){
			minAngle = minAngle - 360;
		}
		else if( minAngle < -180){
			minAngle = minAngle + 360;
		}
		return minAngle;
	}
	
	public double getGunAngleToTurn(double seeAngle){
		return (seeAngle + getHeading() - getGunHeading()) % 360;
	}
	
	public double getDirectionDiff(ScannedRobotEvent e){
		double angleDelta = e.getHeading() - e.getBearing();
		return Math.sin( angleDelta * 0.017 * (2 * 3.14/360));
	}
	
	public double guessAdjustAngle(ScannedRobotEvent e){
		double speed = e.getVelocity() * getDirectionDiff(e);
		double angle = speed * 40;
		return angle;
	}
	
	public double toAngle(double x, double y, double fieldWidth, double fieldHeight){
		double angle = 0;
		if (x > fieldWidth/2 && y > fieldHeight/2) {
            angle = 135;
        } else if (x > fieldWidth/2 && y < fieldHeight/2) {
            angle = 225;
        } else if (x < fieldWidth/2 && y < fieldHeight/2) {
            angle = 315;
        } else if (x < fieldWidth/2 && y > fieldHeight/2) {
            angle = 45;
        } 
        return angle;
	}
	
	public double getRunAngle(){
		double runAngle = getHeading() - toAngle(getX(), getY(), getBattleFieldWidth(), getBattleFieldHeight());
		return minTurnAngle(runAngle);
	}
	
	public double getBackAngle(){
		double runAngle = getHeading() - toAngle(getX(), getY(), getBattleFieldWidth(), getBattleFieldHeight());
		return runAngle;
	}
	
	public boolean isEnemy(HitRobotEvent e) {
		return true;
	}
}
