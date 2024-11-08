package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

// OBJ_Fireball defines a fireball projectile used in the game, which deals damage when it hits a target.
// This projectile has a limited lifespan and consumes mana when used.
public class OBJ_Fireball extends Projectile {

    GamePanel gp;

    // Constructor that initializes the fireball's attributes such as name, speed, attack power, and lifespan.
    public OBJ_Fireball(GamePanel gp) {
        super(gp);

        // Store the game panel reference.
        this.gp = gp;

        // Set the name of the projectile.
        name = "Fireball";

        // Define the speed of the fireball.
        speed = 5;

        // Set the maximum life (lifespan) of the fireball in frames.
        maxLife = 80;

        // Initialize current life to maxLife for full lifespan at launch.
        life = maxLife;

        // Define the attack power of the fireball.
        attack = 2;

        // Set the mana cost to cast this fireball.
        useCost = 1;

        // Set the initial state of the fireball as inactive.
        alive = false;

        // Load the images for the fireball in different directions.
        getImage();
    }

    // Loads images for the fireball's animations in all four directions.
    public void getImage() {
        up1 = setup("/projectile/fireball_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/fireball_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/fireball_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/fireball_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/fireball_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/fireball_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/fireball_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/fireball_right_2", gp.tileSize, gp.tileSize);
    }

    @Override
    // Checks if the entity has sufficient mana to use the fireball.
    public boolean haveResource(Entity entity) {
        // Returns true if entity's mana is greater than or equal to fireball's mana cost.
        return entity.mana >= useCost;
    }

    @Override
    // Deducts the required mana from the entity to cast the fireball.
    public void subtractResource(Entity entity) {
        // Calls the superclass method (if any additional behavior is defined).
        super.subtractResource(entity);
        // Reduces the entity's mana by the fireball's mana cost.
        entity.mana -= useCost;
    }
}
