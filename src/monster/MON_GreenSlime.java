package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_CoinBronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

// MON_GreenSlime represents a green slime monster entity within the game,
// inheriting properties and behaviors from the Entity class. This monster
// type has unique attributes such as life, speed, and movement animations.
public class MON_GreenSlime extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructs the Green Slime with specific attributes and initializes
    // its image and solid area for collision detection.
    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_monster;                    // Sets type to type_monster, indicating a monster entity.
        name = "Green Slime";                   // Assigns the name of this monster type.
        defaultSpeed = 1;                       // The base movement speed of the slime.
        speed = defaultSpeed;                   // Current movement speed, initially set to the defaultSpeed.
        maxLife = 4;                            // Sets maximum life for the slime.
        life = maxLife;                         // Initializes life to maxLife.
        attack = 5;                             // Sets the attack power of the slime.
        defense = 0;                            // Sets the defense level of the slime.
        exp = 2;                                // Experience points awarded to the player when this monster is defeated.
        projectile = new OBJ_Rock(gp);         // Initializes a Rock projectile that the slime can shoot.
        // Defines the solid area for collision detection, setting its position
        // and size, which helps prevent the entity from moving through obstacles.
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage(); // Loads the slime's movement images for animation.
    }

    // Loads the images for each direction (up, down, left, right) to represent
    // the green slime's movement using the setup method to load and scale each image.
    public void getImage() {
        up1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
    }

    @Override
    // Updates the slime's behavior based on its proximity to the player.
    // The slime may enter 'aggro' mode when the player is close and will pursue the player.
    // If the slime moves too far from the player, it will stop pursuing.
    public void update() {
        // Calls the superclass's update method to handle general updates
        super.update();

        // Calculates the absolute distance between the slime and the player on both X and Y axes
        int xDistance = Math.abs(worldX - gp.player.worldX);
        int yDistance = Math.abs(worldY - gp.player.worldY);

        // Determines the total tile distance between the slime and the player, in terms of tile units
        int tileDistance = (xDistance + yDistance) / gp.tileSize;

        // Checks if the slime is not already in aggro mode and the player is nearby (less than 5 tiles away)
        // If true, there's a chance for the slime to go into aggro mode and pursue the player.
        if (!onPath && tileDistance < 5) {
            int i = new Random().nextInt(100) + 1; // Generates a random number between 1 and 100.

            // 50% chance to enter aggro mode if the player is within 5 tiles.
            if (i > 50) {
                onPath = true; // Slime becomes aggressive and starts pursuing the player.
            }
        }

        // If the slime is already in aggro mode and the player moves too far away (more than 20 tiles),
        // the slime will lose interest and stop pursuing the player.
        if (onPath && tileDistance > 20) {
            onPath = false; // Slime stops pursuing the player when the distance exceeds 20 tiles.
        }

    }

    @Override
    // Defines the Green Slime's behavior, making it either pursue the player if it's in an aggro state
    // or move randomly when it's not. It also handles the slime's ranged attack when certain conditions are met.
    public void setAction() {

        // If the slime is in aggro mode (onPath is true), it will pursue the player
        if (onPath) {

            // Calculates the column and row position of the player on the map grid
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

            // Makes the slime search for a path to the player's position
            searchPath(goalCol, goalRow);

            // Random chance for the slime to fire a shot if it's aggro
            int i = new Random().nextInt(200) + 1;

            // If the random chance exceeds 197, and the slime is not already shooting a projectile
            // and the shot is available, the slime will shoot.
            if (i > 197 && !projectile.alive && shotAvailableCounter == 30) {
                // Sets the projectile's position, direction, and makes it active
                projectile.set(worldX, worldY, direction, true, this);

                // Adds the projectile to the game, allowing the slime to fire it
                for (int j = 0; j < gp.projectile[j].length; j++) {
                    if (gp.projectile[gp.currentMap][j] == null) {
                        gp.projectile[gp.currentMap][j] = projectile;
                        break;
                    }
                }
                // Resets the shot counter to prevent consecutive shots without delay
                shotAvailableCounter = 0;
            }

        } else { // If the slime is not in aggro mode, it will move randomly

            // Increments the actionLockCounter to control the timing of the slime's movements
            actionLockCounter++;

            // Every 120 frames (controlled by actionLockCounter), the slime will choose a new direction
            if (actionLockCounter == 120) {
                // Generates a random number to determine the direction of movement
                Random random = new Random();
                int i = random.nextInt(100) + 1; // Random number between 1 and 100.

                // 25% chance to move up, down, left, or right
                if (i <= 25) {
                    direction = "up"; // 25% chance for "up"
                } else if (i <= 50) {
                    direction = "down"; // 25% chance for "down"
                } else if (i <= 75) {
                    direction = "left"; // 25% chance for "left"
                } else {
                    direction = "right"; // 25% chance for "right"
                }

                // Resets the action lock counter to allow the slime to change direction after 120 frames
                actionLockCounter = 0;
            }

        }
    }

    @Override
    // Handles the slime's reaction to damage by resetting its action counter,
    // and changing its state to aggro. The slime will immediately start
    // following the player after taking damage.
    public void damageReaction() {
        super.damageReaction();

        // Resets the action lock counter to allow an immediate response after taking damage
        actionLockCounter = 0;

        // Changes the slime's state to aggro, causing it to pursue the player
        onPath = true;
    }

    @Override
    // Determines the item dropped by the slime upon defeat:
    // - 50% chance for a bronze coin.
    // - 25% chance for a heart.
    // - 25% chance for a mana crystal.
    public void checkDrop() {
        super.checkDrop();

        // Generate a random number between 1 and 100 (inclusive) to determine the drop item.
        int i = new Random().nextInt(100) + 1;

        // Determine the item to drop based on the random number:
        // - If i is between 1 and 50, the drop will be a bronze coin (50% chance).
        if (i <= 50) {
            dropItem(new OBJ_CoinBronze(gp));
        }
        // - If i is between 51 and 75, the drop will be a heart (25% chance).
        else if (i <= 75) {
            dropItem(new OBJ_Heart(gp));
        }
        // - If i is between 76 and 100, the drop will be a mana crystal (25% chance).
        else {
            dropItem(new OBJ_ManaCrystal(gp));
        }
    }
}
