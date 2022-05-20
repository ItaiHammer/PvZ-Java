import processing.core.PImage;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Zombie {
    ArrayList<PImage> walkingZombieAnimation = new ArrayList<PImage>();
    ArrayList<PImage> eatingZombieAnimation = new ArrayList<PImage>();
    int size, health, animationIndex, row, damage;
    float x, y, speed, attackSpeed;
    float attackTimer = attackSpeed;
    int opacity = 255;

    public Zombie (float x, float y, ArrayList<PImage> walkingZombieAnimation, ArrayList<PImage> eatingZombieAnimation, int size, int health, int damage, float speed, float attackSpeed, int animationIndex, int row) {
        this.x = x;
        this.y = y;
        this.walkingZombieAnimation = walkingZombieAnimation;
        this.eatingZombieAnimation = eatingZombieAnimation;
        this.size = size;
        this.health = health;
        this.speed = speed;
        this.attackSpeed = attackSpeed;
        this.animationIndex = animationIndex;
        this.row = row;
        this.attackTimer = attackTimer;
        this.damage = damage;
        this.opacity = opacity;

    }

    public void move(int x, int y) {

    }
}
