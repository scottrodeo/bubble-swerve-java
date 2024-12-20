package bubbleswervegame;
import java.awt.Color;

/**
 * @author https://scott.rodeo/
 */

public class BubbloidRectangle6 extends AbstractBubbloid {

	public static int[] rowOffset = {-1,0,1,-1,0,1};
	public static int[] colOffset = {0,0,0,1,1,1};
	
	public BubbloidRectangle6(int initialRow, int initialColumn, Grid grid, Color color) {
		super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
	}
}
