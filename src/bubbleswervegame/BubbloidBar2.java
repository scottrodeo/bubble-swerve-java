package bubbleswervegame;
import java.awt.Color;

/**
 * @author https://scott.rodeo/
 */

public class BubbloidBar2 extends AbstractBubbloid{

	public static int[] rowOffset = {0,0,9,9,9,9};
	public static int[] colOffset = {0,-1,9,9,9,9};
	
	public BubbloidBar2(int initialRow, int initialColumn, Grid grid, Color color) {
		super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
	}
}
