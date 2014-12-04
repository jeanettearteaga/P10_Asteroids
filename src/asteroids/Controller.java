package asteroids;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

import static asteroids.Constants.*;

/**
 * Controls a game of asteroids
 * 
 * @author Joe Zachary
 */
public class Controller implements CollisionListener, ActionListener,
KeyListener, CountdownTimerListener
{
    // Shared random number generator
    private Random random;

    // The ship (if one is active) or null (otherwise)
    private Ship ship;

    // When this timer goes off, it is time to refresh the animation
    private Timer refreshTimer;

    // Count of how many transitions have been made. This is used to keep two
    // conflicting transitions from being made at almost the same time.
    private int transitionCount;
    
//\\***//\\***//\\
    // score of the game
    private int level;

    // Number of lives left
    private int lives;

    // score of the game
    private int score;

    // The Game and Screen objects being controlled
    private Game game;
    private Screen screen;

//\\***//\\***//\\
    // The Bullets on the screen of the game
    private ArrayList<Bullet> bullPressed= new ArrayList<Bullet>();

    // Booleans that represent weather the keys were pressed
    // If pressed then it is true if it is released or not 
    // pressed then the boolean is false
    private boolean left, right, up;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller (Game game, Screen screen)
    {
        // Record the game and screen objects
        this.game = game;
        this.screen = screen;

        // Initialize the random number generator
        random = new Random();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);
        transitionCount = 0;

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        refreshTimer.start();
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen and display the legend
        screen.clear();
        screen.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();

        // Make sure there's no ship
        ship = null;

    }

    /**
     * Get the number of transitions that have occurred.
     */
    public int getTransitionCount ()
    {
        return transitionCount;
    }

    /**
     * The game is over. Displays a message to that effect and enables the start
     * button to permit playing another game.
     */
    private void finalScreen ()
    {
        screen.setLegend(GAME_OVER);
        screen.removeCollisionListener(this);
        screen.removeKeyListener(this);
    }

    /**
     * Places four asteroids near the corners of the screen. Gives them random
     * velocities and rotations.
     */
    private void placeAsteroids ()
    {
        Participant a = new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET);
        a.setVelocity(3, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);

        a = new Asteroid(1, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET);
        a.setVelocity(3, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);

        a = new Asteroid(2, 2, EDGE_OFFSET, SIZE - EDGE_OFFSET);
        a.setVelocity(3, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);

        a = new Asteroid(3, 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET);
        a.setVelocity(3, random.nextDouble() * 2 * Math.PI);
        a.setRotation(2 * Math.PI * random.nextDouble());
        screen.addParticipant(a);
    }

    /**
     * Set things up and begin a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        screen.clear();

        // Place four asteroids
        placeAsteroids();

        // Place the ship
        placeShip();

        // Reset statistics
        score = 0;
        lives = 3;

        game.setScore(score);
        game.setLives(lives);


        // Start listening to events. In case we're already listening, take
        // care to avoid listening twice.
        screen.removeCollisionListener(this);
        screen.removeKeyListener(this);
        screen.addCollisionListener(this);
        screen.addKeyListener(this);

        // Give focus to the game screen
        screen.requestFocusInWindow();
    }

    /**
     * Place a ship in the center of the screen.
     */
    private void placeShip ()
    {
        if (ship == null)
        {
            ship = new Ship();
        }
        ship.setPosition(SIZE / 2, SIZE / 2);
        ship.setRotation(-Math.PI / 2 );
        screen.addParticipant(ship);
    }

    /**
     * Deal with collisions between participants.
     */
    @Override
    public void collidedWith (Participant p1, Participant p2)
    {
        if (p1 instanceof Asteroid && p2 instanceof Ship)
        {
            asteroidCollision((Asteroid) p1);
            shipCollision((Ship) p2);
        }
        else if (p1 instanceof Ship && p2 instanceof Asteroid)
        {
            asteroidCollision((Asteroid) p2);
            shipCollision((Ship) p1);
        }

        if (p1 instanceof Asteroid && p2 instanceof Bullet)
        {
            asteroidCollision((Asteroid) p1);
            bulletCollision((Bullet) p2);
            // The amount of time that the Bullet will stay on the screen
            new CountdownTimer (this,null,2000);
        }
        else if (p1 instanceof Bullet && p2 instanceof Asteroid)
        {
            asteroidCollision((Asteroid) p2);
            bulletCollision((Bullet) p1);  
            // The amount of time that the Bullet will stay on the screen
            new CountdownTimer (this,null,2000);
        }
    }

    /**
     * The bullet that has collided with something
     * @param b
     */
    private void bulletCollision (Bullet b)
    {
        screen.removeParticipant(b);
    }

    /**
     * The ship has collided with something
     */
    private void shipCollision (Ship s)
    {
//\\***//\\***//\\
        // Creates four lines from the Debris class
        for (int i = 0; i < 4; i++)
        {
            Debris d = new Debris(s.getX(), s.getY());
            screen.addParticipant(d);
            new CountdownTimer(this, d, 1000);
            d.setVelocity(1, 2 * Math.PI * random.nextDouble());
            d.setRotation(2 * Math.PI * random.nextDouble());
        }

        // Remove the ship from the screen and null it out
        screen.removeParticipant(s);
        ship = null;

        // Display a legend and make it disappear in one second
        screen.setLegend("Ouch!");
        new CountdownTimer(this, null, 1000);

        // Decrement lives
        lives--;

        // Start the timer that will cause the next round to begin.
        new TransitionTimer(END_DELAY, transitionCount, this);

        game.setLives(lives);
    }

    /**
     * Something has hit an asteroid
     */
    private void asteroidCollision (Asteroid a)
    {
        // The asteroid disappears
        screen.removeParticipant(a);

//\\***//\\***//\\
        // Creates five dust particles when an asteroid is hit
        for(int i = 0; i < 5; i++)
        {
            Dust du = new Dust(a.getX() + (5 * i * Math.random()/2), a.getY() + (5 * i * Math.random()));
            screen.addParticipant(du);
            new CountdownTimer(this, du, 2000);
            du.setVelocity(1, 2 * Math.PI * random.nextDouble());
        }

        // Create two smaller asteroids. Put them at the same position
        // as the one that was just destroyed and give them a random
        // direction.
        int size = a.getSize();
        size = size - 1;

        if (size == 1)
        {
            score += 20;
        }
        else if (size == 0)
        {
            score += 50;
        }
        else if (size == -1)
        {
            score += 100;
        } 
        this.game.setScore(score);

        if (size >= 0)
        {
            int speed = 3;
            Asteroid a1 = new Asteroid(random.nextInt(4), size, a.getX(),
                    a.getY());
            Asteroid a2 = new Asteroid(random.nextInt(4), size, a.getX(),
                    a.getY());
            a1.setVelocity(speed, random.nextDouble() * 2 * Math.PI);
            a2.setVelocity(speed, random.nextDouble() * 2 * Math.PI);
            a1.setRotation(2 * Math.PI * random.nextDouble());
            a2.setRotation(2 * Math.PI * random.nextDouble());
            screen.addParticipant(a1);
            screen.addParticipant(a2);
        }
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            transitionCount++;
            initialScreen();
        }

        // Time to refresh the screen
        else if (e.getSource() == refreshTimer)
        {

            // Refresh screen
            screen.refresh();
        }

        // If left key is pressed will rotate ship counter clockwise
        if (left == true)
        {
            if (ship != null)
                ship.rotate((-Math.PI / 16));
        }

        // If right key is pressed will rotate ship clockwise
        if (right == true)
        {
            if (ship != null)
                ship.rotate((Math.PI / 16));
        }

        // If up key is pressed will accelerate ship
        if(up == true)
        {
            if (ship != null)
            {
                ship.accelerate(1);
            }
        }
    }

    /**
     * Based on the state of the controller, transition to the next state.
     */
    public void performTransition ()
    {
        // Record that a transition was made. That way, any other pending
        // transitions will be ignored.
        transitionCount++;
        System.out.println(transitionCount);

        // If there are no lives left, the game is over. Show
        // the final screen.
        if (lives == 0)
        {
            finalScreen();
        }

        // The ship must have been destroyed. Place a new one and
        // continue on the current level
        else
        {
            placeShip();
        }
    }

    /**
     * Deals with certain key presses
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        // If left key is pressed it will assign boolean called left to true
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            left = true;
        }

        // If right key is pressed it will assign boolean called right to true
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            right = true;
        }

        // If up key is pressed it will assign boolean called up to true
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            up = true;
        }

        // If the down key is pressed it will make ship teleport to a random spot on the screen
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            if (ship != null)
                ship.setPosition(SIZE * random.nextDouble(), SIZE * random.nextDouble());
        }

        // If the space key is pressed the ship will shoot a bullet
        if (ship != null)
            if (e.getKeyCode() == KeyEvent.VK_SPACE)
            {   
                Bullet bullet = new Bullet(ship.getXNose(), ship.getYNose(), ship.getRotation());
                if (bullPressed.size() < 8)
                {
                    bullPressed.add(bullet);
                    screen.addParticipant(bullet);
                }
                CountdownTimer timer = new CountdownTimer (this, bullet, BULLET_DURATION);
            }
    }

    /**
     * Sets the booleans right, left and up to false when the key is released
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        right = false;
        left = false;
        up = false;
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
        right = true;
        left = true;
        up = true;
    }

    /**
     * Callback for countdown timer. Used to create transient effects.
     */
    @Override
    public void timeExpired (Participant p)
    {
        screen.setLegend("");
        screen.removeParticipant(p);
        
        // If participant is of type Bullet it will remove from the screen 
        // when timer expires and will make sure there are no more then 
        // 8 bullets on the game screen at once
        if (p instanceof Bullet)
        {
            screen.removeParticipant(p);
            bullPressed.remove(p);
        }
        
        // If participant is of type Debris than it will disappear it when timer expires
        if (p instanceof Debris)
        {
            screen.removeParticipant(p);
        }
    }
    
//\\***//\\***//\\
    public int getLives()
    {
        return lives;
    }
}
