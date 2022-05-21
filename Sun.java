public class Sun {
    int size, fallAmount, timer, id, opacity;
    float x, y;
    float speed = (float) (0.5);
    boolean hasBeenPicked = false;
    float pickedUpSpeedX, pickedUpSpeedY;

    public Sun(int size, float x, float y, int fallAmount, int timer, int id) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.fallAmount = fallAmount;
        this.timer = timer;
        this.id = id;
        this.opacity = 255;
        this.hasBeenPicked = false;
        this.pickedUpSpeedX = pickedUpSpeedX;
        this.pickedUpSpeedY = pickedUpSpeedY;
    }

    public void fall() {
        if(fallAmount > 0) {
            y+=speed;
            fallAmount -= speed;
        }
    }
}
