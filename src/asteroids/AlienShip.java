package asteroids;

import java.awt.Shape;
import java.awt.geom.Path2D;

public class AlienShip extends Participant
{
    private Shape outline;
    
    public AlienShip ()
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(0, 0);
        poly.lineTo(25, 0);
        poly.lineTo(28, 8);
        poly.lineTo(-3, 8);
        poly.closePath();
        
        poly.moveTo(-3, 8);
        poly.lineTo(28, 8);
        poly.lineTo(38,13);
        poly.lineTo(8, 13);
        poly.closePath();
        
        poly.moveTo(8, 13);
        poly.lineTo(38, 13);
        poly.lineTo(28, 18);
        poly.lineTo(-3, 18);
        poly.closePath();
        
        outline = poly;
    }

    @Override
    Shape getOutline ()
    {
        return outline;
    }
    
    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        super.move();
    }

}
