package bubbleswervegame;
import java.awt.Color;

/**
 * @author https://scott.rodeo/
 */

public class BubbloidJ5 extends AbstractBubbloid {

	public static int[] rowOffset = {-1,0,1,2,2,9};
	public static int[] colOffset = {0,0,0,0,-1,9};
	
	public BubbloidJ5(int initialRow, int initialColumn, Grid grid, Color color) {
		super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
	}
}
