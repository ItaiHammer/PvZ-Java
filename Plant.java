import processing.core.PImage;

import java.util.ArrayList;

public class Plant {
    ArrayList<PImage> idlePlantAnimation = new ArrayList<PImage>();
    ArrayList<PImage> shootingPlantAnimation = new ArrayList<PImage>();
    int health, strength, animationIndex;

    public Plant (ArrayList<PImage> idlePlantAnimation, ArrayList<PImage> shootingPlantAnimation, int health, int strength, int animationIndex) {
        this.idlePlantAnimation = idlePlantAnimation;
        this.shootingPlantAnimation = shootingPlantAnimation;
        this.health = health;
        this.strength = strength;
        this.animationIndex = animationIndex;
    }
}
