package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

// The Player class extends the Entity class, inheriting common attributes like position and speed.
// It adds specific logic for handling player movement, drawing, and now, collision detection and object interaction.
public class Player extends Entity {

    // Reference to the KeyHandler, which captures the player's key inputs for movement.
    KeyHandler keyH;

    // screenX and screenY represent the player's position on the screen, always centered.
    public final int screenX;
    public final int screenY;

    // Tracks idle frames to set player to standstill position after a delay.
    int standCounter = 0;

    // Constructor initializes the Player with references to the game environment and key handler.
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;

        // Calculate and set the player's position at the center of the screen.
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Define the solid area for the player, which will be used for collision detection.
        solidArea = new Rectangle();
        solidArea.x = 8;  // Offset of the solid area within the player's sprite.
        solidArea.y = 16; // Offset within the sprite.
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;  // Width of the collision area.
        solidArea.height = 32; // Height of the collision area.

        // Set the player's initial position and speed.
        setDefaultValues();
        getPlayerImage();
    }

    // Sets the default values for the player's position and speed.
    public void setDefaultValues() {
        // Player's starting position in the world (worldX, worldY) in tile units.
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        // Set the player's movement speed.
        speed = 4;
        // Default movement direction.
        direction = "down";
    }

    // Load the images for the player's movement in all four directions.
    public void getPlayerImage() {
        // Use the setup method to load and scale player images for different movements
        up1 = setup("/player/boy_up_1");
        up2 = setup("/player/boy_up_2");
        down1 = setup("/player/boy_down_1");
        down2 = setup("/player/boy_down_2");
        left1 = setup("/player/boy_left_1");
        left2 = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");
    }


    // Update method, called every frame, processes key inputs, moves the player, and handles collisions and object interaction.
    public void update() {
        // Check if no movement keys are pressed to keep the player idle
        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {
            // Increment the stand counter each frame when idle
            standCounter++;

            // If the player has been idle for 20 frames, switch to the standstill sprite
            if (standCounter == 20) {
                spriteNum = 1; // Set sprite to represent the standing still position
                standCounter = 0; // Reset the stand counter for future idle checks
            }

            return; // Exit update early if the player is idle
        }

        // Check and update the player's direction based on key inputs.
        if (keyH.upPressed) {
            direction = "up";
        } else if (keyH.downPressed) {
            direction = "down";
        } else if (keyH.leftPressed) {
            direction = "left";
        } else {
            direction = "right";
        }

        // Check for tile collision.
        collisionOn = false; // Reset collision state.
        gp.cChecker.checkTile(this); // Check if the player is colliding with any tiles.

        // Check for collisions with objects (like keys or doors).
        // objIndex will hold the index of the object the player collides with.
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex); // Call the method to handle object interaction.

        //Check for collisions with NPC
        // npcIndex will hold the index of the NPC the player collides with.
        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        // If no collision detected, move the player in the current direction.
        if (!collisionOn) {
            switch (direction) {
                case "up" -> worldY -= speed;  // Move up in the world (Y-axis).
                case "down" -> worldY += speed; // Move down.
                case "left" -> worldX -= speed; // Move left on the X-axis.
                case "right" -> worldX += speed; // Move right.
            }
        }

        // Increment spriteCounter to control the animation frame rate.
        spriteCounter++;

        // Toggle between two sprites every 12 frames to create walking animation.
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    // Handles object interaction.
    // If the player collides with an object (like a key, door, or chest), this method manages the interaction.
    public void pickUpObject(int i) {
        // Check if an object was found at the collision point (999 indicates no object).
        if (i != 999) {
            //TODO Add logic later
        }
    }

    // Manages interaction with non-playable characters (NPCs).
    // This method checks for collisions with NPCs and initiates dialogue if the player interacts with them.
    public void interactNPC(int i) {
        // Verify if an NPC is found at the collision point (999 indicates no NPC present).
        if (i != 999) {
            // If the enter key is pressed, change the game state to dialogue state and initiate the NPC's dialogue.
            if (gp.keyH.enterPressed) {
                gp.gameState = gp.dialogueState; // Set the game state to allow dialogue interaction.
                gp.npc[i].speak(); // Call the speak method of the colliding NPC to display its dialogue.
            }
        }
        gp.keyH.enterPressed = false; // Reset the enter key state to avoid repeated interactions in the same frame.
    }


    // The draw method draws the player's current sprite based on direction and animation frame.
    public void draw(Graphics2D g2) {
        BufferedImage image = switch (direction) {
            case "up" -> (spriteNum == 1) ? up1 : up2;
            case "down" -> (spriteNum == 1) ? down1 : down2;
            case "left" -> (spriteNum == 1) ? left1 : left2;
            case "right" -> (spriteNum == 1) ? right1 : right2;
            default -> down1;
        };

        // Draw the player sprite at the center of the screen, using screenX and screenY.
        g2.drawImage(image, screenX, screenY, null);
    }

}
