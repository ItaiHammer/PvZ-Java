import processing.core.PImage;
import java.util.ArrayList;

public class Plant {
    ArrayList<PImage> idlePlantAnimation = new ArrayList<PImage>();
    ArrayList<PImage> shootingPlantAnimation = new ArrayList<PImage>();
    int index, health, strength, animationIndex;
    PImage currentFrame;

    public Plant (int index, ArrayList<PImage> idlePlantAnimation, ArrayList<PImage> shootingPlantAnimation, int health, int strength, int animationIndex) {
        this.index = index;
        this.idlePlantAnimation = idlePlantAnimation;
        this.shootingPlantAnimation = shootingPlantAnimation;
        this.health = health;
        this.strength = strength;
        this.animationIndex = animationIndex;
        this.currentFrame = idlePlantAnimation.get(0);
    }
}
