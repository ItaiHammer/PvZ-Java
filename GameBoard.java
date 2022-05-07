public class GameBoard {
	private int[][] grid; 						// the grid that stores the pieces

	public GameBoard(int width, int height) {
		grid = new int[height][width];

		// Initialize starting positions
		grid[grid.length/2][grid[0].length/2] = 2;
		grid[grid.length/2-1][grid[0].length/2-1] = 2;

		grid[grid.length/2][grid[0].length/2-1] = 1;
		grid[grid.length/2-1][grid[0].length/2] = 1;


	}


	int we = 2;
	// Make the requested move at (row, col) by changing the grid.
	// returns false if no move was made, true if the move was successful.
	public boolean move(int row, int col) {
		System.out.println("[DEBUGGING INFO] You clicked in row " + row + " and column " + col);

		// check if move is not valid.  If so, return false.
		if (we == 2) {
			we = 1;
		}else {
			we = 2;
		}
		grid[row][col] = we;


		return true; // if move was valid, return true
	}

	/*
	 * Return true if the game is over. False otherwise.
	 */
	public boolean isGameOver() {

		/*** YOU COMPLETE THIS METHOD ***/
		int count = 0;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 1) {
					count++;
				}
			}
		}

		return count >= 8;
	}
	public int[][] getGrid() {
		return grid;
	}

	// Return true if the row and column in location loc are in bounds for the grid
	public boolean isInGrid(int row, int col) {

		/*** YOU COMPLETE THIS METHOD ***/

		return false;
	}
}