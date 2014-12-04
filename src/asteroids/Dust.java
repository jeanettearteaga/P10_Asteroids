package asteroids;

import static asteroids.Constants.BULLET_SPEED;

import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 * Represents the particles left from an asteroid crashing with 
 * a ship or a bullet.
 * @author Jeanette Arteaga
 * @author Edgar Estrada
 */
public class Dust extends Participant
{
    // The outline of the particles (dust)
    private Shape outline;
    
    public Dust(double x, double y){
        setPosition(x, y);
        
        // Constructs a tiny particle
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(.5, .5);
        poly.lineTo(.5, -.5);
        poly.lineTo(-.5, -.5);
        poly.lineTo(-.5, .5);
        poly.closePath();
        outline = poly;
    }

    /**
     * Returns the outline of the particles
     */
    @Override
    Shape getOutline ()
    {
        return outline;
    }

}
