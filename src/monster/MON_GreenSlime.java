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
    // Handles the Green Slime's behavior. If `onPath` is true, the slime chases the player.
    // Otherwise, it moves randomly and may enter aggro mode or perform a ranged attack.
    public void setAction() {

        // If the slime is currently in aggro mode, it actively pursues the player.
        if (onPath) {

            // Check if the player has moved too far away (more than 15 tiles) to stop chasing.
            checkIsStopChasing(gp.player, 15, 100);

            // Calculate the path to the player's position and follow it.
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));

            // Attempt to shoot at the player if conditions (like distance and timing) are met.
            checkIsShooting(200, 30);

        } else {
            // If the slime is not in aggro mode, it performs random movements.

            // Check if the player is within 5 tiles and decide whether to enter aggro mode.
            checkIsStartChasing(gp.player, 5, 100);

            // Move in a random direction if not chasing the player.
            getRandomDirection();
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
