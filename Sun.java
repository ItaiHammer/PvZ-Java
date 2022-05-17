public class Sun {
    int size, x, y, fallAmount, timer, id, opacity;
    int speed = 3;

    public Sun(int size, int x, int y, int fallAmount, int timer, int id) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.fallAmount = fallAmount;
        this.timer = timer;
        this.id = id;
        this.opacity = 255;
    }

    public void fall() {
        if(fallAmount > 0) {
            y+=speed;
            fallAmount -= speed;
        }
    }
}
