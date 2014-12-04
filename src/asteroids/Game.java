package asteroids;

import javax.swing.*;
import java.awt.*;
import static asteroids.Constants.*;

/**
 * Implements an asteroid game.
 * 
 * @author Joe Zachary
 *
 */
public class Game extends JFrame
{
    /**
     * Launches the game
     */
    public static void main (String[] args)
    {
        Game a = new Game();
        a.setVisible(true);
    }
    
    private JPanel controls;
    private JLabel lives;
    private JLabel scoreL;
    private int score;
    
    private JLabel level;
    private int lev;


    /**
     * Lays out the game and creates the controller
     */
    public Game ()
    {
        // Title at the top
        setTitle(TITLE);

        // Default behavior on closing
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main playing area and the controller
        Screen screen = new Screen();
        Controller controller = new Controller(this, screen);

        // This panel contains the screen to prevent the screen from being
        // resized
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());
        screenPanel.add(screen);

        // This panel contains buttons and labels
        controls = new JPanel();

        // The button that starts the game
        JButton startGame = new JButton(START_LABEL);
        controls.add(startGame);
        
        // Organize everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(screenPanel, "Center");
        mainPanel.add(controls, "North");
        setContentPane(mainPanel);
        pack();

        // Connect the controller to the start button
        startGame.addActionListener(controller);
        
      //\\***//\\***//\\
        // The level at the moment for the initial screen
        level = new JLabel("Level: " + 1 + "\t");
        controls.add(level); 
        
        // The lives available for initial screen
        lives = new JLabel("\tLives: " + 3 + "\t");
        controls.add(lives); 
        
        // The score board for initial screen
        scoreL = new JLabel("Score: " + 0 + "\t");
        controls.add(scoreL);
    }
    
    /**
     *  Updates the level every time the player destroys all the asteroids
     * @param score
     */
    public void setLevel (int l)
    {
        this.lev = lev;
        level.setText("Level: " + l + "\t");
    }

    /**
     * Updates lives when there is a ship collision
     * @param l
     */
    public void setLives (int l)
    {
        lives.setText("\tLives: " + l + "\t");
    }
    
    /**
     *  Updates the score every time these is an asteroid collision
     * @param score
     */
    public void setScore (int score)
    {
        this.score = score;
        scoreL.setText("Score: " + score + "\t");
    }
}
