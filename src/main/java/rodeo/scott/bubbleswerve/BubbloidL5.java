package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in the shape of an "L".
 * This piece consists of five bubbles arranged vertically, with the bottommost bubble extending one column to the right.
 * 
 * The offsets specify the relative positions of the bubbles forming this piece.
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidL5 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    // The bubbles are primarily arranged vertically, with the last bubble extending to the right.
    public static int[] rowOffset = {-1, 0, 1, 2, 2, 9};

    // Column offsets for the bubbles that make up this piece.
    // All bubbles except the last are aligned in the same column.
    // The last bubble extends one column to the right.
    public static int[] colOffset = {0, 0, 0, 0, 1, 9};

    /**
     * Constructs a new BubbloidL5 game piece.
     * This piece consists of five connected bubbles arranged in an "L" shape.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidL5(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
