package gavin;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class Target {
	public long timeStamp = 0;
	public double bearing = 0;
	public double velocity = 0;
	public double distance = 0;
	public double heading = 0;
	public double direction = 0;
	public double x = 0, y = 0;
	
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
