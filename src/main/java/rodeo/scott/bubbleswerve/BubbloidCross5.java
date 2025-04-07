package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in the shape of a cross.
 * This piece consists of five bubbles: one at the center and one extending in each cardinal direction.
 * 
 * The offsets specify the relative positions of the bubbles forming this piece.
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidCross5 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    // The center bubble is at (0, 0), with other bubbles extending vertically (up and down) and horizontally (left and right).
    // The remaining positions are marked as `9`, indicating no bubble.
    public static int[] rowOffset = {0, 0, 0, 1, -1, 9};

    // Column offsets for the bubbles that make up this piece.
    // The center bubble is at (0, 0), with other bubbles extending to the left and right horizontally, and one at the center.
    public static int[] colOffset = {-1, 0, 1, 0, 0, 9};

    /**
     * Constructs a new BubbloidCross5 game piece.
     * This piece consists of five connected bubbles arranged in a cross shape.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidCross5(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
