import processing.core.PImage;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Zombie {
    ArrayList<PImage> walkingZombieAnimation = new ArrayList<PImage>();
    ArrayList<PImage> eatingZombieAnimation = new ArrayList<PImage>();
    int size, health, speed, attackSpeed, animationIndex, row, damage;
    float x, y;
    int attackTimer = attackSpeed;

    public Zombie (float x, float y, ArrayList<PImage> walkingZombieAnimation, ArrayList<PImage> eatingZombieAnimation, int size, int health, int damage, int speed, int attackSpeed, int animationIndex, int row) {
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

    }

    public void move(int x, int y) {

    }
}
