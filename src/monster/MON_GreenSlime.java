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
        speed = 1;                              // Defines the movement speed of the slime.
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
    // Defines the Green Slime's behavior by selecting a random movement direction
    // every 120 frames, updating the direction and resetting the action lock counter.
    public void setAction() {
        super.setAction();
        actionLockCounter++;

        // Changes direction if actionLockCounter reaches 120, ensuring the slime
        // moves in random directions periodically.
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // Generates a random number between 1 and 100.

            if (i <= 25) {
                direction = "up"; // 25% chance for "up" direction.
            } else if (i <= 50) {
                direction = "down"; // 25% chance for "down" direction.
            } else if (i <= 75) {
                direction = "left"; // 25% chance for "left" direction.
            } else {
                direction = "right"; // 25% chance for "right" direction.
            }

            actionLockCounter = 0; // Resets the action lock counter.
        }

        // Randomly triggers a slime shot if conditions are met (1% chance).
        int i = new Random().nextInt(100) + 1;

        if (i > 99 && !projectile.alive && shotAvailableCounter == 30) {
            // Sets projectile position, direction, and marks it as active.
            projectile.set(worldX, worldY, direction, true, this);

            // Adds projectile to the game for slime's ranged attack.
            gp.projectileList.add(projectile);

            // Resets shot counter to prevent immediate consecutive shots.
            shotAvailableCounter = 0;
        }
    }

    @Override
    // Handles the slime's reaction to damage by resetting its action counter
    // and changing direction to match the player's current facing direction.
    public void damageReaction() {
        super.damageReaction();
        // Resets the action lock counter to allow an immediate response after taking damage
        actionLockCounter = 0;
        // Sets the slime's direction to match the player's current facing direction,
        // making the slime appear to react by moving away from the player's attack
        direction = gp.player.direction;
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
