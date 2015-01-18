package gavin;
import java.awt.*;
import robocode.*; 

public class Observer extends AdvancedRobot {
	Enemy enemy = new Enemy();
    public static double PI = Math.PI;
         
    public void run() 
    { 
       setAdjustGunForRobotTurn( true ); 
       setAdjustRadarForGunTurn( true ); 
       this.setColors(Color.red, Color.blue, Color.yellow, Color.red, Color.green);
    
            while(true){
                    if(enemy.name == null){
                            setTurnRadarRightRadians(2*PI); 
                            execute(); 
                    }
                    else{
                            execute();
                    }
            }
	}
	         
	public void onScannedRobot(ScannedRobotEvent e) 
	{ 
	    enemy.update(e, this); 
	    double Offset = miniOffset( enemy.direction - getRadarHeadingRadians() ); 
	    setTurnRadarRightRadians( Offset * 1.5); 
	    turnGunToRadar();
	    fire(1);
	}
	

	
	public double miniOffset ( double angle ) 
	{ 
	    if ( angle < -Math.PI ) 
	        angle += 2*Math.PI; 
	    if ( angle > Math.PI ) 
	        angle -= 2*Math.PI; 
	    return angle; 
	}
	public class Enemy {
	    public double x,y;
	    public String name = null;
	    public double headingRadian = 0.0D; 
	    public double bearingRadian = 0.0D; 
	    public double distance = 1000D; 
	    public double direction = 0.0D; 
	    public double velocity = 0.0D; 
	    public double prevHeadingRadian = 0.0D; 
	    public double energy = 100.0D; 
	    
	    
	    public void update(ScannedRobotEvent e,AdvancedRobot me){
	            name = e.getName();
	            headingRadian = e.getHeadingRadians();
	            bearingRadian = e.getBearingRadians();
	            energy = e.getEnergy();
	            velocity = e.getVelocity();
	            distance = e.getDistance();
	            direction = bearingRadian + me.getHeadingRadians(); 
	            x = me.getX() + Math.sin( direction ) * distance; 
	            y= me.getY() + Math.cos( direction ) * distance; 
	    }
	}
	
	public void turnGunToRadar() {
		double offset = miniOffset(getRadarHeadingRadians() - getGunHeadingRadians());
		setTurnGunRightRadians(offset);
	}
	
	/**This function predicts the time of the intersection between the 
	bullet and the target based on a simple iteration.  It then moves 
	the gun to the correct angle to fire on the target.**/
	void doGun() {
	    long time;
	    long nextTime;
	    Point2D.Double p;
	    p = new Point2D.Double(target.x, target.y);
	    for (int i = 0; i < 10; i++){
	        nextTime = 
	    (intMath.round((getRange(getX(),getY(),p.x,p.y)/(20-(3*firePower))));
	        time = getTime() + nextTime;
	        p = target.guessPosition(time);
	    }
	    /**Turn the gun to the correct angle**/
	    double gunOffset = getGunHeadingRadians() - 
	                  (Math.PI/2 - Math.atan2(p.y - getY(), p.x - getX()));
	    setTurnGunLeftRadians(normaliseBearing(gunOffset));
	}
	double normaliseBearing(double ang) {
	    if (ang > Math.PI)
	        ang -= 2*PI;
	    if (ang < -Math.PI)
	        ang += 2*Math.PI;
	    return ang;
	}
	public double getrange(double x1,double y1, double x2,double y2) {
	    double x = x2-x1;
	    double y = y2-y1;
	    double h = Math.sqrt( x*x + y*y );
	    return h;	
	}
}
