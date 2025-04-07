package rodeo.scott.bubbleswerve;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JFrame;

/**
 * Represents a single bubble in the game grid.
 * Each bubble has a position, color, and mobility status.
 * Provides functionality for movement, rotation, and rendering.
 * 
 * @author https://scott.rodeo/
 */
public class Bubble {

    private Grid grid;  // Reference to the game grid.
    private int row, col;  // Current position of the bubble in the grid.
    private boolean ableToMove;  // Indicates if the bubble can move.
    private Color color;  // Color of the bubble.

    /**
     * Constructs a new Bubble.
     * 
     * @param grid   The game grid to which the bubble belongs.
     * @param row    Initial row position of the bubble.
     * @param col    Initial column position of the bubble.
     * @param c      The color of the bubble.
     * @param mobile Indicates whether the bubble can move.
     */
    public Bubble(Grid grid, int row, int col, Color c, boolean mobile) {
        this.grid = grid;
        this.row = row;
        this.col = col;
        color = c;
        ableToMove = mobile;
        initializeBubble();
    }

    /**
     * Initializes the bubble. Reserved for future functionality.
     */
    private void initializeBubble() {
        // No initialization logic currently needed.
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int r) {
        row = r;
    }

    public void setCol(int c) {
        col = c;
    }

    /**
     * Determines if the bubble can move one spot in the specified direction.
     * 
     * @param direction The direction to test.
     * @return True if the move is possible; false otherwise.
     */
    public boolean canMove(Direction direction) {
        if (!ableToMove) {
            return false; // Bubble is immobile.
        }

        boolean move = true;

        // Check if the move is blocked in the specified direction.
        switch (direction) {
            case DOWN:
                if (row == (grid.HEIGHT - 1) || grid.isSet(row + 1, col)) {
                    move = false;
                }
                break;
            case UP:
                if (row == 0 || grid.isSet(row - 1, col)) {
                    move = false;
                }
                break;
            case LEFT:
                if (col == 0 || grid.isSet(row, col - 1)) {
                    move = false;
                }
                break;
            case RIGHT:
                if (col == (grid.WIDTH - 1) || grid.isSet(row, col + 1)) {
                    move = false;
                }
                break;
        }

        return move;
    }

    /**
     * Moves the bubble in the specified direction if possible.
     * 
     * @param direction The direction to move.
     */
    public void move(Direction direction) {
        if (canMove(direction)) {
            switch (direction) {
                case DOWN:
                    row += 1;
                    break;
                case UP:
                    row -= 1;
                    break;
                case RIGHT:
                    col += 1;
                    break;
                case LEFT:
                    col -= 1;
                    break;
            }
        }
    }

    /**
     * Moves the bubble to a new position as part of a rotation.
     * 
     * @param x New column position.
     * @param y New row position.
     */
    public void rotate(int x, int y) {
        row = y;
        col = x;
    }

    /**
     * Checks if the bubble can rotate to a new position.
     * 
     * @param x Target column position.
     * @param y Target row position.
     * @return True if the rotation is valid; false otherwise.
     */
    public boolean canRotate(int x, int y) {
        boolean rotatable = true;

        // Check boundaries.
        if (x >= grid.WIDTH || x < 0 || y >= grid.HEIGHT || y < 0) {
            rotatable = false;
        }

        // Check for collisions with other bubbles.
        if (rotatable && grid.isSet(y, x)) {
            rotatable = false;
        }

        return rotatable;
    }

    /**
     * Changes the color of the bubble.
     * 
     * @param c The new color.
     */
    public void setColor(Color c) {
        color = c;
    }

    /**
     * Gets the color of the bubble.
     * 
     * @return The current color of the bubble.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Draws the bubble on the given graphics context.
     * 
     * @param g The graphics context.
     */
    public void draw(Graphics g) {
        if (color.equals(Grid.EMPTY)) {
            return; // Skip rendering for empty bubbles.
        }

        Graphics2D g2d = (Graphics2D) g;

        int cellSize = grid.getPanelGame().getCellSize();

        // Calculate the upper left (x, y) coordinate of the bubble.
        int actualX = Grid.LEFT + (col * cellSize);
        int actualY = Grid.TOP + (row * cellSize);

        // Draw the outer bubble (background).
        g2d.setColor(color);
        g2d.fillOval(actualX, actualY, cellSize, cellSize);

        // Add an inner border for texture.
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(actualX + 2, actualY + 2, cellSize - 4, cellSize - 4);

        // Add a gradient for a 3D effect.
        GradientPaint gradient = new GradientPaint(
            actualX, actualY, color.brighter(),
            actualX + cellSize, actualY + cellSize, color.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillOval(actualX + 4, actualY + 4, cellSize - 8, cellSize - 8);

        // Add inner grid lines for texture.
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillOval(actualX + (cellSize / 4), actualY + (cellSize / 4), cellSize / 4, cellSize / 4);

        // Draw the black outline.
        g2d.setColor(Color.BLACK);
        g2d.drawOval(actualX, actualY, cellSize, cellSize);
    }
}
