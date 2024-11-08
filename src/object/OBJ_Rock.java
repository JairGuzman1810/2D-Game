package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

// OBJ_Rock defines a rock projectile used in the game, which deals damage when it hits a target.
// This projectile has a limited lifespan and consumes mana when used.
public class OBJ_Rock extends Projectile {

    GamePanel gp;

    // Constructor that initializes the fireball's attributes such as name, speed, attack power, and lifespan.
    public OBJ_Rock(GamePanel gp) {
        super(gp);

        // Store the game panel reference.
        this.gp = gp;

        // Set the name of the projectile.
        name = "Rock";

        // Define the speed of the rock.
        speed = 8;

        // Set the maximum life (lifespan) of the rock in frames.
        maxLife = 80;

        // Initialize current life to maxLife for full lifespan at launch.
        life = maxLife;

        // Define the attack power of the rock.
        attack = 2;

        // Set the mana cost to cast this rock.
        useCost = 1;

        // Set the initial state of the rock as inactive.
        alive = false;

        // Load the images for the rock in different directions.
        getImage();
    }

    // Loads images for the rock's animations in all four directions.
    public void getImage() {
        up1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right1 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
    }

    @Override
    // Checks if the entity has sufficient ammon to use the rock.
    public boolean haveResource(Entity entity) {
        // Returns true if entity's ammo is greater than or equal to rock's ammo cost.
        return entity.ammo >= useCost;
    }

    @Override
    // Deducts the required ammo from the entity to throw the rock.
    public void subtractResource(Entity entity) {
        // Calls the superclass method (if any additional behavior is defined).
        super.subtractResource(entity);
        // Reduces the entity's ammo by the rock's ammo cost.
        entity.ammo -= useCost;
    }
}
