package rodeo.scott.bubbleswerve;
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
 * Represents the core logic of the Bubbleswerve game, including grid management, 
 * piece movement, game state updates, power-up handling, and scoring.
 * 
 * @author https://scott.rodeo/
 */
public class Game {

	public Grid grid; // The game grid for storing pieces and empty cells.
	public AbstractBubbloid piece; // The currently active game piece.
	public Engine engine; // Reference to the Engine instance managing the game.
	private Timer timer; // Timer for managing periodic game updates.
	public boolean gameIsOver; // Flag indicating if the game has ended.
	public boolean isPaused = false; // Indicates if the game is paused.
	public boolean powerupActive = false; // Flag for active power-up.
	private int powerupX; // X-coordinate of the active power-up.
	private int powerupY; // Y-coordinate of the active power-up.
	private int powerupTickCounter = 0; // Tracks ticks since the power-up spawned.	

	private int score = 0;
	private int level = 1;
	private int linesCleared = 0;
	
	public PowerUpType activePowerUpType; // The currently active power-up type.
	
	// Colors for additional visual elements.
    public static Color b1 = new Color(175, 18, 202);
    public static Color b2 = new Color(101, 216, 246);
    public static Color b3 = new Color(74, 125, 255);
    public static Color b4 = new Color(79, 255, 254);
    public static Color b5 = new Color(230, 56, 174);
    public static Color b6 = new Color(249, 116, 122);
    public static Color b7 = new Color(45, 50, 116);
    public static Color b8 = new Color(99, 32, 178);
    public static Color b9 = new Color(65, 84, 203);	
	
    /**
     * Enum for the possible grid orientations.
     */
    public enum Orientation {
        DOWN, LEFT, RIGHT, UP
    }
    Orientation currentOrientation = Orientation.DOWN; // Default orientation
	

    /**
     * Enum for different types of power-ups in the game.
     */
    public enum PowerUpType {
        CLEAR_LINE, // Clears a line.
        SLOW_SPEED, // Temporarily slows game speed.
        EXTRA_POINTS, // Adds extra points.
        EXTRA_LIFE // Grants an extra life.
    }

    
    /**
     * Constructor for initializing the Game instance.
     * 
     * @param engine The engine managing this game instance.
     */	
	public Game(Engine engine) {
		this.engine=engine;
        this.grid = new Grid(engine);
	    gameIsOver = false;
	    this.score = 0;
	    this.level = 1;
	    this.linesCleared = 0;
		initializeGame();
	}
	    
	
	/**
	 * Initializes the game by starting the timer and preparing the first game piece.
	 */
	private void initializeGame() {  
	    timerStart(); // Start the game timer.
	    gameStart();  // Start the game logic.
	}

	/**
	 * Starts the game by spawning the first piece.
	 */
	void gameStart() {
	    spawnNewPiece(); // Generate the first game piece.
	}

	/**
	 * Restarts the game by resetting the board and spawning a new piece.
	 */
	void gameRestart() {
	    gameBoardReset(); // Clear and reset the game board.
	    spawnNewPiece();  // Spawn a new piece to begin the game.
	}

	/**
	 * Starts or restarts the timer for periodic game updates.
	 */
	void timerStart() {
	    if (timer == null) {
	        // Create a new timer that triggers every 500 milliseconds.
	        timer = new Timer(500, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if (!isPaused) { // Only update the game if not paused.
	                    if (isGameOver()) { // Stop the game if it's over.
	                        timer.stop();
	                        GameOverScreen();
	                    } else {
	                        updateState(); // Update the game state.
	                        engine.getPanelGame().repaint(); // Refresh the game display.
	                    }
	                }
	            }
	        });
	        timer.start(); // Start the timer.
	    } else {
	        timer.restart(); // Restart the timer if it already exists.
	    }
	}

	/**
	 * Resets the game board to its initial state.
	 * Clears the grid, resets the score, level, and orientation, and restarts the timer.
	 */
	public void gameBoardReset() {
	    System.out.println("gameBoardReset()");

	    this.gameIsOver = false; // Reset the game-over flag.
	    
	    this.grid.HEIGHT = 22; // Set the grid height.
	    this.grid.WIDTH = 12;  // Set the grid width.
	    this.grid = new Grid(engine); // Reinitialize the grid.
	    this.score = 0; // Reset the score.
	    this.level = 1; // Reset the level.
	    this.linesCleared = 0; // Reset the cleared lines counter.

	    currentOrientation = Orientation.DOWN; // Reset the grid orientation to default.
	    timerStart(); // Restart the game timer.
	}

    
		
	/**
	 * Draws the game state on the provided Graphics context.
	 *
	 * @param g The Graphics object used to render the game grid and active piece.
	 */
	public void draw(Graphics g) {
	    grid.draw(g); // Draw the grid and all locked pieces.
	    if (piece != null) {
	        piece.draw(g); // Draw the currently active piece.
	    }
	}

	/**
	 * Moves the active piece in the specified direction.
	 *
	 * @param direction The direction to move the piece (e.g., LEFT, RIGHT, DOWN, DROP).
	 */
	public void movePiece(Direction direction) {
	    if (piece != null) {
	        if (direction == Direction.DROP) {
	            // Perform a hard drop, moving the piece straight down until it can't move further.
	            while (piece.canMove(Direction.DOWN)) {
	                piece.move(Direction.DOWN);
	            }
	        } else {
	            piece.move(direction); // Move the piece in the specified direction.
	        }
	    }

	    updatePiece(); // Update the game state after moving the piece.
	    this.engine.getPanelGame().update(); // Refresh the game panel.
	    grid.checkRows(); // Check for and handle full rows.
	}

	/**
	 * Handles player input to move the active piece.
	 *
	 * @param direction The direction to move the piece.
	 */
	public void handleMove(Direction direction) {
	    if (isGameOver()) {
	        GameOverScreen(); // Display the game-over screen if the game has ended.
	    } else {
	        movePiece(direction); // Move the piece in the specified direction.
	    }
	}

	/**
	 * Displays the game-over screen and stops the game.
	 */
	public void GameOverScreen() {
	    System.out.println("Game Over!"); // Log the game-over message to the console.
	}

	/**
	 * Handles player input to rotate the active piece.
	 *
	 * @param clockDirection The rotation direction (e.g., clockwise or counterclockwise).
	 */
	public void handleRotate(int clockDirection) {
	    if (isGameOver()) {
	        GameOverScreen(); // Display the game-over screen if the game has ended.
	    } else {
	        rotatePiece(clockDirection); // Rotate the active piece in the specified direction.
	    }
	}

	/**
	 * Removes the currently active piece from the grid.
	 */
	private void removeActivePiece() {
	    if (piece != null) {
	        // Clear the bubbles of the current piece from the grid.
	        for (Bubble square : piece.getBubbles()) {
	            grid.clearCell(square.getRow(), square.getCol());
	        }

	        // Remove the reference to the active piece.
	        piece = null;
	    }
	}


	
	/**
	 * Rotates the entire game board clockwise and updates the grid orientation.
	 */
	public void rotateBoard() {
	    grid.rotateBoardClockwise(); // Rotate the grid's data.

	    // Update the current orientation of the board.
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

	/**
	 * Rotates the game board clockwise, removes the active piece, and spawns a new one.
	 * Also refreshes the game display.
	 */
	public void rotateBoardClockwise() {
	    grid.rotateBoardClockwise(); // Rotate the grid data clockwise.

	    // Update the board's orientation after rotation.
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

	    removeActivePiece(); // Remove the active piece from the grid.
	    spawnNewPiece(); // Spawn a new piece to match the new grid orientation.
	    engine.getPanelGame().update(); // Refresh the game panel to reflect changes.
	    System.out.println("Board rotated to: " + currentOrientation); // Log the new orientation.
	}

	/**
	 * Updates the player's score and level based on the number of cleared lines.
	 *
	 * @param clearedLines The number of lines cleared during the last move.
	 */
	private void updateScore(int clearedLines) {
	    switch (clearedLines) {
	        case 1:
	            score += 100 * level;
	            break;
	        case 2:
	            score += 300 * level;
	            break;
	        case 3:
	            score += 500 * level;
	            break;
	        case 4:
	            score += 800 * level;
	            break;
	    }

	    level = (score / 1000) + 1; // Increase the level every 1000 points.
	    System.out.println("Current Level: " + level); // Log the updated level.
	}

	/**
	 * Updates the game's state during each tick of the game timer.
	 */
	public void updateState() {
	    gameTick(); // Perform actions for each game tick.
	}

	/**
	 * Deactivates the currently active power-up after its duration ends.
	 */
	private void deactivatePowerUp() {
	    powerupActive = false; // Mark the power-up as inactive.
	    powerupTickCounter = 0; // Reset the tick counter for power-ups.
	    System.out.println("Power-up disappeared after 10 ticks."); // Log power-up expiration.
	}

	/**
	 * Executes the main game loop for each tick, applying gravity and handling power-ups.
	 */
	private void gameTick() {
	    applyGravity(); // Move the active piece downward.

	    // Handle the countdown for an active power-up.
	    if (powerupActive) {
	        powerupTickCounter++;
	        if (powerupTickCounter >= 36) { // Deactivate the power-up after 36 ticks.
	            deactivatePowerUp();
	        }
	    }

	    engine.getPanelGame().update(); // Refresh the game panel.
	    checkPowerUpCollection(); // Check if a power-up has been collected.
	}

	/**
	 * Pauses the game and prevents further updates until resumed.
	 */
	void pauseGame() {
	    isPaused = true;
	    System.out.println("Game paused."); // Log the paused state.
	}

	/**
	 * Checks whether a power-up is currently active in the game.
	 *
	 * @return True if a power-up is active, false otherwise.
	 */
	public boolean isPowerupActive() {
	    return powerupActive;
	}

	/**
	 * Locks the active piece into the grid and clears full rows. 
	 * Spawns a new piece or ends the game if necessary.
	 */
	private void lockPiece() {
	    if (piece != null) {
	        Point[] pieceLocations = piece.getLocations();
	        Color pieceColor = piece.getColor();

	        // Lock each part of the piece into the grid.
	        for (Point point : pieceLocations) {
	            grid.set((int) point.getY(), (int) point.getX(), pieceColor);
	        }

	        piece = null; // Clear the current piece reference.

	        // Clear full rows and update the score.
	        int clearedLines = grid.clearFullRows(currentOrientation);
	        updateScore(clearedLines);

	        if (isGameOver()) {
	            GameOverScreen(); // End the game if the board is full.
	        } else {
	            rotateBoard(); // Rotate the board as part of the game mechanics.
	            spawnNewPiece(); // Spawn a new active piece.
	        }
	    }
	}

	/**
	 * Retrieves the x-coordinate of the active power-up.
	 *
	 * @return The x-coordinate of the power-up's position.
	 */
	public int getpowerupX() {
	    return powerupX;
	}


	
	/**
	 * Retrieves the y-coordinate of the active power-up.
	 *
	 * @return The y-coordinate of the power-up's position.
	 */
	public int getpowerupY() {
	    return powerupY;
	}

	/**
	 * Spawns a new game piece at the appropriate position based on the current orientation.
	 */
	private void spawnNewPiece() {
	    int initialRow = 1; // Default starting row for a new piece.
	    int initialColumn = grid.WIDTH / 2 - 1; // Default centered column for a new piece.

	    // Adjust the starting position based on the board's current orientation.
	    switch (currentOrientation) {
	        case DOWN:
	            initialRow = 2;
	            initialColumn = grid.WIDTH / 2 - 1;
	            break;
	        case UP:
	            initialRow = grid.HEIGHT - 2;
	            initialColumn = grid.WIDTH / 2 - 1;
	            break;
	        case LEFT:
	            initialRow = grid.HEIGHT / 2 - 1;
	            initialColumn = grid.WIDTH - 2;
	            break;
	        case RIGHT:
	            initialRow = grid.HEIGHT / 2 - 1;
	            initialColumn = 1;
	            break;
	    }

	    // Randomly select and instantiate a new game piece.
	    int random = (int) (Math.random() * 9 + 1);
	    switch (random) {
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

	/**
	 * Applies gravity to the active piece, moving it downward based on the current orientation.
	 */
	public void applyGravity() {
	    checkPowerUpCollection(); // Check for any power-up collection.

	    if (piece != null) {
	        // Move the piece in the direction corresponding to the current orientation.
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
	    engine.getPanelGame().update(); // Refresh the game panel.
	}

	/**
	 * Spawns a new power-up at a random, unoccupied position on the grid.
	 */
	public void spawnPowerUp() {
	    if (!powerupActive && Math.random() < 0.9) { // 90% chance to spawn a power-up.
	        int attempts = 0; // Track the number of attempts to find a free square.
	        final int maxAttempts = 100; // Limit the number of attempts.

	        // Find a random unoccupied position for the power-up.
	        do {
	            powerupX = (int) (Math.random() * (grid.WIDTH - 1)) + 1;
	            powerupY = (int) (Math.random() * (grid.HEIGHT - 4)) + 4;
	            attempts++;
	        } while (grid.isSet(powerupY, powerupX) && attempts < maxAttempts);

	        if (attempts >= maxAttempts) {
	            System.out.println("Failed to find an empty square for power-up after " + maxAttempts + " attempts.");
	            return;
	        }

	        // Randomly select a power-up type.
	        PowerUpType[] powerUpTypes = PowerUpType.values();
	        int randomIndex = (int) (Math.random() * powerUpTypes.length);

	        activePowerUpType = powerUpTypes[randomIndex];
	        powerupActive = true;
	        powerupTickCounter = 0; // Reset the power-up tick counter.
	    }
	}

	/**
	 * Activates the current power-up, applying its effect to the game.
	 */
	private void activatePowerUp() {
	    switch (activePowerUpType) {
	        case CLEAR_LINE:
	            grid.deleteRow(19); // Clears the bottom row of the grid.
	            break;
	        case SLOW_SPEED:
	            // Implementation for slowing the game speed goes here.
	            break;
	        case EXTRA_POINTS:
	            score += 500; // Award extra points.
	            break;
	        case EXTRA_LIFE:
	            // Implementation for granting an extra life goes here.
	            break;
	        default:
	            System.out.println("Unknown power-up type: " + activePowerUpType);
	    }
	    System.out.println("Activated power-up: " + activePowerUpType); // Log the activation.
	}

	/**
	 * Checks if the active piece overlaps with the power-up and activates it if collected.
	 */
	void checkPowerUpCollection() {
	    if (!powerupActive || piece == null) return;

	    Point[] pieceLocations = piece.getLocations();

	    for (Point block : pieceLocations) {
	        int blockX = (int) block.getY();
	        int blockY = (int) block.getX();

	        // Check if the piece overlaps with the power-up
	        if (blockX == powerupX && blockY == powerupY) {
	            activatePowerUp(); 
	            powerupActive = false; // Deactivate the power-up after collection
	            //System.out.println("Power-up collected at: (" + powerupX + ", " + powerupY + ")");
	            break;
	        }
	    }
	}

	/**
	 * Determines whether the game is over based on the grid's state.
	 *
	 * @return True if the game is over; otherwise, false.
	 */
	public boolean isGameOver() {
	    switch (currentOrientation) {
	        case DOWN: // Check the top row
	            for (int col = 0; col < grid.WIDTH; col++) {
	                if (!grid.isValidAndEmpty(0, col)) {
	                    return true; // Top row filled
	                }
	            }
	            break;
	        case UP: // Check the bottom row
	            for (int col = 0; col < grid.WIDTH; col++) {
	                if (!grid.isValidAndEmpty(grid.HEIGHT - 1, col)) {
	                    return true; // Bottom row filled
	                }
	            }
	            break;
	        case LEFT: // Check the rightmost column
	            for (int row = 0; row < grid.HEIGHT; row++) {
	                if (!grid.isValidAndEmpty(row, grid.WIDTH - 1)) {
	                    return true; // Right column filled
	                }
	            }
	            break;
	        case RIGHT: // Check the leftmost column
	            for (int row = 0; row < grid.HEIGHT; row++) {
	                if (!grid.isValidAndEmpty(row, 0)) {
	                    return true; // Left column filled
	                }
	            }
	            break;
	    }
	    return false;
	}

	/**
	 * Rotates the active piece in the specified direction.
	 *
	 * @param clockDirection The direction to rotate the piece (clockwise or counterclockwise).
	 */
	public void rotatePiece(int clockDirection) {
	    piece.rotate(clockDirection);
	}

	/**
	 * Updates the game state by spawning a new piece if needed or ending the game if it's over.
	 */
	private void updatePiece() {
	    if (piece == null) {
	        if (isGameOver()) {
	            GameOverScreen();
	        } else {
	            // Spawn a new piece
	            spawnNewPiece();
	        }
	    }
	}

	/**
	 * Performs a soft drop of the active piece, moving it in the current orientation's direction.
	 */
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


	/**
	 * Performs a hard drop, moving the active piece as far as possible in the current orientation.
	 * Awards points based on the number of cells dropped and locks the piece in place.
	 */
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

	/**
	 * Retrieves the current game score.
	 *
	 * @return The current score.
	 */
	public int getScore() {
	    return score;
	}

	/**
	 * Retrieves the current game level.
	 *
	 * @return The current level.
	 */
	public int getLevel() {
	    return level;
	}

	/**
	 * Retrieves the currently active power-up type.
	 *
	 * @return The active PowerUpType, or null if no power-up is active.
	 */
	public PowerUpType getPowerupType() {
	    return activePowerUpType;
	}

	/**
	 * Toggles the game's paused state. If paused, the game stops updating.
	 * Prints a message indicating whether the game is paused or resumed.
	 */
	void togglePause() {
	    isPaused = !isPaused;

	    if (isPaused) {
	        System.out.println("Game paused.");
	    } else {
	        System.out.println("Game resumed.");
	    }
	}

	/**
	 * Checks if the game is currently paused.
	 *
	 * @return True if the game is paused, otherwise false.
	 */
	boolean getPaused() {
	    return isPaused;
	}


	
	
}
