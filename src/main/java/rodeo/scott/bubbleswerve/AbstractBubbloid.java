package rodeo.scott.bubbleswerve;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a Bubbloid, a game piece made up of multiple bubbles.
 * Provides functionality for positioning, moving, rotating, and rendering the piece.
 * 
 * @author https://scott.rodeo/
 */
public abstract class AbstractBubbloid {
    private static final long serialVersionUID = 1L;

    protected int row, col;  // Current position of the Bubbloid in the grid.

    public AbstractBubbloid() {
        this.row = 0;      // Default initial row.
        this.col = 0;      // Default initial column.
    }

    private boolean ableToMove;            // Indicates if this Bubbloid can move.
    protected List<Bubble> bubble;         // List of individual bubbles making up this piece.
    private Grid grid;                     // Reference to the game grid.

    /**
     * Constructs a new Bubbloid piece.
     * 
     * @param initialRow   The starting row position for the piece.
     * @param initialColumn The starting column position for the piece.
     * @param gridin       The game grid where the piece resides.
     * @param color        The color of the piece.
     * @param rowOffset    Array specifying row offsets for the bubbles.
     * @param colOffset    Array specifying column offsets for the bubbles.
     */
    public AbstractBubbloid(int initialRow, int initialColumn, Grid gridin, Color color, int[] rowOffset, int[] colOffset) {
        grid = gridin;
        bubble = new ArrayList<>();
        ableToMove = true;

        // Initialize bubbles based on offsets.
        for (int i = 0; i < rowOffset.length; i++) {
            if (rowOffset[i] != 9) { // Offset value of 9 indicates no bubble at this index.
                bubble.add(new Bubble(gridin, initialRow + rowOffset[i], initialColumn + colOffset[i], color, true));
            }
        }
    }

    /**
     * Retrieves a specific bubble from the Bubbloid.
     * 
     * @param i Index of the bubble.
     * @return The Bubble object at the specified index.
     */
    public Bubble getBubbleTest(int i) {
        return bubble.get(i);
    }

    /**
     * Retrieves the list of all bubbles making up the Bubbloid.
     * 
     * @return List of bubbles.
     */
    public List<Bubble> getBubbles() {
        return bubble;
    }

    /**
     * Draws the Bubbloid on the given Graphics context.
     * 
     * @param g The Graphics object used for rendering.
     */
    public void draw(Graphics g) {
        for (Bubble sq : bubble) {
            sq.draw(g);
        }
    }

    /**
     * Returns an array of the rotated coordinates for the bubbles.
     * The rotation is centered on the second bubble (index 1) in the list.
     * 
     * @return A 2D array containing the rotated x, y coordinates.
     */
    public int[][] getRotatedCoords() {
        int originX = bubble.get(1).getCol(); // Pivot column.
        int originY = bubble.get(1).getRow(); // Pivot row.
        int[][] rotatedCoords = new int[bubble.size()][2];

        for (int i = 0; i < bubble.size(); i++) {
            int squareCartX = bubble.get(i).getCol() - originX; // Cartesian x.
            int squareCartY = bubble.get(i).getRow() - originY; // Cartesian y.

            // Apply rotation transformation.
            rotatedCoords[i][0] = originX - squareCartY; // New x.
            rotatedCoords[i][1] = originY + squareCartX; // New y.
        }

        return rotatedCoords;
    }

    /**
     * Checks if the Bubbloid intersects with a specific grid position.
     * 
     * @param x Column position to check.
     * @param y Row position to check.
     * @return True if any bubble overlaps the given position, false otherwise.
     */
    public boolean intersects(int x, int y) {
        for (Bubble bubble : this.bubble) {
            if (bubble.getCol() == x && bubble.getRow() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Rotates the Bubbloid, if the rotation is valid.
     * 
     * @param clockDirection Rotation direction (0 for clockwise).
     */
    public void rotate(int clockDirection) {
        int[][] coords = getRotatedCoords(); // Calculate new coordinates.
        if (canRotate(coords)) { // Check if rotation is valid.
            for (int i = 0; i < bubble.size(); i++) {
                bubble.get(i).rotate(coords[i][0], coords[i][1]);
            }
        }
    }

    /**
     * Moves the Bubbloid in a given direction, if possible.
     * Freezes the Bubbloid if it cannot move downward.
     * 
     * @param direction The direction to move.
     */
    public void move(Direction direction) {
        if (canMove(direction)) {
            for (Bubble sq : bubble) {
                sq.move(direction);
            }
        } else if (direction == Direction.DOWN) {
            ableToMove = false;
        }
    }

    /**
     * Retrieves the grid coordinates occupied by the Bubbloid.
     * 
     * @return Array of Points representing occupied grid cells.
     */
    public Point[] getLocations() {
        Point[] points = new Point[bubble.size()];
        for (int i = 0; i < bubble.size(); i++) {
            points[i] = new Point(bubble.get(i).getCol(), bubble.get(i).getRow());
        }
        return points;
    }

    /**
     * Gets the color of the Bubbloid.
     * Assumes all bubbles share the same color.
     * 
     * @return The color of the Bubbloid.
     */
    public Color getColor() {
        return bubble.get(0).getColor();
    }

    /**
     * Determines if the Bubbloid can move in the specified direction.
     * 
     * @param direction The direction to check.
     * @return True if the move is valid, false otherwise.
     */
    public boolean canMove(Direction direction) {
        for (Bubble square : getBubbles()) {
            int newRow = square.getRow();
            int newCol = square.getCol();

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

            if (!grid.isValidAndEmpty(newRow, newCol)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the Bubbloid can rotate to the specified coordinates.
     * 
     * @param coords The target coordinates for rotation.
     * @return True if the rotation is valid, false otherwise.
     */
    public boolean canRotate(int[][] coords) {
        if (coords.length != bubble.size()) {
            throw new IllegalArgumentException("Mismatch between coordinates and bubbles.");
        }

        for (int i = 0; i < coords.length; i++) {
            int x = coords[i][0];
            int y = coords[i][1];

            if (x < 0 || x >= grid.WIDTH || y < 0 || y >= grid.HEIGHT) {
                return false;
            }

            if (grid.isSet(y, x)) {
                return false;
            }
        }
        return true;
    }
}
