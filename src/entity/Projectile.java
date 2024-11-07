package entity;

import main.GamePanel;

// Projectile defines the base behavior for projectiles in the game, providing attributes and methods
// that control movement, lifespan, and interaction with other entities.
// Projectiles are launched by an entity and move in a set direction until they hit a target or expire.
public class Projectile extends Entity {

    // Reference to the entity that launched the projectile
    Entity entity;

    // Constructor initializes the projectile with the game panel reference
    public Projectile(GamePanel gp) {
        super(gp);
    }

    // Sets the initial state of the projectile when launched, including position, direction, and life reset
    public void set(int worldX, int worldY, String direction, boolean alive, Entity entity) {
        this.worldX = worldX;           // Set the projectile's initial X-coordinate
        this.worldY = worldY;           // Set the projectile's initial Y-coordinate
        this.direction = direction;     // Set the movement direction of the projectile
        this.alive = alive;             // Mark the projectile as active (alive)
        this.entity = entity;           // Reference to the launching entity
        this.life = this.maxLife;       // Reset projectile life span upon launch
    }

    // Update method controls movement, lifespan, and collision for the projectile
    public void update() {

        // Check for collisions with monsters if launched by the player
        if (entity == gp.player) {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);

            // Deactivate projectile if it hits a monster and inflict damage
            if (monsterIndex != 999) {
                gp.player.damageMonster(monsterIndex, attack); // Inflict damage on the monster
                alive = false;                                 // Deactivate projectile
            }
        }

        // Move projectile based on its direction
        switch (direction) {
            case "up" -> worldY -= speed;    // Move upward
            case "down" -> worldY += speed;  // Move downward
            case "left" -> worldX -= speed;  // Move left
            case "right" -> worldX += speed; // Move right
        }

        // Reduce projectile's life, setting it inactive if life reaches zero
        life--;
        if (life <= 0) {
            alive = false;
        }

        // Increment spriteCounter to control the animation frame rate.
        spriteCounter++;

        // Toggle between two sprites every 12 frames to create shot animation.
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
}