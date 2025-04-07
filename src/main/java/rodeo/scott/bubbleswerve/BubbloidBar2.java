package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in the form of a small bar.
 * This piece consists of two bubbles positioned adjacent to each other.
 * 
 * The offsets specify the relative positions of the bubbles forming this piece.
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidBar2 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    // The piece occupies two rows, with the remaining positions marked as `9` (indicating no bubble).
    public static int[] rowOffset = {0, 0, 9, 9, 9, 9};

    // Column offsets for the bubbles that make up this piece.
    // The piece occupies two adjacent columns, with the second bubble offset to the left by one column.
    public static int[] colOffset = {0, -1, 9, 9, 9, 9};

    /**
     * Constructs a new BubbloidBar2 game piece.
     * This piece consists of two connected bubbles forming a horizontal bar.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidBar2(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
