package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

// OBJ_Rock defines a rock projectile used in the game, which deals damage when it hits a target.
// This projectile has a limited lifespan and consumes ammo when used.
public class OBJ_Rock extends Projectile {

    // String identifier for the class, representing the specific name of this object type.
    public static final String objName = "Rock";

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that initializes the rock's attributes such as name, speed, attack power, and lifespan.
    public OBJ_Rock(GamePanel gp) {
        super(gp); // Initialize the parent class with the GamePanel reference.

        // Store the game panel reference.
        this.gp = gp;

        // Set the name of the projectile.
        name = objName;

        // Define the speed of the rock.
        speed = 8;

        // Set the maximum life (lifespan) of the rock in frames.
        maxLife = 80;

        // Initialize current life to maxLife for full lifespan at launch.
        life = maxLife;

        // Define the attack power of the rock.
        attack = 2;

        // Set the ammo cost to throw this rock.
        useCost = 1;

        // Set the initial state of the rock as inactive.
        alive = false;

        // Load the images for the rock in different directions.
        getImage();
    }

    // Loads images for the rock's animations in all four directions.
    public void getImage() {
        // Load the image for the rock in different directions: up, down, left, and right.
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
    // Checks if the entity has sufficient ammo to throw the rock.
    public boolean haveResource(Entity entity) {
        // Returns true if the entity's ammo is greater than or equal to the rock's ammo cost.
        return entity.ammo >= useCost;
    }

    @Override
    // Deducts the required ammo from the entity to throw the rock.
    public void subtractResource(Entity entity) {
        // Reduces the entity's ammo by the rock's ammo cost.
        entity.ammo -= useCost;
    }

    // Return the particle color when the rock is used.
    public Color getParticleColor() {
        return new Color(40, 50, 0); // A dull, earthy color for the rock particles.
    }

    // Return the particle size when the rock is used.
    public int getParticleSize() {
        return 10; // The size of the particles in pixels.
    }

    // Return the particle speed when the rock is used.
    public int getParticleSpeed() {
        return 1; // The speed at which the particles move.
    }

    // Return the maximum lifetime of the particles after the rock is thrown.
    public int getParticleMaxLife() {
        return 20; // Particles will live for 20 frames before disappearing.
    }
}
