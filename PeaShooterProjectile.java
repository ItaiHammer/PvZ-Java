import java.util.ArrayList;

public class PeaShooterProjectile {
    int row;
    float x, y;
    int damage = 20;

    public PeaShooterProjectile (float x, float y, int row) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.row = row;
    }

    public void move (PeaShooterProjectile peaShooterProjectile, ArrayList<ArrayList<Zombie>> activeZombies, ArrayList<PeaShooterProjectile> activePeaShooterProjectiles, int size, int innerWidth) {
        x += 1;

        if (x - size >= innerWidth) {
            activePeaShooterProjectiles.remove(peaShooterProjectile);
        }

        for (int i = 0; i < activeZombies.get(row).size(); i ++) {
            Zombie zombie = activeZombies.get(row).get(i);

            if (x + size > zombie.x && x - size < zombie.x + zombie.size) {
                System.out.println(zombie.health);
                activePeaShooterProjectiles.remove(peaShooterProjectile);

                if (zombie.health > 0) {
                    zombie.health -= damage;
                }
            }
        }
    }
}
