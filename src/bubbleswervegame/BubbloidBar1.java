package bubbleswervegame;
import java.awt.Color;

/**
 * @author https://scott.rodeo/
 */
 
public class BubbloidBar1 extends AbstractBubbloid{

	public static int[] rowOffset = {0,9,9,9,9,9};
	public static int[] colOffset = {0,9,9,9,9,9};
	
	public BubbloidBar1(int initialRow, int initialColumn, Grid grid, Color color) {
		super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
	}
}
