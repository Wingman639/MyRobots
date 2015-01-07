package gavin;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class GavinBot extends AdvancedRobot
{
    static boolean RIGHT = true;
    static boolean LEFT = false;
    public void run() 
    {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForRobotTurn(true);
        setAdjustRadarForGunTurn( true );
        setTurnRadarRight( 400 ); 
        execute();
    } 

    public void onScannedRobot( ScannedRobotEvent e ) 
    {
    	if (e.isSentryRobot()) return;
        double heading = e.getBearing() +getHeading();
		double distance = e.getDistance(); 
		double ager_bearing = Math.toRadians(heading % 360); 
		double genyX = getX() + Math.sin(ager_bearing) * distance;
		double genyY = getY() + Math.cos(ager_bearing) * distance;
		out.println("genyX:"+ Math.round(genyX));
		out.println("genyY:"+ Math.round(genyY));
	
        if( heading >= 360 )
			 heading = heading - 360;
        if( heading < 0 )
			 heading = heading +360; 
        double bearing = getRadarHeading() - heading;
        double radar_degree;
        boolean radar_direction; 
        if( 0 <= bearing && bearing <= 180 )
        {
            radar_direction = LEFT;
        }
        else if( bearing <= -180 )
        {
            radar_direction = LEFT;
            bearing = ( 360 + bearing );
        }
        else if( bearing < 0 )
        {
            radar_direction = RIGHT;
            bearing =( -bearing );
        }
        else 
        {
            radar_direction = RIGHT;
            bearing = (360 - bearing);
        } 
        radar_degree = bearing * 1.3 ; 
				
        if( radar_direction == RIGHT )
        {
            setTurnRadarRight( radar_degree );
			execute();
        }
        else
        {
            setTurnRadarLeft( radar_degree );
			execute();
        } 
        
        double gunTurnAmt = normalRelativeAngleDegrees(getGunHeading() - (getHeading() - e.getBearing()) );
		setTurnGunRight(gunTurnAmt);
    }
}
