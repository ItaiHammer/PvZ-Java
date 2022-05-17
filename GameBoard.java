public class GameBoard {
	private Plant[][] grid; 						// the grid that stores the pieces

	public GameBoard(int width, int height) {
		grid = new Plant[height][width];
	}

	// Make the requested move at (row, col) by changing the grid.
	// returns false if no move was made, true if the move was successful.
	public boolean move(int row, int col) {
		System.out.println("[DEBUGGING INFO] You clicked in row " + row + " and column " + col);

		// check if move is not valid.  If so, return false.

		return true; // if move was valid, return true
	}

	/*
	 * Return true if the game is over. False otherwise.
	 */
	public boolean isGameOver() {

		/*** YOU COMPLETE THIS METHOD ***/
		return false;
	}
	public Plant[][] getGrid() {
		return grid;
	}

	// Return true if the row and column in location loc are in bounds for the grid
	public boolean isInGrid(int row, int col) {

		/*** YOU COMPLETE THIS METHOD ***/

		return false;
	}
}