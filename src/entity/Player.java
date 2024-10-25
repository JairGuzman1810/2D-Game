package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// The Player class extends the Entity class, inheriting common attributes like position and speed.
// It adds specific logic for handling player movement, drawing, and now, collision detection and object interaction.
public class Player extends Entity {

    // A logger to handle logging messages, such as errors during image loading.
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;

    // Reference to the KeyHandler, which captures the player's key inputs for movement.
    KeyHandler keyH;

    // screenX and screenY represent the player's position on the screen, always centered.
    public final int screenX;
    public final int screenY;

    // This counter tracks how many keys the player has picked up.
    public int hasKey = 0;

    // Tracks idle frames to set player to standstill position after a delay.
    int standCounter = 0;

    // Constructor initializes the Player with references to the game environment and key handler.
    public Player(GamePanel gp, KeyHandler keyH) {
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
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_2.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_2.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_2.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load player image", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found for player!", e);
        }
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
            // Retrieve the object's name to determine its type.
            String objectName = gp.obj[i].name;

            // Handle different object types based on the name.
            switch (objectName) {
                case "Key":
                    // Play the sound effect for picking up a key.
                    gp.playSE(1); // Index 1 refers to the "coin" sound (interpreted as key pickup).

                    // Increment the player's key count.
                    hasKey++;

                    // Remove the key object from the game (set it to null).
                    gp.obj[i] = null;

                    // Show a message indicating the player picked up a key.
                    gp.ui.showMessage("You got a key!");
                    break;

                case "Boots":
                    // Play the sound effect for picking up boots (power-up).
                    gp.playSE(2); // Index 2 refers to the "power-up" sound.

                    // Increase the player's speed when boots are collected.
                    speed += (int) 1.5;

                    // Remove the boots from the game once picked up.
                    gp.obj[i] = null;

                    // Show a message indicating the player's speed has increased.
                    gp.ui.showMessage("Speed up!");
                    break;

                case "Door":
                    // If the player has a key, unlock the door.
                    if (hasKey > 0) {
                        // Play the sound effect for unlocking the door.
                        gp.playSE(3); // Index 3 refers to the "unlock" sound.

                        // Remove the door (unlocked) from the game.
                        gp.obj[i] = null;

                        // Decrease the player's key count after unlocking the door.
                        hasKey--;

                        // Show a message indicating the door has been opened.
                        gp.ui.showMessage("You opened the door!");

                    } else {
                        // Show a message indicating that a key is needed to unlock the door.
                        gp.ui.showMessage("You need a key!");
                    }

                    break;

                case "Chest":
                    // If the player reaches the chest, finish the game.
                    gp.ui.gameFinished = true;

                    // Stop the background music and play the victory fanfare.
                    gp.stopMusic();
                    gp.playSE(4); // Index 4 refers to the "fanfare" sound.

                    break;
            }
        }
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
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

}
