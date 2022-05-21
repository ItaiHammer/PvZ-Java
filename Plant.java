import processing.core.PImage;

import java.util.ArrayList;

public class Plant {
    ArrayList<PImage> idlePlantAnimation = new ArrayList<PImage>();
    ArrayList<PImage> shootingPlantAnimation = new ArrayList<PImage>();
    int index, cost, health, strength, animationIndex, type;
    PImage currentFrame;
    int moveTimer;
    int moveTimerCount = moveTimer;

    public Plant (int index, int cost, ArrayList<PImage> idlePlantAnimation, ArrayList<PImage> shootingPlantAnimation, int health, int type, int animationIndex, int moveTimer) {
        this.index = index;
        this.cost = cost;
        this.idlePlantAnimation = idlePlantAnimation;
        this.shootingPlantAnimation = shootingPlantAnimation;
        this.health = health;
        this.animationIndex = animationIndex;
        this.currentFrame = idlePlantAnimation.get(0);
        this.moveTimer = moveTimer;
        this.type = type;
    }

    public void move (GameBoard game, ArrayList<PeaShooterProjectile> activePeaShooterProjectiles, ArrayList<ArrayList<Zombie>> activeZombies, ArrayList<Sun> activeSuns) {
        if (type == 0) {
            Plant[][] plantGrid = game.getGrid();

            for (int r = 0; r < plantGrid.length; r++) {
                for (int c = 0; c < plantGrid[0].length; c++) {
                    if (plantGrid[r][c] != null) {
                        if (plantGrid[r][c].index == index) {
                            boolean doesZombieExist = false;

                            if (r >= 0 && r <= activeZombies.size()-1) {
                                if (activeZombies.get(r).size() > 0) {
                                    doesZombieExist = true;
                                }
                            }

                            if (moveTimerCount <= 0 && doesZombieExist) {
                                int squareWidth = (int)(900/1.2)/9;
                                int squareHeight = (int)(590/1.2)/5;
                                int gridOffsetX = 30;
                                int gridOffsetY = 80;

                                activePeaShooterProjectiles.add(new PeaShooterProjectile(gridOffsetX + squareWidth*(c + 1), gridOffsetY + squareHeight*r + squareHeight/10, r));
                                moveTimerCount = moveTimer;
                            }else {
                                moveTimerCount--;
                            }
                        }
                    }
                }
            }
        }else if (type == 1) {
            if (moveTimerCount <= 0) {
                System.out.println("running2");
                int squareWidth = (int)(900/1.2)/9;
                int squareHeight = (int)(590/1.2)/5;
                int gridOffsetX = 30;
                int gridOffsetY = 80;

                activeSuns.add(new Sun(80, gridOffsetX + (RunGraphicalGame.getPlantLocation(index).get(0) * squareWidth + (squareWidth/2 - 40) + (float)(((int)(Math.random() * 2) - 0.5) * 2) * squareWidth/4),gridOffsetY + (RunGraphicalGame.getPlantLocation(index).get(1) * squareHeight - 20), 100, 300, RunGraphicalGame.getSunId()));
                moveTimerCount = moveTimer;
            }else {
                moveTimerCount--;
            }
        }
    }
}
