package gavin;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class Target {
	final double PI = Math.PI;
	public long timeStamp = 0;
	public double bearing = 0;
	public double velocity = 0;
	public double distance = 0;
	public double heading = 0;
	public double headingChange = 0;
	public double direction = 0;
	public double x = 0, y = 0;
	
	public void update( ScannedRobotEvent e, AdvancedRobot me) {
		headingChange = getHeadingChange(e, me);
		timeStamp = me.getTime();
		bearing = e.getBearingRadians();
		velocity = e.getVelocity();
		distance = e.getDistance();
		heading = e.getHeadingRadians();
		direction = bearing + me.getHeadingRadians();
		double absbearing_rad = (me.getHeadingRadians()+e.getBearingRadians())%(2*PI);
		x = me.getX() + Math.sin( absbearing_rad ) * distance; 
        y= me.getY() + Math.cos( absbearing_rad ) * distance; 
	}
	
	
	
	public Point2D.Double guessPosition(double timeDiff) {
		if (headingChange > 0.00001) {
			return circleMovingPosition(timeDiff);
		}
		return straightMovingPosition(timeDiff);
	}
	
	public Point2D.Double straightMovingPosition(double timeDiff) {
		double newX = x + Math.sin(heading) * velocity * timeDiff;
		double newY = y + Math.cos(heading) * velocity * timeDiff;
		return new Point2D.Double(newX, newY);
	}
	
	public Point2D.Double circleMovingPosition(double timeDiff) {
		double r = velocity / headingChange;
		double headingDiff = headingChange * timeDiff;
		double newX = x + (r * Math.cos(heading)) - (r * Math.cos(heading + headingDiff));
		double newY = y + (r * Math.sin(heading + headingDiff) - (r * Math.sin(heading)));
		return new Point2D.Double(newX, newY);
	}
	
	public double normaliseBearing(double ang) {
		if (ang > PI)
			ang -= 2*PI;
		if (ang < -PI)
			ang += 2*PI;
		return ang;
	}
	
	public double getHeadingChange( ScannedRobotEvent e, AdvancedRobot me ) {
		double h = normaliseBearing(e.getHeadingRadians() - heading);
		h = h/(me.getTime() - timeStamp);
		return h;
	}
}
