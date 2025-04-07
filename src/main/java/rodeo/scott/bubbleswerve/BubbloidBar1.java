package rodeo.scott.bubbleswerve;
import java.awt.Color;

/**
 * Represents a specific type of Bubbloid game piece in the form of a single block.
 * Inherits from the AbstractBubbloid class.
 * 
 * The piece consists of a single bubble located at the initial position,
 * with no additional bubbles (indicated by offsets of `9`, which mean no bubble in that position).
 * 
 * @author https://scott.rodeo/
 */
public class BubbloidBar1 extends AbstractBubbloid {

    // Row offsets for the bubbles that make up this piece.
    public static int[] rowOffset = {0, 9, 9, 9, 9, 9};

    // Column offsets for the bubbles that make up this piece.
    public static int[] colOffset = {0, 9, 9, 9, 9, 9};

    /**
     * Constructs a new BubbloidBar1 game piece.
     * This piece is a single block located at the given initial position.
     * 
     * @param initialRow    The initial row position for the piece.
     * @param initialColumn The initial column position for the piece.
     * @param grid          The game grid where the piece resides.
     * @param color         The color of the piece.
     */
    public BubbloidBar1(int initialRow, int initialColumn, Grid grid, Color color) {
        // Pass the initial position, grid, color, and offsets to the superclass constructor.
        super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
    }
}
