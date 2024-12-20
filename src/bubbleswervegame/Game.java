package bubbleswervegame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author https://scott.rodeo/
 */

public class Game {

	private Grid grid;				
	private AbstractBubbloid piece; 
	public boolean gameIsOver;
	private boolean powerupActive = false;
	private int powerupX; 
	private int powerupY;

	public Engine engine;
	
	public PowerUpType activePowerUpType;

	private int powerupTickCounter = 0; // Tracks the number of ticks since power-up spawn

    private boolean isPaused = false;
    
	public static Color crimson = new Color(220, 20, 60);      // Crimson
	public static Color skyBlue = new Color(135, 206, 235);    // Sky Blue
	public static Color limeGreen = new Color(50, 205, 50);    // Lime Green
	public static Color gold = new Color(255, 215, 0);         // Gold
	public static Color magenta = new Color(255, 0, 255);      // Magenta
	public static Color teal = new Color(0, 128, 128);         // Teal
	public static Color orangeRed = new Color(255, 69, 0);     // Orange Red
	public static Color lavender = new Color(230, 230, 250);   // Lavender
	public static Color deepPurple = new Color(106, 90, 205);  // Deep Purple
	
	public static Color b1 = new Color(2, 167, 212);  
	public static Color b2 = new Color(28, 158, 202);  
	public static Color b3 = new Color(59, 180, 216);  
	public static Color b4 = new Color(180, 227, 243);  
	public static Color b5 = new Color(243, 249, 252);  
	public static Color b6 = new Color(121, 201, 230);
	public static Color b7 = new Color(159, 212, 223);
	public static Color b8 = new Color(154, 201, 214);
	public static Color b9 = new Color(123, 178, 177);
	
	
    public enum Orientation {
        DOWN, LEFT, RIGHT, UP
    }
    Orientation currentOrientation = Orientation.DOWN; // Default orientation
	
	private int score = 0;
	private int level = 1;
	private int linesCleared = 0;

	private Timer timer;

	public enum PowerUpType {
	    CLEAR_LINE,
	    SLOW_SPEED,
	    EXTRA_POINTS,
	    EXTRA_LIFE
	}

	
	public Game(Engine engine) {
		this.engine=engine;
        this.grid = new Grid(engine);
	    gameIsOver = false;
	    this.score = 0;
	    this.level = 1;
	    this.linesCleared = 0;
		initializeGame();
	}
	    
	
	private void initializeGame() {  
		timerStart();
		gameStart();
	}
	   
	
    void gameStart() {
	    spawnNewPiece();
    }
	
    
    void gameRestart() {
    	gameBoardReset();
	    spawnNewPiece();
    }
    
    

    void timerStart() {
        if (timer == null) {
            timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isPaused) {
                        if (isGameOver()) {
                            timer.stop();
                            GameOverScreen();
                        } else {
                            updateState();
                            engine.getPanelGame().repaint();
                        }
                    }
                }
            });
            timer.start();
        } else {
            timer.restart(); // Restart the timer if it already exists
        }
    }
    
    

    public void gameBoardReset() {
        System.out.println("gameBoardReset()");

        this.gameIsOver = false;
        
    	this.grid.HEIGHT = 22; 
    	this.grid.WIDTH = 12;    	
    	this.grid = new Grid(engine);
	    this.score = 0;
	    this.level = 1;
	    this.linesCleared = 0;

	    currentOrientation = Orientation.DOWN; // Default orientation
	    timerStart();
    }
    
		

	public void draw(Graphics g) {
		grid.draw(g);
		if (piece != null) {
			piece.draw(g);
		}
	}

	

	public void movePiece(Direction direction) {
		if (piece != null) {
			if(direction == Direction.DROP) {
				while(piece.canMove(Direction.DOWN)) {
					piece.move(Direction.DOWN);
				}
			}else {
				piece.move(direction);
			}
		}
		
		updatePiece();
		this.engine.getPanelGame().update();
		grid.checkRows();
	}

	
	
	public void handleMove(Direction direction) {
	    if (isGameOver()) {
	    	GameOverScreen();
	    }else{
		    movePiece(direction);
	    }
	}

	
	
	public void GameOverScreen() {
		System.out.println("Game Over!");
	}
	

	
	public void handleRotate(int clockDirection) {
	    if (isGameOver()) {
	    	GameOverScreen();
	    }else{
			rotatePiece(clockDirection); 
	    }
	}
	
	
	
	private void removeActivePiece() {
	    if (piece != null) {
	        // Clear the piece's bubbles from the grid
	        for (Bubble square : piece.getBubbles()) {
	            grid.clearCell(square.getRow(), square.getCol());
	        }

	        // Remove the reference to the active piece
	        piece = null;
	    }
	}

	
	
	public void rotateBoard() {
	    // Rotate the grid
	    grid.rotateBoardClockwise();

	    // Update the orientation
	    switch (currentOrientation) {
	        case DOWN:
	            currentOrientation = Orientation.LEFT;
	            break;
	        case RIGHT:
	            currentOrientation = Orientation.DOWN;
	            break;
	        case UP:
	            currentOrientation = Orientation.RIGHT;
	            break;
	        case LEFT:
	            currentOrientation = Orientation.UP;
	            break;
	    }

	}

	
	
	public void rotateBoardClockwise() {
	    // Rotate the grid
	    grid.rotateBoardClockwise();

	    // Update the orientation
	    switch (currentOrientation) {
	        case DOWN:
	            currentOrientation = Orientation.LEFT;
	            break;
	        case RIGHT:
	            currentOrientation = Orientation.DOWN;
	            break;
	        case UP:
	            currentOrientation = Orientation.RIGHT;
	            break;
	        case LEFT:
	            currentOrientation = Orientation.UP;
	            break;
	    }

	    // Remove the active piece
	    removeActivePiece();

	    // Spawn a new piece
	    spawnNewPiece();
	    // Refresh the display
	    engine.getPanelGame().update();
	    System.out.println("Board rotated to: " + currentOrientation);
	}


	
	
	private void updateScore(int clearedLines) {
	    switch (clearedLines) {
	        case 1: score += 100 * level; break;
	        case 2: score += 300 * level; break;
	        case 3: score += 500 * level; break;
	        case 4: score += 800 * level; break;
	    }

	    level = (score / 1000) + 1;
	    System.out.println("Current Level: " + level);
	}


	
	public void updateState() {
	    gameTick();
	}

	
	
	private void deactivatePowerUp() {
	    powerupActive = false; 
	    powerupTickCounter = 0; 
	    System.out.println("Power-up disappeared after 10 ticks.");
	}

	

	private void gameTick() {

	    applyGravity();

	    // Handle power-up tick countdown
	    if (powerupActive) {
	        powerupTickCounter++;
	        if (powerupTickCounter >= 36) {
	            deactivatePowerUp();
	        }
	    }

	    engine.getPanelGame().update(); // Refresh the game screen
		checkPowerUpCollection();
	}

	
	
    void pauseGame() {
		isPaused = true;
		System.out.println("Game paused.");
    }
    
	
	
	public void adjustPieceAfterBoardRotation(AbstractBubbloid piece) {
	    if (piece == null) return;

	    for (Bubble bubble : piece.getBubbles()) {
	        int originalRow = bubble.getRow();
	        int originalCol = bubble.getCol();
	        int newRow = originalRow;
	        int newCol = originalCol;

	        switch (currentOrientation) {
	            case DOWN:
	                newRow = originalRow;
	                newCol = originalCol;
	                break;

	            case UP:
	                newRow = Grid.HEIGHT - 1 - originalRow;
	                newCol = Grid.WIDTH - 1 - originalCol;
	                break;

	            case LEFT:
	                newRow = originalCol;
	                newCol = Grid.HEIGHT - 1 - originalRow;
	                break;

	            case RIGHT:
	                newRow = Grid.WIDTH - 1 - originalCol;
	                newCol = originalRow;
	                break;
	        }

	        bubble.setRow(newRow);
	        bubble.setCol(newCol);
	    }
	}

	
	
	public boolean isPowerupActive() {
	    return powerupActive; // Returns whether the ClearLine power-up is active
	}

	
	
	private void lockPiece() {
	    if (piece != null) {
	        Point[] pieceLocations = piece.getLocations();
	        Color pieceColor = piece.getColor();

	        // Lock the piece onto the grid
	        for (Point point : pieceLocations) {
	        	
	        	
	            //System.out.println("point.getX(), (int) point.getY()::"+ point.getX()+"::"+ (int) point.getY());
	        	
	            grid.set((int) point.getY(), (int) point.getX(), pieceColor);
	        }

	        piece = null; // Clear the current piece reference

	        // Clear full rows and update the score
	        int clearedLines = grid.clearFullRows(currentOrientation);
	        updateScore(clearedLines);

	        // Check for game over
	        if (isGameOver()) {
	            GameOverScreen();
	        } else {
	            // Spawn a new piece
	        	rotateBoard();
	            spawnNewPiece();
	        }
	    }
	}



	public int getpowerupX() {
	    return powerupX;
	}

	
	
	public int getpowerupY() {
	    return powerupY;
	}

	
	
	private void spawnNewPiece() {
	    
		int initialRow = 1;
		int initialColumn = Grid.WIDTH / 2 - 1;
		
	    switch (currentOrientation) {
        case DOWN:
    		initialRow = 2;
    		initialColumn = Grid.WIDTH / 2 - 1;
    		break;
        case UP:
    		initialRow = Grid.HEIGHT - 2;
    		initialColumn = Grid.WIDTH / 2 - 1;
            break;
        case LEFT: 
    		initialRow = Grid.HEIGHT / 2 - 1;
    		initialColumn = Grid.WIDTH - 2;
            break;
        case RIGHT: 
    		initialRow = Grid.HEIGHT / 2 - 1;
    		initialColumn = 1;
            break;
	    }
		
		
	    int random = (int)(Math.random() * 9 + 1);
	    switch(random){
	        case 1:
	        	piece = new BubbloidBar1(initialRow, initialColumn, grid, b1);    
	            break;
	        case 2:
	            piece = new BubbloidBar3(initialRow, initialColumn, grid, b2);
	            break;
	        case 3:
	        	piece = new BubbloidL5(initialRow, initialColumn, grid, b3); 
	            break;
	        case 4:
	            piece = new BubbloidBar2(initialRow, initialColumn, grid, b4);       
	            break;
	        case 5:
	            piece = new BubbloidCross5(initialRow, initialColumn, grid, b5);   
	            break;
	        case 6:
	            piece = new BubbloidV3(initialRow, initialColumn, grid, b6);          
	            break;
	        case 7:
	            piece = new BubbloidJ5(initialRow, initialColumn, grid, b7);     
	            break;
	        case 8:
	            piece = new BubbloidRectangle6(initialRow, initialColumn, grid, b8);     
	            break;    
	        case 9:
	            piece = new BubbloidVDiscon2(initialRow, initialColumn, grid, b9);     
	            break;	            
	    }
	}


	
	public void applyGravity() {
	    checkPowerUpCollection();

	    if (piece != null) {
	        switch (currentOrientation) {
	            case DOWN:
	                if (piece.canMove(Direction.DOWN)) {
	                    piece.move(Direction.DOWN);
	                } else {
	                    lockPiece();
	                }
	                break;
	            case LEFT:
	                if (piece.canMove(Direction.LEFT)) {
	                    piece.move(Direction.LEFT);
	                } else {
	                    lockPiece();
	                }
	                break;
	            case RIGHT:
	                if (piece.canMove(Direction.RIGHT)) {
	                    piece.move(Direction.RIGHT);
	                } else {
	                    lockPiece();
	                }
	                break;
	            case UP:
	                if (piece.canMove(Direction.UP)) {
	                    piece.move(Direction.UP);
	                } else {
	                    lockPiece();
	                }
	                break;
	        }
	    }
	    engine.getPanelGame().update();
	}


	
	public void spawnPowerUp() {
	    if (!powerupActive && Math.random() < 0.9) { // % chance to spawn a power-up
	        int attempts = 0;
	        final int maxAttempts = 100; // Max number of attempts to find a free square

	        do {
	            powerupX = (int) (Math.random() * (Grid.WIDTH - 1)) + 1;
	            powerupY = (int) (Math.random() * (Grid.HEIGHT - 4)) + 4; 
	            attempts++;
	        } while (grid.isSet(powerupY, powerupX) && attempts < maxAttempts); // Ensure the square is not filled

	        if (attempts >= maxAttempts) {
	            System.out.println("Failed to find an empty square for power-up after " + maxAttempts + " attempts.");
	            return;
	        }

	        PowerUpType[] powerUpTypes = PowerUpType.values();
	        int randomIndex = (int) (Math.random() * powerUpTypes.length);
	        
	        activePowerUpType = powerUpTypes[0]; // clear line powerup
	        powerupActive = true;
	        powerupTickCounter = 0;
	        //System.out.println("Power-up spawned: " + activePowerUpType + " at (" + powerupX + ", " + powerupY + ")");
	    }
	}

	
	
	private void activatePowerUp() {
	    switch (activePowerUpType) {
	        case CLEAR_LINE:
	            grid.deleteRow(19); // clear bottom row
	            break;
	        case SLOW_SPEED:
	            break;
	        case EXTRA_POINTS:
	            score += 500;
	            break;
	        case EXTRA_LIFE:
	            break;
	        default:
	            System.out.println("Unknown power-up type: " + activePowerUpType);
	    }
	    System.out.println("Activated power-up: " + activePowerUpType);
	}

	
	void checkPowerUpCollection() {
	    if (!powerupActive || piece == null) return;

	    Point[] pieceLocations = piece.getLocations();

	    for (Point block : pieceLocations) {
	        int blockX = (int) block.getY();
	        int blockY = (int) block.getX();

	        // Check if the piece overlaps with the power-up
	        if (blockX == powerupX && blockY == powerupY) {
	            activatePowerUp(); 
	            powerupActive = false;
	            //System.out.println("Power-up collected at: (" + powerupX + ", " + powerupY + ")");
	            break;
	        }
	    }
	}

	
	
	public boolean isGameOver() {
	    switch (currentOrientation) {
	        case DOWN: // Check the top row
	            for (int col = 0; col < Grid.WIDTH; col++) {
	                if (!grid.isValidAndEmpty(0, col)) {
	                    return true; // Top row filled
	                }
	            }
	            break;
	        case UP: // Check the bottom row
	            for (int col = 0; col < Grid.WIDTH; col++) {
	                if (!grid.isValidAndEmpty(Grid.HEIGHT - 1, col)) {
	                    return true; // Bottom row filled
	                }
	            }
	            break;
	        case LEFT: // Check the rightmost column
	            for (int row = 0; row < Grid.HEIGHT; row++) {
	                if (!grid.isValidAndEmpty(row, Grid.WIDTH - 1)) {
	                    return true; // Right column filled
	                }
	            }
	            break;
	        case RIGHT: // Check the leftmost column
	            for (int row = 0; row < Grid.HEIGHT; row++) {
	                if (!grid.isValidAndEmpty(row, 0)) {
	                    return true; // Left column filled
	                }
	            }
	            break;
	    }
	    return false;
	}

	
	
	public void rotatePiece(int clockDirection) {
		piece.rotate(clockDirection);
	}

	
	
	private void updatePiece() {
	    if (piece == null) {
		    if (isGameOver()) {
		    	
		    	GameOverScreen();
		        
		    }else{
		        // Spawn a new piece
		        spawnNewPiece();
		    }
	    }
	}

	

	public void softDrop() {
	        
		switch (currentOrientation) {
            case DOWN: // Default orientation (no changes needed)
        	    if (piece != null && piece.canMove(Direction.DOWN)) {
        	        piece.move(Direction.DOWN);
        	        score += 1; // 1 point per soft drop
        	        checkPowerUpCollection(); // Check for power-up collision
        	    }
                break;

            case UP: // Upside-down orientation
        	    if (piece != null && piece.canMove(Direction.UP)) {
        	        piece.move(Direction.UP);
        	        score += 1; // 1 point per soft drop
        	        checkPowerUpCollection(); // Check for power-up collision
        	    }
                break;

            case LEFT: // Rotated left (counterclockwise 90°)
        	    if (piece != null && piece.canMove(Direction.LEFT)) {
        	        piece.move(Direction.LEFT);
        	        score += 1; // 1 point per soft drop
        	        checkPowerUpCollection(); // Check for power-up collision
        	    }
                break;

            case RIGHT: // Rotated right (clockwise 90°)
        	    if (piece != null && piece.canMove(Direction.RIGHT)) {
        	        piece.move(Direction.RIGHT);
        	        score += 1; // 1 point per soft drop
        	        checkPowerUpCollection(); // Check for power-up collision
        	    }
                break;
        }
	}


	
	public void hardDrop() {
		
	    int cellsDropped = 0;

		switch (currentOrientation) {
        case DOWN: 
    	    while (piece != null && piece.canMove(Direction.DOWN)) {
    	        piece.move(Direction.DOWN);
    	        cellsDropped++;
    	        checkPowerUpCollection();
    	    }
            break;
        case UP: 
        	while (piece != null && piece.canMove(Direction.UP)) {
    	        piece.move(Direction.UP);
    	        cellsDropped++;
    	        checkPowerUpCollection();
    	    }
            break;

        case LEFT:
        	while (piece != null && piece.canMove(Direction.LEFT)) {
    	        piece.move(Direction.LEFT);
    	        cellsDropped++;
    	        checkPowerUpCollection();
    	    }
            break;

        case RIGHT:
        	while (piece != null && piece.canMove(Direction.RIGHT)) {
    	        piece.move(Direction.RIGHT);
    	        cellsDropped++;
    	        checkPowerUpCollection(); 
    	    }
            break;
		}
	    
	    score += cellsDropped * 2; // 2 points per hard drop cell
	    lockPiece(); // Lock the piece after a hard drop
	}

	
	
	public int getScore() {
	    return score;
	}

	
	
	public int getLevel() {
	    return level;
	}
	
	
	
	public PowerUpType getPowerupType() {
	    return activePowerUpType;		
	}	
	


    void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            System.out.println("Game paused.");
        } else {
            System.out.println("Game resumed.");
        }
    }

    

    boolean getPaused() {
        if (isPaused) {
            return true;
        } else {
            return false;
        }
    }


	// Clear the top rows to ensure the game isn't instantly over
	private void clearTopRows() {
	    for (int row = 0; row < 2; row++) { // Clear the first two rows
	        for (int col = 0; col < Grid.WIDTH; col++) {
	            grid.set(row, col, Grid.EMPTY);
	        }
	    }
	}

	
	
	private void validatePiecePosition() {
	    if (piece != null) {
	        Point[] pieceLocations = piece.getLocations();
	        for (Point point : pieceLocations) {
	            int x = (int) point.getX();
	            int y = (int) point.getY();

	            // If the piece overlaps an occupied cell, move it down
	            if (grid.isSet(y, x)) {
	                while (piece.canMove(Direction.DOWN)) {
	                    piece.move(Direction.DOWN);
	                }
	                break;
	            }
	        }
	    }
	}

	
	
	public void saveGame() {
		
	    pauseGame(); // Pause the game

	    // Define the bubbleswerve_saves directory in the user's home directory
	    File saveDirectory = new File(System.getProperty("user.home"), "bubbleswerve_saves");

	    // Ensure the directory exists
	    if (!saveDirectory.exists()) {
	        if (saveDirectory.mkdir()) {
	            System.out.println("Save directory created: " + saveDirectory.getAbsolutePath());
	        } else {
	            JOptionPane.showMessageDialog(null, "Failed to create save directory.", "Error", JOptionPane.ERROR_MESSAGE);
	            togglePause(); // Resume the game if directory creation fails
	            return;
	        }
	    }

	    // Create and configure the file chooser to use the bubbleswerve_saves directory
	    JFileChooser fileChooser = new JFileChooser(saveDirectory);
	    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PanelGame Save Files", "txt"));
	    fileChooser.setDialogTitle("Save Game");

	    int result = fileChooser.showSaveDialog(null);
	    if (result == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = fileChooser.getSelectedFile();
	        if (!selectedFile.getName().endsWith(".txt")) {
	            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
	        }

	        try (FileWriter writer = new FileWriter(selectedFile)) {
	            // Save score and level
	            writer.write("Score: " + score + "\n");
	            writer.write("Level: " + level + "\n");

	            // Save grid state with letters representing colors
	            writer.write("Grid:\n");
	            for (int row = 0; row < Grid.HEIGHT; row++) {
	                for (int col = 0; col < Grid.WIDTH; col++) {
	                    Color cellColor = grid.getColor(row, col);
	                    if (cellColor == null || cellColor.equals(Grid.EMPTY)) {
	                        writer.write("E"); // Empty cell
	                    } else if (cellColor.equals(Color.GRAY)) {
	                        writer.write("G"); // Gray color
	                    } else if (cellColor.equals(crimson)) {
	                        writer.write("R"); // Red color
	                    } else if (cellColor.equals(skyBlue)) {
	                        writer.write("B"); // Blue color
	                    } else if (cellColor.equals(limeGreen)) {
	                        writer.write("Y"); // Yellow color
	                    } else if (cellColor.equals(gold)) {
	                        writer.write("C"); // Teal color
	                    } else if (cellColor.equals(magenta)) {
	                        writer.write("L"); // Bright green color
	                    } else if (cellColor.equals(teal)) {
	                        writer.write("P"); // Purple color
	                    } else if (cellColor.equals(orangeRed)) {
	                        writer.write("Q"); // Purple color
	                    } else if (cellColor.equals(lavender)) {
	                        writer.write("W"); // Purple color
	                    } else if (cellColor.equals(deepPurple)) {
	                        writer.write("Z"); // Purple color
	                    } else {
	                        writer.write("U"); // Unknown color
	                    }
	                }
	                writer.write("\n");
	            }

	            System.out.println("Game saved to " + selectedFile.getAbsolutePath());
	        } catch (IOException e) {
	            JOptionPane.showMessageDialog(null, "Error saving the game: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    togglePause(); // Resume the game
	}

	
	
	public void loadGame() {
	    pauseGame(); // Pause the game
	    
	    // Define the bubbleswerve_saves directory in the user's home directory
	    File saveDirectory = new File(System.getProperty("user.home"), "bubbleswerve_saves");

	    // Ensure the directory exists
	    if (!saveDirectory.exists()) {
	        if (saveDirectory.mkdir()) {
	            System.out.println("Save directory created: " + saveDirectory.getAbsolutePath());
	        } else {
	            JOptionPane.showMessageDialog(null, "Failed to create save directory.", "Error", JOptionPane.ERROR_MESSAGE);
	            togglePause(); // Resume the game if directory creation fails
	            return;
	        }
	    }

	    // Create and configure the file chooser to use the bubbleswerve_saves directory
	    JFileChooser fileChooser = new JFileChooser(saveDirectory);
	    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PanelGame Save Files", "txt"));

	    int result = fileChooser.showOpenDialog(null);
	    if (result == JFileChooser.APPROVE_OPTION) {
	        File selectedFile = fileChooser.getSelectedFile();

	        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
	            // Load score and level
	            score = Integer.parseInt(reader.readLine().split(": ")[1]);
	            level = Integer.parseInt(reader.readLine().split(": ")[1]);

	            // Load grid state with letters representing colors
	            reader.readLine(); // Skip "Grid:" line
	            for (int row = 0; row < Grid.HEIGHT; row++) {
	                String line = reader.readLine();
	                for (int col = 0; col < Grid.WIDTH; col++) {
	                    char colorChar = line.charAt(col);
	                    switch (colorChar) {
	                        case 'E': grid.set(row, col, Grid.EMPTY); break; // Empty cell
	                        case 'G': grid.set(row, col, Color.GRAY); break; // Gray color
	                        case 'R': grid.set(row, col, crimson); break; // Red color
	                        case 'B': grid.set(row, col, skyBlue); break; // Blue color
	                        case 'Y': grid.set(row, col, limeGreen); break; // Yellow color
	                        case 'C': grid.set(row, col, gold); break; // Teal color
	                        case 'L': grid.set(row, col, magenta); break; // Bright green color
	                        case 'P': grid.set(row, col, teal); break; // Purple color
	                        case 'Q': grid.set(row, col, orangeRed); break; // Purple color
	                        case 'W': grid.set(row, col, lavender); break; // Purple color
	                        case 'Z': grid.set(row, col, deepPurple); break; // Purple color
	                        default: grid.set(row, col, Grid.EMPTY); break; // Default to empty
	                    }
	                }
	            }

	            // Clear any actively falling piece
	            piece = null;

	            // Clear active power-ups
	            clearPowerUps();

	            // Spawn a new piece
	            spawnNewPiece();
	            
	            engine.getPanelGame().resumeGame();
	            

	            System.out.println("Game Loaded! Score: " + score + ", Level: " + level);
	        } catch (IOException | NumberFormatException e) {
	            JOptionPane.showMessageDialog(null, "Error loading the game: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }

	    togglePause(); // Resume the game
	}



	private void clearPowerUps() {
	    powerupActive = false; // Deactivate any active power-up
	    powerupX = -1; // Reset power-up position
	    powerupY = -1;
	    activePowerUpType = null; // Clear the power-up type
	    System.out.println("Power-ups cleared.");
	}

	
}
