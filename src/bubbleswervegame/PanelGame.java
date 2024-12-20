package bubbleswervegame;

import java.awt.*;
import javax.swing.*;
import bubbleswervegame.Game.PowerUpType;
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

    
	public PanelGame(Engine engine) {  
		this.engine = engine;
		initializePanelMenu();
	}
    
   
	private void initializePanelMenu() {  
		SwingUtilities.invokeLater(() -> {
			engine.getPanelGame().setBackground(Color.BLACK);
		});
		setupKeyBindings();
		setupListeners();
	}
    
   
	public void update() {
    	repaint();
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        engine.getGame().draw(g);

        // Draw the selected avatar at the bottom-left
        if (selectedAvatar != null) {
            int screenWidth = getWidth();
            int avatarSize = screenWidth / 10;      // 10% of the screen width
            int x = getWidth() - avatarSize - 10;   // Small padding from the left
            int y = getHeight() - avatarSize - 10;  // Small padding from the bottom
            g.drawImage(selectedAvatar, x, y, avatarSize, avatarSize, this);
        }
        
        /*
        // Draw the click coordinates for testing
        if (clickX >= 0 && clickY >= 0) {
            g.setColor(Color.RED); // Set text color
            g.drawString("Click: (" + clickX + ", " + clickY + ")", clickX, clickY);
        }
        */
        
        // Render the ClearLine Power-Up
        if (engine.getGame().isPowerupActive()) {
        	
            int cellSize = getCellSize(); // Get the cell size
        	PowerUpType activePowerUpType = getPowerupType();           
    
        	Object powerupIcon = null;
        	
			// Handle the power-up based on its type
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

		    
            clearLineImage = new ImageIcon((String) powerupIcon).getImage();	
            
            
	        int drawCoordX = (engine.getGame().getpowerupX() * cellSize) + Grid.LEFT;
	        int drawCoordY = (engine.getGame().getpowerupY() * cellSize) + Grid.TOP;
	        
	        g.drawImage(
            	clearLineImage, 
            	drawCoordX, // Apply LEFT offset for the column
            	drawCoordY,  // Apply TOP offset for the row
            	cellSize, cellSize, // Scale to fit the grid cell
            	this
	        );
	        
	        
        }

        // Draw "Game Over" if the game is over
        if (engine.getGame().isGameOver()) {
            g.setFont(new Font("Palatino", Font.BOLD, 40));
            g.setColor(Color.BLACK);
            //g.drawString("GAME OVER", 80, 300);
        }

        
        // Draw the legend
        //drawLegend(g);

        // Render score and level
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + engine.getGame().getScore(), 20, 50); // Score at top-left
        g.drawString("Level: " + engine.getGame().getLevel(), 20, 80); // Level below score
    }



    public int getCellSize() {
		
        Dimension size = engine.getProgramWindow().getContentPane().getSize();
        Insets insets = engine.getProgramWindow().getInsets();
		
		int windowWidth = size.width;
		int windowHeight = size.height - insets.top;
		
        int gridWidth = Grid.WIDTH;    // Number of columns in the grid
        int gridHeight = Grid.HEIGHT;  // Number of rows in the grid

	    int windowMax = Math.min(windowWidth-200 , windowHeight-200);	    
	    
	    int cellSize = Math.min((windowMax) / gridWidth, (windowMax) / gridHeight);
		
        // Use the minimum dimension to calculate cell size
        return cellSize;
    }


    public int calculateLeft() {

        Dimension size = engine.getProgramWindow().getContentPane().getSize();
        //System.out.println("New width: " + size.width + ", New height: " + size.height);
		
		
		int windowWidth = size.width;
		
        int gridWidth = Grid.WIDTH * getCellSize(); // Width of the grid in pixels
    
        int leftMargin = (windowWidth - gridWidth) / 2;
        
        //System.out.println("calculateLeft(): windowWidth:" + windowWidth + " | gridWidth:" + gridWidth + " | leftMargin:" + leftMargin);    
        

       
        return leftMargin; // Center the grid horizontally
    }
    
    
    public int calculateTop() {

        Dimension size = engine.getProgramWindow().getContentPane().getSize();
        //System.out.println("New width: " + size.width + ", New height: " + size.height);
		
        Insets insets = engine.getProgramWindow().getInsets();
        //System.out.println("calculateTop(): Top inset:" + insets.top);
        
		
		int windowHeight = size.height - insets.top;
		
        int gridHeight = Grid.HEIGHT * getCellSize(); // Width of the grid in pixels

        int topMargin = (windowHeight - gridHeight) / 2;
        
        //System.out.println("calculateTop(): windowHeight:" + windowHeight + " | gridHeight:" + gridHeight + " | topMargin:" + topMargin);    
        

        
        return topMargin; // Center the grid horizontally
    }
    
  
    private PowerUpType getPowerupType() {

        // Choose the smaller dimension to keep cells square
        return engine.getGame().getPowerupType();
    }
    
    

    /**
     * Draws the controls legend at the bottom of the panel.
     */
    private void drawLegend(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.WHITE);

        // Legend lines
        String[] legendLines = {
            "Controls:",
            "Move Left, Move Right",
            "Soft Drop: Down Arrow | Hard Drop: Space",
            "Rotate Clockwise: Up | Rotate Counterclockwise: Shift"
        };

        // Starting position for the legend
        int x = 20; // Padding from the left
        int y = getHeight() - 60; // Start a little above the bottom
        int lineHeight = 15; // Space between lines

        // Draw each line
        for (String line : legendLines) {
            g.drawString(line, x, y);
            y += lineHeight; // Move down for the next line
        }
    }

    
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






    
    void resumeGame() {
        // Logic to restart the game
    	

        System.out.println("Game Resumes");
        engine.getGame().gameIsOver=false;
    	

        // Ensure the timer is properly initialized and started
        if (timer == null || !timer.isRunning()) {
            timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!engine.getGame().getPaused()) { // Only update the game if it's not paused
                        if (engine.getGame().isGameOver()) {
                            timer.stop(); // Stop the timer
                            engine.getGame().GameOverScreen(); // Show the game-over screen
                        } else {
                        	engine.getGame().updateState();
                            repaint(); // Redraw the game
                        }
                    }
                }
            });
            timer.start();
        } else {
            timer.restart(); // Restart the timer if it already exists
        }
    }
    

 // Declare timer as a class-level variable
    private Timer timer;



    
    

    
    /*
    public void PanelGame2(Engine launcher) {
    	
        this.engine = launcher; // Store reference to the Game object
    	
        launcher.getPanelGame().setBackground(Color.BLACK);

        setupKeyBindings();

        // Timer for game updates and rendering

        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!launcher.getGame().getPaused()) { // Only update the game if it's not paused
                    if (launcher.getGame().isGameOver()) {
                        ((Timer) e.getSource()).stop(); // Stop the timer
                        launcher.getGame().GameOverScreen(); // Show the game-over screen
                    } else {
                    	launcher.getGame().updateState();
                        repaint(); // Redraw the game
                    }
                }
            }
        });
        timer.start();
        

       
    
        // Randomly select an avatar when the game starts
        String[] avatarPaths = {
	            "resources/avatars/blazorb.png",
	            "resources/avatars/cryon.png",
	            "resources/avatars/lumina.png",
	            "resources/avatars/spherra.png",
	            "resources/avatars/umbra.png",
	            "resources/avatars/zephyr.png"
        };
        String randomAvatarPath = avatarPaths[(int) (Math.random() * avatarPaths.length)];
        selectedAvatar = new ImageIcon(randomAvatarPath).getImage();
        
        
        
        
        
        
        
        // Avatar Selection Panel
        avatarSelectionPanel = new JPanel();
        avatarSelectionPanel.setBackground(Color.LIGHT_GRAY);
        avatarSelectionPanel.setLayout(new FlowLayout());
        String[] avatars = {
                "resources/avatars/blazorb.png",
                "resources/avatars/cryon.png",
                "resources/avatars/lumina.png",
                "resources/avatars/spherra.png",
                "resources/avatars/umbra.png",
                "resources/avatars/zephyr.png"
        };
        
        
        setupListeners();
        
        for (String avatarPath : avatars) {
            JButton avatarButton = new JButton(new ImageIcon(avatarPath));
            avatarButton.addActionListener(e -> {
                selectedAvatar = new ImageIcon(avatarPath).getImage();
                cardLayout.show(mainPanel, "Game");
                repaint();
            });
            avatarSelectionPanel.add(avatarButton);
        }
    }
    
    
    
    public void switchToGameScreen() {
        cardLayout.show(mainPanel, "Game");
    }
    

    public void switchToAvatarSelectionScreen() {
        cardLayout.show(mainPanel, "AvatarSelection");
    }
    
     */
    
    
    
    
    
    
    public void setupListeners() {
    	/*
        // Add resizing listener
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = frame.getContentPane().getSize();
                //System.out.println("New width: " + size.width + ", New height: " + size.height);
                repaint(); // Adjust the rendering
            }
        });

        // Add a MouseListener to capture clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickX = e.getX(); // Get X-coordinate of click
                clickY = e.getY(); // Get Y-coordinate of click
                //System.out.println("Clicked at: (" + clickX + ", " + clickY + ")");
                repaint(); // Trigger a repaint to show coordinates
            }
        });
    	*/
    }
    
    
}
