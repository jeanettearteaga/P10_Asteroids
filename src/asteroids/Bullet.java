package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import static asteroids.Constants.*;


/**
 * Represents fired bullets
 *
 */
public class Bullet extends Participant//extends Constants
{       
    private Shape outline;
    
    public Bullet(double x, double y, double direction){
        setPosition(x, y);
        setVelocity(BULLET_SPEED, direction);

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(1, 1);
        poly.lineTo(1, -1);
        poly.lineTo(-1, -1);
        poly.lineTo(-1, 1);
        poly.closePath();
        outline = poly;
    }
     
    @Override
    Shape getOutline ()
    {
        return outline;
    }

}
