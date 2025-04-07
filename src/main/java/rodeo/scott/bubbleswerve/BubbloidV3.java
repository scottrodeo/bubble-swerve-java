package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in the shape of a "V".
 * This piece consists of three bubbles arranged with one in the center and two extending diagonally.
 * 
 * The offsets specify the relative positions of the bubbles forming this piece.
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidV3 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    // The bubbles are arranged to form a "V" shape, with two bubbles in the same row
    // and one extending below the center bubble.
    public static int[] rowOffset = {0, 0, 1, 9, 9, 9};

    // Column offsets for the bubbles that make up this piece.
    // The left and center bubbles are in the same row, while the bottom bubble is in the same column as the center.
    public static int[] colOffset = {-1, 0, 0, 9, 9, 9};

    /**
     * Constructs a new BubbloidV3 game piece.
     * This piece consists of three connected bubbles arranged in a "V" shape.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidV3(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
