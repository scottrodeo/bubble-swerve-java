package rodeo.scott.bubbleswerve;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 * @author https://scott.rodeo/
 */

public class Grid {
	
	public int HEIGHT = 22;	// default height of game grid in bubbles
	public int WIDTH = 12;	// default width of game grid in bubbles
	public static int TOP;		// default pixel position: top of grid
	public static int LEFT;	// default pixel position: left of grid

	public static final int BORDER = 0;
	public static final Color EMPTY = Color.WHITE;
	
	private Bubble[][] board;
	private Engine engine;
    
	
	public Grid(Engine engine) {
	    this.engine = engine;
	    initializeGrid();
	}
	 

	private void initializeGrid() {  
	
		board = new Bubble[this.HEIGHT][this.WIDTH];
		
		// Populate the grid with bubbles
		for (int row = 0; row < HEIGHT; row++) {
		    for (int col = 0; col < WIDTH; col++) {
		        board[row][col] = new Bubble(this, row, col, EMPTY, false);
		    }
		}   
	}
	    
    
    public void clearCell(int row, int col) {
        if (row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH) {
            board[row][col].setColor(EMPTY); // EMPTY represents an unoccupied cell
        }
    }
    
    
    public void rotateBoardClockwise() {
    	
        Bubble[][] newBoard = new Bubble[WIDTH][HEIGHT]; // Transposed dimensions
        
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                newBoard[col][HEIGHT - 1 - row] = board[row][col];
                newBoard[col][HEIGHT - 1 - row].setRow(col);
                newBoard[col][HEIGHT - 1 - row].setCol(HEIGHT - 1 - row);
            }
        }
        
        board = newBoard;
        int temp = WIDTH;
        WIDTH = HEIGHT;
        HEIGHT = temp; // Update dimensions
    }

    
    
    public boolean isValidAndEmpty(int row, int col) {
        // Check bounds
        if (row < 0 || row >= HEIGHT || col < 0 || col >= WIDTH) {
            return false; // Out of bounds
        }
        // Check if the cell is empty
        return board[row][col].getColor() == EMPTY; // Assuming EMPTY is a constant for unoccupied cells
    }

    
    public Color getColor(int row, int col) {
        // Validate row and column indices
        if (row < 0 || row >= HEIGHT || col < 0 || col >= WIDTH) {
            throw new IllegalArgumentException("Invalid grid coordinates: (" + row + ", " + col + ")");
        }

        Bubble square = board[row][col];
        return (square != null) ? square.getColor() : Grid.EMPTY; // Return the color or EMPTY if null
    }


    public boolean isSet(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return false; // Prevent ArrayIndexOutOfBoundsException
        }
        return board[row][col].getColor() != EMPTY;
    }

    
    public Game getGame() {
    	return engine.getGame();
    }
    
    public JFrame getFrame() {
    	return engine.getProgramWindow();
    }    
    
    public PanelMenu getPanelMenu() {
    	return engine.getPanelMenu();
    }      
    
    public PanelGame getPanelGame() {
    	return engine.getPanelGame();
    }          
    
    
    // Changes bubble color
    public void set(int row, int col, Color c) {
		
		board[row][col].setColor(c);
	}


	public void clearRow(int row) {
	    for (int col = 0; col < WIDTH; col++) {
	        board[row][col].setColor(EMPTY); // Clear the row by setting all cells to empty
	    }
	}
	
	
	public int clearFullRows(Game.Orientation currentOrientation) {
	    int cleared = 0;

	    switch (currentOrientation) {
	        case DOWN: // Default orientation
	            for (int row = engine.game.grid.HEIGHT - 1; row >= 0; row--) {
	                if (isFullRow(row)) {
	                    deleteRow(row);
	                    cleared++;
	                    row++; // Re-check the same row after shifting
	                }
	            }
	            break;

	        case UP: // Upside-down orientation
	            for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	                if (isFullRow(row)) {
	                    deleteRowUp(row);
	                    cleared++;
	                    row--; // Re-check the same row after shifting
	                }
	            }
	            break;

	        case LEFT: // Rotated left
	            for (int col = engine.game.grid.WIDTH - 1; col >= 0; col--) {
	                if (isFullColumn(col)) {
	                    deleteColumnLeft(col);
	                    cleared++;
	                    col++; // Re-check the same column after shifting
	                }
	            }
	            break;

	        case RIGHT: // Rotated right
	            for (int col = 0; col < engine.game.grid.WIDTH; col++) {
	                if (isFullColumn(col)) {
	                    deleteColumnRight(col);
	                    cleared++;
	                    col--; // Re-check the same column after shifting
	                }
	            }
	            break;
	    }

	    return cleared; // Return the number of cleared lines
	}

	
	private void deleteRowUp(int row) {
	    for (int r = row; r < engine.game.grid.HEIGHT - 1; r++) { // Shift rows up
	        for (int col = 0; col < engine.game.grid.WIDTH; col++) {
	            board[r][col].setColor(board[r + 1][col].getColor());
	        }
	    }

	    // Clear the bottom row
	    for (int col = 0; col < engine.game.grid.WIDTH; col++) {
	        board[engine.game.grid.HEIGHT - 1][col].setColor(Grid.EMPTY);
	    }
	}

	
	private void deleteColumnLeft(int col) {
	    for (int c = col; c < engine.game.grid.WIDTH - 1; c++) { // Shift columns left
	        for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	            board[row][c].setColor(board[row][c + 1].getColor());
	        }
	    }

	    // Clear the rightmost column
	    for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	        board[row][engine.game.grid.WIDTH - 1].setColor(Grid.EMPTY);
	    }
	}

	
	private void deleteColumnRight(int col) {
	    for (int c = col; c > 0; c--) { // Shift columns right
	        for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	            board[row][c].setColor(board[row][c - 1].getColor());
	        }
	    }

	    // Clear the leftmost column
	    for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	        board[row][0].setColor(engine.game.grid.EMPTY);
	    }
	}

	
	private boolean isFullRow(int row) {
	    for (int col = 0; col < engine.game.grid.WIDTH; col++) {
	        if (board[row][col].getColor().equals(engine.game.grid.EMPTY)) {
	            return false;
	        }
	    }
	    return true;
	}

	
	private boolean isFullColumn(int col) {
	    for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	        if (board[row][col].getColor().equals(Grid.EMPTY)) {
	            return false;
	        }
	    }
	    return true;
	}
	
	
	private void deleteColumn(int col) {
	    for (int c = col; c > 0; c--) {
	        for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	            board[row][c].setColor(board[row][c - 1].getColor());
	        }
	    }

	    // Clear the leftmost column
	    for (int row = 0; row < engine.game.grid.HEIGHT; row++) {
	        board[row][0].setColor(engine.game.grid.EMPTY);
	    }
	}

	
	// Helper method to delete a row and shift rows above down
	void deleteRow(int row) {
	    for (int r = row; r > 0; r--) {
	        for (int col = 0; col < WIDTH; col++) {
	            board[r][col].setColor(board[r - 1][col].getColor());
	        }
	    }

	    // Clear the topmost row
	    for (int col = 0; col < WIDTH; col++) {
	        board[0][col].setColor(EMPTY);
	    }
	}

	
	public void deleteRows(int row) {
		
		Color colorChange;
		
		if (row == 0) {
		
			for (int c = 0; c < WIDTH; c++) {
				board[0][c].setColor(EMPTY);
			}
			
		}else{
			
			for (int r = row; r > 0; r--) {
				for (int c = 0; c < WIDTH; c++) {
					board[r][c].setColor(board[r-1][c].getColor());	
				}
			}
		}
	}
	
	
	public void checkRows() {
		
		boolean rowFullCheck;
		for (int r = 0; r < HEIGHT; r++) {
			rowFullCheck = true;
			for (int c = 0; c < WIDTH; c++) {
				if (board[r][c].getColor().equals(EMPTY)) {
					rowFullCheck = false;
				}
			}
			
			if (rowFullCheck == true) {
				
				if (r == 0) {
					
					for (int c = 0; c < WIDTH; c++) {
						board[0][c].setColor(EMPTY);
					}
					
				}else{
					
					for (int y = r; y> 0; y--) {
						for (int c = 0; c < WIDTH; c++) {
							board[y][c].setColor(board[y-1][c].getColor());	
						}
					}
				}
			}
		}
	}
	

	// Draws the grid on the given Graphics context
	public void draw(Graphics g) {

        this.LEFT = engine.getPanelGame().calculateLeft();	// Calculate the dynamic LEFT position
        this.TOP = engine.getPanelGame().calculateTop();	// Calculate the dynamic TOP position
        
	    int boardWidth = engine.game.grid.WIDTH * engine.getPanelGame().getCellSize();
	    int boardHeight = engine.game.grid.HEIGHT * engine.getPanelGame().getCellSize();
        
	    // Draw the rounded rectangle gray grid box
	    /*
        g.setColor(Color.GRAY);
        g.setColor(Color.GRAY);
        g.fillRoundRect(engine.getPanelGame().calculateLeft(), engine.getPanelGame().calculateTop(), boardWidth, boardHeight, 30, 30);
	    */
		// Draw all the bubbles in the grid
		// Empty ones first (to avoid covering the black lines of the pieces that have already fallen)
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				if (board[r][c].getColor().equals(EMPTY)) {
					board[r][c].draw(g);
				}
			}
		}
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				if (!board[r][c].getColor().equals(EMPTY)) {
					board[r][c].draw(g);
				}
			}
		}
	}



}
