package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in a disconnected "V" shape.
 * This piece consists of two bubbles that are not directly connected but form a "V" pattern.
 * 
 * The offsets specify the relative positions of the bubbles forming this piece.
 * 
 * Note: The `9` in the offsets indicates no bubble at that position.
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidVDiscon2 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    // One bubble is at the starting row (`0`), and the other is one row below (`1`).
    public static int[] rowOffset = {0, 9, 1, 9, 9, 9};

    // Column offsets for the bubbles that make up this piece.
    // One bubble is to the left of the initial column (`-1`), and the other is aligned with the starting column (`0`).
    public static int[] colOffset = {-1, 9, 0, 9, 9, 9};

    /**
     * Constructs a new BubbloidVDiscon2 game piece.
     * This piece consists of two disconnected bubbles forming a "V" shape.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidVDiscon2(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
