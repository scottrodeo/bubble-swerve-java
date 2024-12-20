package bubbleswervegame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author https://scott.rodeo/
 */

public abstract class AbstractBubbloid {
    private static final long serialVersionUID = 1L;

    protected int[][] shape; // The shape of the Bubbloid
    protected int row, col;  // Current position of the Bubbloid

    public AbstractBubbloid() {
        this.shape = null; // Default to null
        this.row = 0;      // Default position
        this.col = 0;
    }

	private boolean ableToMove;					// Can this Bubbloid move
	protected List<Bubble> bubble;				// The bubbles that make up this piece
	private Grid grid;                          // Grid object reference
	

	
	/**
	 * 
	 * @param r         Row location for this piece
	 * @param c         Column location for this piece
	 * @param g         Grid for this game piece
	 * 
	 */
	public AbstractBubbloid(int initialRow, int initialColumn, Grid gridin, Color color, int[] rowOffset, int[] colOffset) {
		
		grid = gridin;
		bubble = new ArrayList<>();

		ableToMove = true;
		
		// Create the individual bubbles making up the bubbloid
		
		if (rowOffset[0] != 9) {		
			bubble.add(new Bubble(gridin, initialRow + rowOffset[0], initialColumn + colOffset[0], color, true));
		}
		
		if (rowOffset[1] != 9) {
			bubble.add(new Bubble(gridin, initialRow + rowOffset[1], initialColumn + colOffset[1], color, true));
		}
		
		if (rowOffset[2] != 9) {
			bubble.add(new Bubble(gridin, initialRow + rowOffset[2], initialColumn + colOffset[2], color, true));
		}
		
		if (rowOffset[3] != 9) {
			bubble.add(new Bubble(gridin, initialRow + rowOffset[3], initialColumn + colOffset[3], color, true));
		}	
		
		if (rowOffset[4] != 9) {
			bubble.add(new Bubble(gridin, initialRow + rowOffset[4], initialColumn + colOffset[4], color, true));
		}	
		
		if (rowOffset[5] != 9) {
			bubble.add(new Bubble(gridin, initialRow + rowOffset[5], initialColumn + colOffset[5], color, true));
		}	
	}
	
	

	public Bubble getBubbleTest(int i) {
		return bubble.get(i);

	}
	
	
	public List<Bubble> getBubbles() {
	    return bubble; // Returns the list of bubbles
	}
	
	
	/**
	 * Draws the piece on the given Graphics context
	 */
	public void draw(Graphics g) {
	    for (Bubble sq : bubble) {
	        sq.draw(g); // Call draw method on each square
	    }
	}


	

	
	private int[][] getCounterclockwiseCoords() {
	    int[][] newCoords = new int[bubble.size()][2]; // Use square.size() for dynamic list
	    for (int i = 0; i < bubble.size(); i++) {
	        // Counterclockwise transformation: (-y, x)
	        newCoords[i][0] = -bubble.get(i).getCol(); // Access using get()
	        newCoords[i][1] = bubble.get(i).getRow(); // Access using get()
	    }
	    return newCoords;
	}

	
	
	/**
	 * Returns an array of the rotated x,y coordinates
	 */
	public int[][] getRotatedCoords() {
	    int originX = bubble.get(1).getCol(); // Use get(1) for the pivot square
	    int originY = bubble.get(1).getRow();
	    int[][] rotatedCoords = new int[bubble.size()][2]; // Use square.size() for dynamic size

	    for (int i = 0; i < bubble.size(); i++) {
	        int squareCartX = bubble.get(i).getCol() - originX; // Cartesian x
	        int squareCartY = bubble.get(i).getRow() - originY; // Cartesian y

	        // Perform rotation
	        rotatedCoords[i][0] = originX - squareCartY; // x' = originX - y
	        rotatedCoords[i][1] = originY + squareCartX; // y' = originY + x
	    }

	    return rotatedCoords;
	}



	public boolean intersects(int x, int y) {
	    for (Bubble bubble : this.bubble) { // Assuming each piece has an array of bubbles
	        if (bubble.getCol() == x && bubble.getRow() == y) {
	            return true; // A square overlaps with the given position
	        }
	    }
	    return false; // No overlap
	}

	
	/**
	 * Rotate function
	 */
	public void rotate(int clockDirection) {
	    int[][] coords = getRotatedCoords(); // Get the new rotated coordinates

	    // Check if the rotation is valid
	    if (canRotate(coords)) {
	        for (int i = 0; i < bubble.size(); i++) { // Iterate using square.size()
	            bubble.get(i).rotate(coords[i][0], coords[i][1]); // Access list elements with get()
	        }
	    }
	}

	
	public void rotateCounterclockwise() {
	    int[][] coords = getCounterclockwiseCoords(); // Get counterclockwise rotation coordinates
	    
	    if (canRotate(coords)) {
	        for (int i = 0; i < coords.length; i++) {
	            bubble.get(i).rotate(coords[i][0], coords[i][1]); // Apply rotation
	        }
	    }
	}
	
	
	/**
	 * Moves the piece if possible Freeze the piece if it cannot move down
	 * anymore
	 * 
	 * @param direction
	 *            the direction to move
	 */
	public void move(Direction direction) {
	    if (canMove(direction)) {
	        // Iterate over all bubbles in the list
	        for (Bubble sq : bubble) {
	            sq.move(direction); // Move each square in the specified direction
	        }
	    }
	    // If unable to move, check if it's because the piece is at the bottom
	    else if (direction == Direction.DOWN) {
	        ableToMove = false;
	    }
	}

	/**
	 * Returns the (row,col) grid coordinates occupied by this Piece
	 * 
	 * @return an Array of (row,col) Points
	 */
	public Point[] getLocations() {
	    Point[] points = new Point[bubble.size()]; // Use the actual size of the list

	    for (int i = 0; i < bubble.size(); i++) { // Loop through all elements in the list
	        points[i] = new Point(bubble.get(i).getCol(), bubble.get(i).getRow());
	    }

	    return points;
	}


	/**
	 * Return the color of this piece
	 */
	public Color getColor() {
	    // All bubbles of this piece have the same color
	    return bubble.get(0).getColor(); // Access the first square using get(0)
	}


	
	/**
	 * Returns if this piece can move in the given direction
	 */
	public boolean canMove(Direction direction) {
	    for (Bubble square : getBubbles()) {
	        int newRow = square.getRow();
	        int newCol = square.getCol();

	        // Traditional switch-case syntax
	        switch (direction) {
	            case DOWN:
	                newRow++;
	                break;
	            case UP:
	                newRow--;
	                break;
	            case LEFT:
	                newCol--;
	                break;
	            case RIGHT:
	                newCol++;
	                break;
	        }

	        // Check if the new position is valid
	        if (!grid.isValidAndEmpty(newRow, newCol)) {
	            return false; // Blocked in this direction
	        }
	    }
	    return true;
	}


	
	/**
	 * Returns if this piece can rotate to the given coords
	 * array 0-4 | array 0:x, 1:y
	 */
	public boolean canRotate(int[][] coords) {
	    if (coords.length != bubble.size()) {
	        // Ensure that the coords array matches the size of the square list
	        throw new IllegalArgumentException("Mismatch between coordinates and bubbles.");
	    }

	    for (int i = 0; i < coords.length; i++) { // Use coords.length instead of PIECE_COUNT
	        int x = coords[i][0];
	        int y = coords[i][1];

	        // Check boundaries
	        if (x < 0 || x >= Grid.WIDTH || y < 0 || y >= Grid.HEIGHT) {
	            return false; // Out of bounds
	        }

	        // Check for collisions
	        if (grid.isSet(y, x)) {
	            return false; // Collision detected
	        }
	    }
	    return true; // Rotation is valid
	}



	
	
}
