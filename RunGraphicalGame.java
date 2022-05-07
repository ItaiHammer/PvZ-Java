import processing.core.*;

public class RunGraphicalGame extends PApplet {
	GameBoard game;
	Display display;

	public void settings() {
		size(640, 550);
	}

	public void setup() {
		// Create a game object
		game = new GameBoard(8, 8);

		// Create the display
		// parameters: (10,10) is upper left of display
		// (400, 400) is the width and height
		display = new Display(this, 10, 10, 400, 400);

		display.setColor(1, color(255, 0, 0));
		display.setColor(2, color(0, 255, 0));

		// You can use images instead if you'd like.
		// display.setImage(1, "assets/on.png");
		// display.setImage(2, "assets/off.png");

		display.initializeWithGame(game);
	}

	@Override
	public void draw() {
		background(200);

		display.drawGrid(game.getGrid()); // display the game

		if (game.isGameOver() == true) {
			textSize(64);
			fill(0);
			textAlign(CENTER, CENTER);
			text("Game Over!", width/2, height/2);
		}
	}

	public void mouseReleased() {
		Location loc = display.gridLocationAt(mouseX, mouseY);
		int row = loc.getRow();
		int col = loc.getCol();

		game.move(row, col);
	}

	// main method to launch this Processing sketch from computer
	public static void main(String[] args) {
		PApplet.main(new String[] { "RunGraphicalGame" });
	}
}