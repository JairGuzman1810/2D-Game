package entity;

import main.GamePanel;

import java.util.Random;

// NPC_OldMan represents an NPC character that simulates an old man within the game.
// It inherits from the Entity class, which provides basic entity properties and behaviors
// and overrides specific methods to implement unique characteristics for this NPC.
public class NPC_OldMan extends Entity {

    // Initializes the NPC_OldMan with a reference to the GamePanel.
    // Sets the initial direction and speed, and loads its images.
    public NPC_OldMan(GamePanel gp) {
        super(gp);

        direction = "down"; // Sets the initial direction for the NPC to "down".
        speed = 1;          // Sets the movement speed of the NPC.

        getImage(); // Loads images for the NPC's directional movement animations.
    }

    // Loads the images for each direction (up, down, left, right) to represent
    // the old man's movement using the setup method to load and scale each image.
    public void getImage() {
        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_2");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_2");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_2");
    }

    @Override
    // Determines the NPC's action based on a random direction selection.
    // Changes direction every 120 frames and resets the action lock counter.
    public void setAction() {
        super.setAction();
        actionLockCounter++;

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
