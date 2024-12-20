package bubbleswervegame;
import java.awt.Color;

/**
 * @author https://scott.rodeo/
 */

public class BubbloidVDiscon2 extends AbstractBubbloid {

	public static int[] rowOffset = {0,9,1,9,9,9};
	public static int[] colOffset = {-1,9,0,9,9,9};
	
	public BubbloidVDiscon2(int initialRow, int initialColumn, Grid grid, Color color) {
		super(initialRow, initialColumn, grid, color, rowOffset, colOffset);
	}
}
