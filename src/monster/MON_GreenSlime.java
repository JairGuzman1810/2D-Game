package monster;

import entity.Entity;
import main.GamePanel;

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

        type = 2;              // Sets type to 2, indicating a monster entity.
        name = "Green Slime";  // Assigns the name of this monster type.
        speed = 1;             // Defines the movement speed of the slime.
        maxLife = 4;           // Sets maximum life for the slime.
        life = maxLife;        // Initializes life to maxLife.

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
    }
}
