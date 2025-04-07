package rodeo.scott.bubbleswerve;

import java.awt.*;
import javax.swing.*;
import rodeo.scott.bubbleswerve.Game.PowerUpType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * PanelGame is the primary panel for rendering the game's grid, active pieces, 
 * and additional game elements like power-ups and avatars. It also handles 
 * user interaction and visual updates.
 * 
 * @author https://scott.rodeo/
 */
public class PanelGame extends JPanel {

    public static boolean atBottom;
    public static boolean downPushed;
    private boolean firstUnmaximize = true;

    private int clickX = -1;
    private int clickY = -1;

	private static JFrame frame;
    static Image clearLineImage;
    private Image selectedAvatar;
    private JPanel mainPanel;
    private JPanel gamePanel;
	private JPanel avatarSelectionPanel; 
    private CardLayout cardLayout;
	private Engine engine;

	// Declare timer as a class-level variable
    private Timer timer;

    
    /**
     * Constructs a PanelGame instance and initializes the panel menu.
     * 
     * @param engine The Engine instance managing the game and its components.
     */
    public PanelGame(Engine engine) {  
        this.engine = engine; // Store a reference to the game engine.
        initializePanelMenu(); // Set up the panel's menu and configurations.
    }

   
    /**
     * Initializes the panel menu by setting up its appearance and key bindings.
     * Ensures that UI updates are performed on the Event Dispatch Thread (EDT).
     */
    private void initializePanelMenu() {  
        SwingUtilities.invokeLater(() -> {
            // Set the background color of the game panel to black.
            engine.getPanelGame().setBackground(Color.BLACK);
        });
        setupKeyBindings(); // Configure key bindings for player controls.
    }

    
   
    /**
     * Requests a repaint of the panel to update its visuals.
     * This method is typically called when the game state changes and the panel needs to reflect the updates.
     */
    public void update() {
        repaint(); // Trigger a repaint to refresh the panel's display.
    }

    
    /**
     * Renders the components of the game panel, including the grid, selected avatar,
     * power-ups, and game state indicators (e.g., score and level).
     *
     * @param g The Graphics context used for rendering the panel's visuals.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the previous drawing.
        engine.getGame().draw(g); // Draw the game grid and active piece.

        // Draw the selected avatar at the bottom-right corner.
        if (selectedAvatar != null) {
            int screenWidth = getWidth();
            int avatarSize = screenWidth / 10; // 10% of the screen width.
            int x = getWidth() - avatarSize - 10; // Small padding from the right.
            int y = getHeight() - avatarSize - 10; // Small padding from the bottom.
            g.drawImage(selectedAvatar, x, y, avatarSize, avatarSize, this);
        }

        /*
         * Debugging: Draw the click coordinates.
         * Uncomment the code block below to display the last clicked coordinates on the panel.
         */
        /*
        if (clickX >= 0 && clickY >= 0) {
            g.setColor(Color.RED); // Set text color for debugging.
            g.drawString("Click: (" + clickX + ", " + clickY + ")", clickX, clickY);
        }
        */

        // Render the ClearLine Power-Up icon if the power-up is active.
        if (engine.getGame().isPowerupActive()) {
            int cellSize = getCellSize(); // Get the size of a grid cell.
            PowerUpType activePowerUpType = getPowerupType(); // Determine the active power-up type.

            Object powerupIcon = null;

            // Select the appropriate icon based on the active power-up type.
            switch (activePowerUpType) {
                case CLEAR_LINE:
                    powerupIcon = "resources/images/clearline.png";
                    break;
                case SLOW_SPEED:
                    powerupIcon = "resources/images/slowspeed.png";
                    break;
                case EXTRA_POINTS:
                    powerupIcon = "resources/images/extrapoints.png";
                    break;
                case EXTRA_LIFE:
                    powerupIcon = "resources/images/extralife.png";
                    break;
            }

            if (powerupIcon != null) {
                clearLineImage = new ImageIcon((String) powerupIcon).getImage();
                int drawCoordX = (engine.getGame().getpowerupX() * cellSize) + Grid.LEFT;
                int drawCoordY = (engine.getGame().getpowerupY() * cellSize) + Grid.TOP;

                g.drawImage(
                    clearLineImage,
                    drawCoordX, // Horizontal position based on grid and offset.
                    drawCoordY, // Vertical position based on grid and offset.
                    cellSize, cellSize, // Scale to fit the grid cell.
                    this
                );
            }
        }

        // Display "Game Over" text if the game is over.
        if (engine.getGame().isGameOver()) {
            g.setFont(new Font("Palatino", Font.BOLD, 40));
            g.setColor(Color.BLACK);
            // g.drawString("GAME OVER", 80, 300); // Uncomment to display "GAME OVER".
        }

        // Render the score and level indicators at the top-left corner.
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + engine.getGame().getScore(), 20, 50); // Display score.
        g.drawString("Level: " + engine.getGame().getLevel(), 20, 80); // Display level below score.
    }


    /**
     * Calculates the size of each grid cell based on the current window dimensions and grid configuration.
     *
     * @return The calculated size of a single cell in pixels.
     */
    public int getCellSize() {
        Dimension size = engine.getProgramWindow().getContentPane().getSize(); // Get the size of the game window.
        Insets insets = engine.getProgramWindow().getInsets(); // Get the window's insets.

        int windowWidth = size.width; // The width of the game window.
        int windowHeight = size.height - insets.top; // The height of the game window minus the top inset.

        int gridWidth = engine.game.grid.WIDTH; // Number of columns in the game grid.
        int gridHeight = engine.game.grid.HEIGHT; // Number of rows in the game grid.

        // Calculate the maximum size of the grid that fits within the window dimensions.
        int windowMax = Math.min(windowWidth - 200, windowHeight - 200);

        // Determine the cell size by dividing the maximum grid size by the grid dimensions.
        int cellSize = Math.min(windowMax / gridWidth, windowMax / gridHeight);

        // Return the smallest dimension to ensure square cells.
        return cellSize;
    }


    /**
     * Calculates the left margin to horizontally center the game grid within the window.
     *
     * @return The left margin in pixels.
     */
    public int calculateLeft() {
        Dimension size = engine.getProgramWindow().getContentPane().getSize(); // Get the size of the game window.

        int windowWidth = size.width; // The total width of the window.
        int gridWidth = engine.game.grid.WIDTH * getCellSize(); // The width of the grid in pixels.

        // Calculate the left margin needed to center the grid.
        int leftMargin = (windowWidth - gridWidth) / 2;

        // Return the calculated margin for horizontal centering.
        return leftMargin;
    }

    
    /**
     * Calculates the top margin to vertically center the game grid within the window.
     *
     * @return The top margin in pixels.
     */
    public int calculateTop() {
        Dimension size = engine.getProgramWindow().getContentPane().getSize(); // Get the size of the game window.

        Insets insets = engine.getProgramWindow().getInsets(); // Get the window's insets.

        int windowHeight = size.height - insets.top; // The height of the window minus the top inset.

        int gridHeight = engine.game.grid.HEIGHT * getCellSize(); // The height of the grid in pixels.

        // Calculate the top margin needed to center the grid vertically.
        int topMargin = (windowHeight - gridHeight) / 2;

        // Return the calculated margin for vertical centering.
        return topMargin;
    }

    
    /**
     * Retrieves the currently active power-up type in the game.
     *
     * @return The active PowerUpType, or null if no power-up is active.
     */
    private PowerUpType getPowerupType() {
        // Delegate to the game's method to get the active power-up type.
        return engine.getGame().getPowerupType();
    }

       

    
    /**
     * Configures key bindings for player controls within the game panel.
     * This method maps specific key presses to corresponding game actions.
     */
    private void setupKeyBindings() {
    	
    	InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Bind "LEFT"
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
	           if (!engine.getGame().getPaused()) { 
	            		
        	        switch (engine.getGame().currentOrientation) {
	    	            case DOWN: // Default orientation (no changes needed)
	    	            	engine.getGame().handleMove(Direction.LEFT);
	    	                break;
	
	    	            case UP: // Upside-down orientation
	    	            	engine.getGame().handleMove(Direction.LEFT);
	    	                break;
	
	    	            case LEFT: // Rotated left (counterclockwise 90°)
	    	            	engine.getGame().softDrop(); // Calls the softDrop method in Game
	    	                break;
	
	    	            case RIGHT: // Rotated right (clockwise 90°)
	    	            	engine.getGame().handleRotate(0);// "Rotate Clockwise"
	    	                break;
        	        }

	                repaint();
	                engine.getGame().checkPowerUpCollection(); // Check for power-up collision
            	}
            }
        });

        // Bind "RIGHT"
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (!engine.getGame().getPaused())  {
        	        switch (engine.getGame().currentOrientation) {
    	            case DOWN: // Default orientation (no changes needed)
    	            	engine.getGame().handleMove(Direction.RIGHT);
    	                break;

    	            case UP: // Upside-down orientation
    	            	engine.getGame().handleMove(Direction.RIGHT);
    	                break;

    	            case LEFT: // Rotated left (counterclockwise 90°)
    	            	engine.getGame().handleRotate(0);// "Rotate Clockwise"
    	                break;

    	            case RIGHT: // Rotated right (clockwise 90°)
    	            	engine.getGame().softDrop(); // Calls the softDrop method in Game
    	                break;
    	        }
	                repaint();
	                engine.getGame().checkPowerUpCollection(); // Check for power-up collision
            	}
            }
        });

        // Bind "DOWN"
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "softDrop");
        inputMap.put(KeyStroke.getKeyStroke("S"), "softDrop");
        actionMap.put("softDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!engine.getGame().getPaused())  {
            		
        	        switch (engine.getGame().currentOrientation) {
    	            case DOWN: // Default orientation (no changes needed)
    	            	engine.getGame().softDrop(); // Calls the softDrop method in Game
    	                break;

    	            case UP: // Upside-down orientation
    	            	engine.getGame().handleRotate(0);// "Rotate Clockwise"	    	               
    	                break;

    	            case LEFT: // Rotated left (counterclockwise 90°)
    	            	engine.getGame().handleMove(Direction.DOWN);
    	                break;

    	            case RIGHT: // Rotated right (clockwise 90°)
    	            	engine.getGame().handleMove(Direction.DOWN);
    	                break;
	    	        }
	                
	                repaint();
	                engine.getGame().checkPowerUpCollection(); // Check for power-up collision
	                
            	}
            }
        });

        // Bind "UP"
        inputMap.put(KeyStroke.getKeyStroke("UP"), "rotateClockwise");
        inputMap.put(KeyStroke.getKeyStroke("W"), "rotateClockwise");
        actionMap.put("rotateClockwise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!engine.getGame().getPaused())  {
        	        switch (engine.getGame().currentOrientation) {
    	            case DOWN: // Default orientation (no changes needed)
    	            	engine.getGame().handleRotate(0);// "Rotate Clockwise"	 
    	                break;

    	            case UP: // Upside-down orientation
    	            	engine.getGame().softDrop(); // Calls the softDrop method in Game    	               
    	                break;

    	            case LEFT: // Rotated left (counterclockwise 90°)
    	            	engine.getGame().handleMove(Direction.UP);
    	                break;

    	            case RIGHT: // Rotated right (clockwise 90°)
    	            	engine.getGame().handleMove(Direction.UP);
    	                break;
	    	        }
	                
	                repaint();
	                engine.getGame().checkPowerUpCollection(); // Check for power-up collision
            	}
            }
        });
        
        // Bind "Hard Drop"
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "hardDrop");
        actionMap.put("hardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!engine.getGame().getPaused())  {
            		
            		engine.getGame().hardDrop(); // Calls the hardDrop method in Game
	                repaint();
            	}
            }
        });

        // Bind "Rotate Counterclockwise"
        inputMap.put(KeyStroke.getKeyStroke("released SHIFT"), "rotateCounterclockwise");
        inputMap.put(KeyStroke.getKeyStroke("Q"), "rotateCounterclockwise");
        actionMap.put("rotateCounterclockwise", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!engine.getGame().getPaused())  {
            		engine.getGame().handleRotate(1);// "Rotate Counterclockwise"
	                repaint();
            	}
            }
        });
    
        // Bind Escape to pause
        inputMap.put(KeyStroke.getKeyStroke("P"), "pauseGame");
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "pauseGame");
        actionMap.put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	engine.getGame().togglePause();
            }
        });

        // Rotate the board
        inputMap.put(KeyStroke.getKeyStroke("R"), "rotateGame");
        actionMap.put("rotateGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (engine.getGame() != null) { // Ensure the Game instance is accessible
                	engine.getGame().rotateBoardClockwise(); // Rotate the board
                }
            }
        });

    }


    /**
     * Resumes the game by ensuring the timer is running and restarting it if necessary.
     * This method also resets the game-over flag and updates the game state periodically.
     */
    void resumeGame() {
        System.out.println("Game Resumes");
        engine.getGame().gameIsOver = false; // Reset the game-over flag.

        // Initialize and start the timer if it is null or not running.
        if (timer == null || !timer.isRunning()) {
            timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!engine.getGame().getPaused()) { // Check if the game is not paused.
                        if (engine.getGame().isGameOver()) {
                            timer.stop(); // Stop the timer if the game is over.
                            engine.getGame().GameOverScreen(); // Display the game-over screen.
                        } else {
                            engine.getGame().updateState(); // Update the game state.
                            repaint(); // Redraw the game panel.
                        }
                    }
                }
            });
            timer.start(); // Start the timer.
        } else {
            timer.restart(); // Restart the timer if it already exists.
        }
    }

  
    
}



