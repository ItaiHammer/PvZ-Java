import processing.core.*;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import java.util.ArrayList;

public class RunGraphicalGame extends PApplet {
	int fps = 60;
	float time = 0;
	int secondsValue = (int)(time/60 - ((int)(time/60/60) * 60));
	int minutesValue = (int)(time/60/60);
	int sunSpawnTime = 10;
	int zombieSpawnTime = 60;
	int innerWidth = 800;
	int innerHeight = 600;
	int amountOfSuns = 50;
	boolean isGameOver = false;

	static GameBoard game;
	Display display;
	ArrayList<Sun> activeSuns = new ArrayList<Sun>();
	ArrayList<ArrayList<Zombie>> activeZombies = new ArrayList<ArrayList<Zombie>>();
	static ArrayList<PeaShooterProjectile> activePeaShooterProjectiles = new ArrayList<PeaShooterProjectile>();
	ArrayList<Integer> plantsMenu = new ArrayList<Integer>();
	int currentPlantSelected = 0;

	// images
	PImage peaShooterIdleAnimation1;
	PImage peaShooterCard;
	PImage sunFlowerIdleAnimation1;
	PImage sunFloweCard;
	PImage zombieImage;
	PImage sunImage;
	PImage background;
	PImage plantMenu;
	PImage gameOverScreen;
	PImage peaShooterProjectileImage;

//	sounds
	Minim loader;
	AudioPlayer song;
	AudioPlayer gameoverAudio;
	AudioPlayer theZombiesAreComingSound;
	AudioPlayer brainzSound;
	AudioPlayer zombieEating;

	int indexCount = 0;
	static int sunIndexCount = 0; // sun id
	int sunSpawnCount = 0;
	int zombieIndexCount = 0;
	int zombieSpawnCount = 0; // zombie timer
	int gameOverScreenOpacityTimer = 0;
	int gameStartedAnimationTimer = 220;

//	list of plant indexes
	ArrayList<Plant> plantList = new ArrayList<Plant>();

	public void settings() {
		size(innerWidth, innerHeight);
	}

	public void setup() {
		frameRate((float)(fps));

		// setting audio
		loader = new Minim(this);

		song = loader.loadFile("Loonboon.mp3");
		gameoverAudio = loader.loadFile("gameover.mp3");
		theZombiesAreComingSound = loader.loadFile("theZombiesAreComing.mp3");
		brainzSound = loader.loadFile("Brainz.mp3");
		zombieEating = loader.loadFile("zombieEating.mp3");

		// playing initial sounds
		theZombiesAreComingSound.play();

		// adding rows to zombies
		for (int i = 0; i < 5; i++) {
			activeZombies.add(new ArrayList<Zombie>());
		}

//		settings timers
		sunSpawnCount = (int)(sunSpawnTime * frameRate);
		zombieSpawnCount = (int)(zombieSpawnTime * frameRate);

		// loading image
		background = loadImage("Background1.png");
		peaShooterIdleAnimation1 = loadImage("plants/peaShooter/idleAnimation/peaShooterIdleAnimation1.png");
		peaShooterCard = loadImage("plants/peaShooter/peaShooterCard.png");
		peaShooterCard.resize(50, 75);
		sunFlowerIdleAnimation1 = loadImage("plants/sunFlower/idleAnimation/sunflowerIdleAnimation1.png");
		sunFloweCard = loadImage("plants/sunFlower/sunFlowerCard.png");
		sunFloweCard.resize(50, 75);
		zombieImage = loadImage("zombies/normal/normalIdleAnimation/normalIdleAnimation1.png");
		sunImage = loadImage("sun.png");
		plantMenu = loadImage("plantsMenu.png");
		gameOverScreen = loadImage("gameOverScreen.png");
		peaShooterProjectileImage = loadImage("ProjectilePea.png");

//		defining plant cards
		plantsMenu.add(1);
		plantsMenu.add(2);

		// Create a game object
		game = new GameBoard(9, 5);

		// Create the display
		// parameters: (10,10) is upper left of display
		// (400, 400) is the width and height
		display = new Display(this, 30, 80, (int)(900/1.2), (int)(590/1.2));

		display.setImage(1, peaShooterIdleAnimation1);
		display.setImage(2, sunFlowerIdleAnimation1);
		display.setColor(0, color(1, 0)); // empty

		display.drawGrid(plantGridRenderer(game.getGrid()));

		display.initializeWithGame(game);

		spawnSun();
//		for (int i = 0; i < 2; i++) {
//			spawnNormalZombie();
//		}
	}

	@Override
	public void draw() {
//		System.out.println(frameRate);
		time += 1;

		// playing music
		if (!theZombiesAreComingSound.isPlaying() && !song.isPlaying()) {
			song.play();
			song.loop();
		}

//		timers
		if (gameStartedAnimationTimer > 0) {
			gameStartedAnimationTimer -= 2;
		}

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
			zombieSpawnTime = 60 / ((minutesValue * 2)+1);
			zombieSpawnCount = (int)(zombieSpawnTime * frameRate);
		}

//		visuals
		// background
		translate(gameStartedAnimationTimer, 0);
		image(background, -220, 0);

		// plants menu
		image(plantMenu, 20, 0);
		image(sunImage, 30, 3, 60, 60);

		fill(color(0, 0, 0));
		textSize(20);
		text(amountOfSuns, (int)(68 - (String.valueOf(amountOfSuns).length() * 9.5)), 80);

		// plants listed in plant menu
		fill(color(255, 255, 255));
		for (int i = 0; i < 10; i++) {

			if (i == 0) {
				image(peaShooterCard, 95+(i*52), 8, 50, 70);
			}else if (i == 1) {
				image(sunFloweCard, 95+(i*52), 8, 50, 70);
			}
		}


		display.drawGrid(plantGridRenderer(game.getGrid())); // display the game

		// plantMoves
		for (int r = 0; r < game.getGrid().length; r++) {
			for (int c = 0; c < game.getGrid()[0].length; c++) {
				if (game.getGrid()[r][c] != null) {
					Plant plant = game.getGrid()[r][c];

					plant.move(game, activePeaShooterProjectiles, activeZombies, activeSuns);
				}
			}
		}


		// draws normal zombies
		for (int i = 0; i < activeZombies.size(); i++) {
			ArrayList<Zombie> currentList = activeZombies.get(i);

			for (int j = 0; j < currentList.size(); j++) {
				Zombie zombie = activeZombies.get(i).get(j);

//				System.out.println(getGridLocation(zombie));

				if (!zombieAttack(zombie)) {
					zombie.x -= zombie.speed;
				}

				tint(255, zombie.opacity);
				image(zombieImage, zombie.x, zombie.y, (int)(zombie.size), (int)(zombie.size*1.5));
				tint(255, 255);

				if (zombie.opacity > 0 && zombie.health <= 0) {
					zombie.opacity -= 30;
				}else if (zombie.opacity <= 0 && zombie.health <= 0) {
					activeZombies.get(zombie.row-2).remove(zombie);
				}

				if (zombie.x + zombie.size <= 0) {
					isGameOver = true;
				}
			}
		}

		// drawing peaShooterProjectiles
		for (int i = 0; i < activePeaShooterProjectiles.size(); i++) {
			PeaShooterProjectile peaShooterProjectile = activePeaShooterProjectiles.get(i);
			int size = 30;

			image(peaShooterProjectileImage, peaShooterProjectile.x, peaShooterProjectile.y, size, size);
			peaShooterProjectile.move(peaShooterProjectile, activeZombies, activePeaShooterProjectiles, size, innerWidth);
		}


		// draws suns
		ArrayList<Integer> removedSuns = new ArrayList<Integer>();
		for (int i = 0; i < activeSuns.size(); i++) {
			Sun sun = activeSuns.get(i);

			if (!sun.hasBeenPicked) {
				sun.fall();
			}else {
				// location of sun icon, x: 30 y: 3

				double distance = Math.sqrt(Math.pow(sun.x - 30, 2) + Math.pow(sun.y - 3, 2));

				if (distance > sun.size/2) {
					sun.x += sun.pickedUpSpeedX;
					sun.y += sun.pickedUpSpeedY;
				}
			}
			int animationSpeed = 1;

			sun.timer -= animationSpeed;
			if (sun.timer <= 0) {
				if (sun.opacity > 1) {
					sun.opacity -= animationSpeed*2;
					tint(255, sun.opacity);
				}else {
					tint(255, 1);
					removedSuns.add(sun.id);
				}
			}

			image(sunImage, sun.x, sun.y, sun.size, sun.size);
			tint(255, 255);
		}

		// removing suns from arraylist
		for (int i = 0;i < removedSuns.size(); i++) {
			int currentSunId = removedSuns.get(i);
//			System.out.println(currentSunId);

			removeSun(currentSunId, i);
		}

		fill(0, 0, 0);
		rect(innerWidth - 150, innerHeight - 30, 150, 30);

		fill(255, 255, 255);
		textSize(20);

		String seconds = String.valueOf(secondsValue).length() == 1 ? "0"+secondsValue : String.valueOf(secondsValue);
		String minutes = String.valueOf(minutesValue).length() == 1 ? "0"+minutesValue : String.valueOf(minutesValue);

		secondsValue = (int)(time/60 - ((int)(time/60/60) * 60));
		minutesValue = (int)(time/60/60);

		text(minutes+":"+seconds, innerWidth-(140), innerHeight - 5);

		String fpsText = (int)(frameRate)+"FPS";
		text(fpsText, innerWidth-(fpsText.length() * 15), innerHeight - 5);


//		System.out.println(currentPlantSelected);

		if (isGameOver) {
			song.close();
			gameoverAudio.play();

			gameOverScreenOpacityTimer++;
			if (gameOverScreenOpacityTimer < 255) {
				gameOverScreenOpacityTimer += 5;
			}
			tint(255, gameOverScreenOpacityTimer);
			image(gameOverScreen, 0, 0, innerWidth, innerHeight);
		}
	}


	public void mouseReleased() {
		if (gameStartedAnimationTimer <= 0) {
			boolean sunFound = false;

			for (int i = 0; i < activeSuns.size(); i++) {
				Sun sun = activeSuns.get(i);
				double distance = Math.sqrt(Math.pow(sun.x - mouseX, 2) + Math.pow(sun.y - mouseY, 2));

				if (distance <= sun.size && !sun.hasBeenPicked) {
					double distanceX = Math.abs(sun.x - (30));
					double distanceY = Math.abs(sun.y - (3));
					double sunAnimationSpeed = fps/2;

					sunFound = true;
					amountOfSuns += 25;

					sun.hasBeenPicked = true;
					sun.timer = 0;

					sun.pickedUpSpeedX = (float)(sun.x < 0 ? distanceX/sunAnimationSpeed : -distanceX/fps);
					sun.pickedUpSpeedY = (float)(sun.y < 0 ? distanceY/sunAnimationSpeed : -distanceY/fps);
				}
			}

//			for (int i = 0; i < 10; i++) {
//				rect(95+(i*52), 4, 50, 75);
//			}
			boolean hasCardBeenClicked = false;
			int whichCard = 0;

			for (int i = 0; i < 2; i++) {
				if (mouseY >= 4 && mouseY <= 4+75) {
					if( mouseX >= 95 && mouseX <= 95+(1*50)) {
						hasCardBeenClicked = true;
						whichCard = 1;
						currentPlantSelected = whichCard;
					}else if (mouseX > 95 + 52 && mouseX < 95+(2*52)) {
						hasCardBeenClicked = true;
						whichCard = 2;
						currentPlantSelected = whichCard;
					}
				}
			}

//			image(sunFloweCard, 95+(i*52), 8, 50, 70);

			if (!sunFound && whichCard == 0) {
				Location loc = display.gridLocationAt(mouseX, mouseY);
				int row = loc.getRow();
				int col = loc.getCol();

				// planting
				if (row >= 0 && row < game.getGrid().length && col >= 0 && col < game.getGrid()[0].length) {
					if (currentPlantSelected == 1) {
						plantPeaShooter(row, col);
					}else if (currentPlantSelected == 2) {
						plantSunFlower(row, col);
					}
				}

				game.move(row, col);
			}
		}
	}

	public static int[][] plantGridRenderer(Plant[][] plantGrid) {
		int [][] renderedGrid = new int[plantGrid.length][plantGrid[0].length];

		for (int r = 0; r < plantGrid.length; r++) {
			for (int c = 0; c < plantGrid[0].length; c++) {
				if (plantGrid[r][c] != null) {
					renderedGrid[r][c] = plantGrid[r][c].type+1;
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
		int x = (int)((Math.random() * innerWidth - (sunSize*2))+sunSize * 2);
		int y = -sunSize;
		int fallAmount = (int)(Math.random() * ((innerHeight-((50/innerHeight)*50))*2) + 200);

		Sun currentSun = new Sun(sunSize, x, y, fallAmount, (fallAmount/30)*fps, sunIndexCount);
		sunIndexCount++;
		activeSuns.add(currentSun);
	}

	public void removeSun(int id, int offset) {
		int index = 0;
		boolean found = false;

		for (int i = 0; i < activeSuns.size(); i++) {
			Sun currentSun = activeSuns.get(i);

			if(currentSun.id == id) {
				found = true;
			}
			if (!found) {
				index++;
			}
		}

		if (found) {
			activeSuns.remove(index);
		}
	}

	public void spawnNormalZombie() {
		int zombieSize = 100;
		int row = (int)(Math.random()*6);
		int x = innerWidth;
		int y = -100+row*100;

		brainzSound.rewind();
		brainzSound.play();

		ArrayList<PImage> normalWalkingAnimation = new ArrayList<PImage>();
		ArrayList<PImage> normalEatingAnimation = new ArrayList<PImage>();

		PImage normalWalkingAnimation1 = loadImage("zombies/normal/idleAnimation/normalIdleAnimation1.png");

		normalWalkingAnimation.add(normalWalkingAnimation1);
		normalEatingAnimation.add(normalWalkingAnimation1);

		Zombie currentZombie = new Zombie(x, y, normalWalkingAnimation, normalEatingAnimation, zombieSize, 100, 10, (float)(0.6), (float)(1*fps), 0, row+1);

		for (int i = 1; i < 6; i++) {
			if (row == i) {
				activeZombies.get(i-1).add(currentZombie);
			}
		}
	}

	public boolean zombieAttack(Zombie zombie) {
		if (getGridLocation(zombie) != 0) {
			Plant plant = null;

			if (getGridLocation(zombie)-2 >= 0 && getGridLocation(zombie)-2 <= 9) {
				if (zombie.row -1 < game.getGrid().length) {
					plant = game.getGrid()[zombie.row-2][getGridLocation(zombie)-2];
				}else {
					plant = game.getGrid()[game.getGrid().length-1][getGridLocation(zombie)-2];
				}
			}

			if (plant == null) {
				return false;
			}else {
//				System.out.println(plant.health);
				if (zombie.attackTimer <= 0) {
					zombieEating.rewind();
					zombieEating.play();
					plant.health -= zombie.damage;
					zombie.attackTimer = zombie.attackSpeed;
				}else {
					zombie.attackTimer -= 1;
				}

				if (plant.health <= 0) {
					game.getGrid()[zombie.row-2][getGridLocation(zombie)-2] = null;
				}
				return true;
			}
		}
		return false;
	}

	public static int getSunId() {
		sunIndexCount++;
		return sunIndexCount-1;
	}

	public static ArrayList<Integer> getPlantLocation(int id) {
		ArrayList<Integer> cords = new ArrayList<>();

		for (int r = 0; r < game.getGrid().length; r++) {
			for (int c = 0; c < game.getGrid()[0].length; c++) {
				Plant plant = game.getGrid()[r][c];
				if (plant != null) {
					if (plant.index == id) {
						cords.add(c);
						cords.add(r);
						return cords;
					}
				}
			}
		}

		return cords;
	}

	public int getGridLocation (Zombie zombie) {
		int x = (int)(zombie.x) + zombie.size;
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

	public void plantPeaShooter (int x, int y) {
		System.out.println("running pea");
		int cost = 100;
		if (game.getGrid()[x][y] == null && amountOfSuns >= cost) {
			System.out.println("running pea 2");
			ArrayList<PImage> peaShooterIdleAnimation = new ArrayList<PImage>();
			ArrayList<PImage> peaShooterShootingAnimation = new ArrayList<PImage>();

			peaShooterIdleAnimation.add(peaShooterIdleAnimation1);
			peaShooterShootingAnimation.add(peaShooterIdleAnimation1);

			Plant peaShooter = new Plant(giveIndex(), cost, peaShooterIdleAnimation, peaShooterShootingAnimation, 100, 0, 0, 2*fps);
			plantList.add(peaShooter);

			amountOfSuns -= cost;
			game.getGrid()[x][y] = peaShooter;
			currentPlantSelected = 0;
		}
	}

	public void plantSunFlower (int x, int y) {
		int cost = 50;
		if (game.getGrid()[x][y] == null && amountOfSuns >= cost) {
			ArrayList<PImage> sunFlowerIdleAnimation = new ArrayList<PImage>();
			ArrayList<PImage> sunFlowerShootingAnimation = new ArrayList<PImage>();

			sunFlowerIdleAnimation.add(sunFlowerIdleAnimation1);
			sunFlowerShootingAnimation.add(sunFlowerIdleAnimation1);

			Plant sunFlower = new Plant(giveIndex(), cost, sunFlowerIdleAnimation, sunFlowerShootingAnimation, 100, 1, 0, 30*fps);
			plantList.add(sunFlower);

			amountOfSuns -= cost;
			game.getGrid()[x][y] = sunFlower;
			currentPlantSelected = 0;
		}
	}

	// main method to launch this Processing sketch from computer

	public static void main(String[] args) {
		PApplet.main(new String[] { "RunGraphicalGame" });
	}

	@Override
	public void keyPressed() {
		if (keyCode == 49) {
			currentPlantSelected = 1;
		} else if (keyCode == 50) {
			currentPlantSelected = 2;
		}
	}
}