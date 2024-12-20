package bubbleswervegame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JFrame;

/**
 * @author https://scott.rodeo/
 */

public class Bubble {
	
	private Grid grid; 
	private int row, col;
	private boolean ableToMove;
	private Color color; 
	
	public Bubble(Grid grid, int row, int col, Color c, boolean mobile) {
		
		this.grid = grid;
		this.row = row;
		this.col = col;
		color = c;
		ableToMove = mobile;
	    initializeBubble();
	}

	private void initializeBubble() { 

	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int r) {
		row=r;
	}
	
	public void setCol(int c) {
		col=c;
	}

	
	/**
	 * true if bubble can move 1 spot in direction
	 * 
	 * @param direction		the direction to test for possible move
	 */
	public boolean canMove(Direction direction) {
		if (!ableToMove) {
			return false;
		}
		
		boolean move = true;
		
		// if the direction is blocked, the bubble can't move
		switch (direction) {
			
			case DOWN:
				if (row == (Grid.HEIGHT - 1) || grid.isSet(row + 1, col))
					move = false;
					break;
			case UP:
				if (row == (0) || grid.isSet(row - 1, col))
					move = false;
					break;		
			case LEFT:
				if (col == 0 || grid.isSet(row, col - 1))
					move = false;
					break;			
							
			case RIGHT:
				if (col == (Grid.WIDTH - 1) || grid.isSet(row, col + 1))
					move = false;
					break;
		}
		
		return move;
	}


	
	public void move(Direction direction) {
		if (canMove(direction)) {
			switch (direction) {
			
				case DOWN:
 					row = row + 1;
					break;
				case UP:
 					row = row - 1;
					break;
				case RIGHT:
					col = col + 1;
					break;		
				case LEFT:
					col = col - 1;
					break;				
			}
		}
	}
	
	
	/**
	 * move the bubble to rotated position
	 */
	public void rotate(int x, int y) {	
		row = y;
		col = x;	
	}
		

	/**
	 * check if the bubble can rotate
	 */
	public boolean canRotate(int x, int y) {
		boolean rotatable=true;

		// check the four walls
		if ( x >= Grid.WIDTH || x < 0 ) {
			rotatable=false;
		}

		if ( y >= Grid.HEIGHT || y < 0 ) {
			rotatable=false;
		}
		
		// check other pieces
		if (rotatable==true&&grid.isSet(y, x)) {
			rotatable=false;
		}
		
		return rotatable;
	}
	

	/**
	 * Changes the color of this bubble
	 */
	public void setColor(Color c) {
		color = c;
	}

	
	/**
	 * Gets the color of this bubble
	 */
	public Color getColor() {
		return color;
	}

	
	/**
	 * Draws this bubble on the given graphics context
	 */
	public void draw(Graphics g) {
	    if (color.equals(Grid.EMPTY)) {
	        return; // Skip rendering for empty bubbles
	    }

	    Graphics2D g2d = (Graphics2D) g;

	    int cellSize = grid.getPanelGame().getCellSize();

	    // Calculate the upper left (x, y) coordinate of this bubble
	    int actualX = Grid.LEFT + (col * cellSize);
	    int actualY = Grid.TOP + (row * cellSize);

	    // Draw the outer rectangle (background)
	    g2d.setColor(color);
	    g2d.fillOval(actualX, actualY, cellSize, cellSize);

	    // Add an inner border for texture
	    g2d.setColor(color.darker());
	    g2d.setStroke(new BasicStroke(2)); // Set the border thickness
	    g2d.drawOval(actualX + 2, actualY + 2, cellSize - 4, cellSize - 4);

	    // Add a gradient for 3D effect
	    GradientPaint gradient = new GradientPaint(
	        actualX, actualY, color.brighter(),
	        actualX + cellSize, actualY + cellSize, color.darker()
	    );
	    g2d.setPaint(gradient);
	    g2d.fillOval(actualX + 4, actualY + 4, cellSize - 8, cellSize - 8);

	    // Add inner grid lines for texture
	    g2d.setColor(new Color(255, 255, 255, 150)); // Semi-transparent white
	    g2d.fillOval(actualX + (cellSize / 4), actualY + (cellSize / 4), cellSize / 4, cellSize / 4);
	    
	    // Draw the black outline
	    g2d.setColor(Color.BLACK);
	    g2d.drawOval(actualX, actualY, cellSize, cellSize);
	}


}
