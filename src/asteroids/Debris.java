package asteroids;

import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 * Represents the parts of the ship breaking apart when 
 * colliding with an asteroid
 * @author Jeanette Arteaga
 * @author Edgar Estrada
 *
 */
class Debris extends Participant
{   
    // Gets the line
    private Shape outline;

    /**
     * Cretes lines the lines that represent the parts of the ship
     * @param x1
     * @param y1
     */
    public Debris(double x1, double y1){
        setPosition(x1, y1);
   
        // Constructs a line
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(10, 0);
        poly.lineTo(-10, 0);
        poly.closePath();
        outline = poly;   
    }

    /**
     * Returns the outline of the Debris
     */
    @Override
    Shape getOutline ()
    {
        // TODO Auto-generated method stub
        return outline;
    }

}
