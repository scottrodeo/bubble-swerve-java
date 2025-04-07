package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in the form of a horizontal bar.
 * This piece consists of three bubbles arranged in a straight line horizontally.
 * 
 * The offsets specify the relative positions of the bubbles forming this piece.
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidBar3 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    // All three bubbles are in the same row, as indicated by `0`.
    // The remaining positions are marked as `9`, indicating no bubble.
    public static int[] rowOffset = {0, 0, 0, 9, 9, 9};

    // Column offsets for the bubbles that make up this piece.
    // The bubbles are aligned horizontally, with positions offset by -1, 0, and 1.
    public static int[] colOffset = {-1, 0, 1, 9, 9, 9};

    /**
     * Constructs a new BubbloidBar3 game piece.
     * This piece consists of three connected bubbles forming a horizontal bar.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidBar3(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
