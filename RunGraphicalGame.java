import processing.core.*;

import java.util.ArrayList;

public class RunGraphicalGame extends PApplet {
	int fps = 60;
	int sunSpawnTime = 20;
	int zombieSpawnTime = 20;
	int innerWidth = 800;
	int innerHeight = 600;

	GameBoard game;
	Display display;
	ArrayList<Sun> activeSuns = new ArrayList<Sun>();
	ArrayList<ArrayList<Zombie>> activeZombies = new ArrayList<ArrayList<Zombie>>();


	int indexCount = 0;
	int sunIndexCount = 0; // sun id
	int sunSpawnCount = 0;
	int zombieSpawnCount = 0; // zombie timer

//	list of plant indexes
	ArrayList<Plant> plantList = new ArrayList<Plant>();

	public void settings() {
		size(innerWidth, innerHeight);
	}

	public void setup() {
		frameRate((float)(fps));

		// adding rows to zombies
		for (int i = 0; i < 5; i++) {
			activeZombies.add(new ArrayList<Zombie>());
		}

//		settings timers
		sunSpawnCount = (int)(sunSpawnTime * frameRate);
		zombieSpawnCount = (int)(zombieSpawnTime * frameRate);

//		handling images
//		defining plants
//		pea shooter: 1
		ArrayList<PImage> peaShooterIdleAnimation = new ArrayList<PImage>();
		ArrayList<PImage> peaShooterShootingAnimation = new ArrayList<PImage>();

		PImage peaShooterIdleAnimation1 = loadImage("plants/peaShooter/idleAnimation/peaShooterIdleAnimation1.png");

		peaShooterIdleAnimation.add(peaShooterIdleAnimation1);
		peaShooterShootingAnimation.add(peaShooterIdleAnimation1);

		Plant peaShooter = new Plant(giveIndex(), peaShooterIdleAnimation, peaShooterShootingAnimation, 100, 20, 0);
		plantList.add(peaShooter);

		// sunflower: 2
		ArrayList<PImage> sunFlowerIdleAnimation = new ArrayList<PImage>();
		ArrayList<PImage> sunFlowerShootingAnimation = new ArrayList<PImage>();

		PImage sunFlowerIdleAnimation1 = loadImage("plants/sunFlower/idleAnimation/sunflowerIdleAnimation1.png");

		sunFlowerIdleAnimation.add(sunFlowerIdleAnimation1);
		sunFlowerShootingAnimation.add(sunFlowerIdleAnimation1);

		Plant sunFlower = new Plant(giveIndex(), sunFlowerIdleAnimation, sunFlowerShootingAnimation, 100, 0, 0);
		plantList.add(sunFlower);

		// Create a game object
		game = new GameBoard(9, 5);

		game.getGrid()[0][0] = peaShooter;
		game.getGrid()[1][0] = peaShooter;
		game.getGrid()[0][1] = sunFlower;
		game.getGrid()[1][1] = sunFlower;
		game.getGrid()[2][1] = sunFlower;
		game.getGrid()[3][1] = sunFlower;
		game.getGrid()[4][1] = sunFlower;

		// Create the display
		// parameters: (10,10) is upper left of display
		// (400, 400) is the width and height
		display = new Display(this, 30, 80, (int)(900/1.2), (int)(590/1.2));

		for (int i = 0; i < plantList.size(); i++) {
			display.setImage(i+1, plantList.get(i).currentFrame);
		}
		display.setColor(0, color(1, 0)); // empty

		display.drawGrid(plantGridRenderer(game.getGrid()));

		display.initializeWithGame(game);

		spawnSun();
		for (int i = 0; i < 1; i++) {
			spawnNormalZombie();
		}
	}

	@Override
	public void draw() {
//		timers
		// sun spawn timer
		if (sunSpawnCount >= 0) {
			sunSpawnCount--;
		}else {
			spawnSun();
			sunIndexCount++;
			sunSpawnCount = (int)(sunSpawnTime * frameRate);
		}

		// zombie spawn timer
		if (zombieSpawnCount >= 0) {
			zombieSpawnCount--;
		}else {
			spawnNormalZombie();
			zombieSpawnCount++;
			zombieSpawnCount = (int)(zombieSpawnTime * frameRate);
		}

//		visuals
		image(loadImage("Background1.png"), -220, 0);

		display.drawGrid(plantGridRenderer(game.getGrid())); // display the game



		// draws normal zombies
		for (int i = 0; i < activeZombies.size(); i++) {
			ArrayList<Zombie> currentList = activeZombies.get(i);

			for (int j = 0; j < currentList.size(); j++) {
				Zombie zombie = activeZombies.get(i).get(j);

				System.out.println(getGridLocation(zombie));

				if (!zombieAttack(zombie)) {
					zombie.x -= zombie.speed;
				}

				PImage zombieImage = loadImage("zombies/normal/normalIdleAnimation/normalIdleAnimation1.png");
				image(zombieImage, zombie.x, zombie.y, (int)(zombie.size), (int)(zombie.size*1.5));
			}
		}

		int removedSuns = 0;

		// draws suns
		for (int i = 0; i < activeSuns.size()-removedSuns; i++) {
			Sun sun = activeSuns.get(i);
			PImage sunImage = loadImage("sun.png");
			int animationSpeed = 3;

			sun.fall();
			sun.timer -= animationSpeed;
			if (sun.timer <= 0) {
				if (sun.opacity > 1) {
					sun.opacity -= animationSpeed*2;
					tint(255, sun.opacity);
				}else {
					removeSun(sun.id);
					removedSuns++;
				}
			}

//			System.out.println(sun.timer);
//			System.out.println(sun.opacity);

			image(sunImage, sun.x, sun.y, sun.size, sun.size);
			tint(255, 255);
		}

		if (game.isGameOver()) {
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

	public static int[][] plantGridRenderer(Plant[][] plantGrid) {
		int [][] renderedGrid = new int[plantGrid.length][plantGrid[0].length];

		for (int r = 0; r < plantGrid.length; r++) {
			for (int c = 0; c < plantGrid[0].length; c++) {
				if (plantGrid[r][c] != null) {
					renderedGrid[r][c] = plantGrid[r][c].index+1;
				}
			}
		}

		return renderedGrid;
	}

	public int giveIndex () {
		indexCount++;
		return indexCount-1;
	}

	public void spawnSun () {
		int sunSize = 80;
		int x = (int)((Math.random() * innerWidth - sunSize)+sunSize);
		int y = -sunSize;
		int fallAmount = (int)((Math.random() * innerHeight - 300) + 300);

		Sun currentSun = new Sun(sunSize, x, y, fallAmount, 10*fps, sunIndexCount);
		sunIndexCount++;
		activeSuns.add(currentSun);
	}

	public void removeSun(int id) {
		int index = 0;

		for (int i = 0; i < activeSuns.size(); i++) {
			index++;
			Sun currentSun = activeSuns.get(i);

			if(currentSun == activeSuns.get(id)) {
				activeSuns.remove(index);
			}
		}
	}

	public void spawnNormalZombie() {
		int zombieSize = 100;
		int row = (int)(Math.random()*5);
		int x = innerWidth - zombieSize;
		int y = row*100;

		ArrayList<PImage> normalWalkingAnimation = new ArrayList<PImage>();
		ArrayList<PImage> normalEatingAnimation = new ArrayList<PImage>();

		PImage normalWalkingAnimation1 = loadImage("zombies/normal/idleAnimation/normalIdleAnimation1.png");

		normalWalkingAnimation.add(normalWalkingAnimation1);
		normalEatingAnimation.add(normalWalkingAnimation1);

		Zombie currentZombie = new Zombie(x, y, normalWalkingAnimation, normalEatingAnimation, zombieSize, 100, 2, 10, 5*fps, 0, row+1);

		for (int i = 1; i < 6; i++) {
			if (row == i) {
				activeZombies.get(i-1).add(currentZombie);
			}
		}
	}

	public boolean zombieAttack(Zombie zombie) {
		if (getGridLocation(zombie) != 0) {
			Plant plant;

			if (getGridLocation(zombie) >= 8) {
				plant = game.getGrid()[zombie.row-1][getGridLocation(zombie)-1];
			}else {
				plant = game.getGrid()[zombie.row-1][getGridLocation(zombie)-2];
			}

			if (plant == null) {
				return false;
			}else {
				System.out.println(plant.health);
				if (zombie.attackTimer >= 0) {
					plant.health -= zombie.damage;
					zombie.attackTimer = zombie.attackSpeed;
				}else {
					zombie.attackTimer -= 5;
				}

				if (plant.health <= 0) {
					game.getGrid()[zombie.row-1][getGridLocation(zombie)-2] = null;
				}
				return true;
			}
		}
		return false;
	}

	public int getGridLocation (Zombie zombie) {
		int x = zombie.x + zombie.size;
		int squareWidth = (int)(900/1.2)/9;
		int leftSideOffset = 30;

		for (int r = 0; r < 9; r++) {
			int gridLeft = leftSideOffset + r*squareWidth;
			int gridRight = leftSideOffset + (r+1)*squareWidth;

			if (x >= gridLeft && x <= gridRight) {
				return r+1;
			}
		}


		return 0;

	}
	// main method to launch this Processing sketch from computer

	public static void main(String[] args) {
		PApplet.main(new String[] { "RunGraphicalGame" });
	}
}