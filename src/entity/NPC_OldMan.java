package entity;

import main.GamePanel;

import java.awt.*;
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

        // Define the solid area for the old man, which will be used for collision detection.
        solidArea = new Rectangle();
        solidArea.x = 8;  // Offset of the solid area within the player's sprite.
        solidArea.y = 16; // Offset within the sprite.
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;  // Width of the player collision area.
        solidArea.height = 32; // Height of the player collision area.

        getImage(); // Loads images for the NPC's directional movement animations.

        setDialogue(); // Sets the dialogue that will be displayed when speaking with the NPC.
    }

    // Loads the images for each direction (up, down, left, right) to represent
    // the old man's movement using the setup method to load and scale each image.
    public void getImage() {
        up1 = setup("/npc/oldman_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }

    // Sets the series of dialogues for this NPC, which will display sequentially when spoken to.
    public void setDialogue() {
        dialogues[0] = "Hello, boy.";
        dialogues[1] = "So you've come to this island to \nfind the treasure?";
        dialogues[2] = "I used to be a great wizard but now... \nI'm a bit too old for taking an \nadventure";
        dialogues[3] = "Well, good luck on you.";
    }

    @Override
    // Determines the NPC's action based on a random direction selection.
    // Changes direction every 120 frames and resets the action lock counter.
    public void setAction() {
        super.setAction();

        // If the NPC is on a path, it will head towards a specific goal (goalCol, goalRow).
        if (onPath) {
            int goalCol = 12; // Column position on the map where the NPC should go.
            int goalRow = 9;  // Row position on the map where the NPC should go.

            // Instructs the NPC to move towards the goal.
            searchPath(goalCol, goalRow);

        } else {
            actionLockCounter++;

            // Changes direction if actionLockCounter reaches 120, ensuring random movement.
            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1; // Generates a random number between 1 and 100.

                // 25% chance for each direction.
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

    @Override
    // Triggers the speak method inherited from Entity, which will manage dialogue display
    // and align the NPC’s direction towards the player during interaction.
    // Once the player talks to the NPC, the NPC starts moving towards a specific location on the map.
    public void speak() {
        super.speak();

        // Once the player speaks to the NPC, set onPath to true, indicating NPC will move.
        onPath = true;
    }
}
