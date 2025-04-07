package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in the shape of a rectangle.
 * This piece consists of six bubbles arranged in a 3x2 rectangular grid pattern.
 * 
 * The offsets specify the relative positions of the bubbles forming this piece.
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidRectangle6 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    // The bubbles are arranged in three rows.
    public static int[] rowOffset = {-1, 0, 1, -1, 0, 1};

    // Column offsets for the bubbles that make up this piece.
    // The bubbles are arranged in two columns.
    public static int[] colOffset = {0, 0, 0, 1, 1, 1};

    /**
     * Constructs a new BubbloidRectangle6 game piece.
     * This piece consists of six connected bubbles arranged in a rectangular grid pattern.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidRectangle6(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
