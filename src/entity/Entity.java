package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// The Entity class serves as a base class for any game entity (like a player or enemy).
// It contains the common attributes of an entity, such as position (x, y), speed, and collision settings.
public class Entity {

    // General
    private static final Logger logger = Logger.getLogger(Entity.class.getName()); // Logger for debugging or error messages.
    GamePanel gp; // Reference to the game panel, provides game state and properties.

    // Entity Identification
    public String name; // The name of the entity.
    public int type; // Type identifier: 0 = player, 1 = NPC, 2 = monster.

    // Position and Movement
    public int worldX, worldY; // Entity's position in the game world.
    public int speed; // Movement speed of the entity.
    public String direction = "down"; // Current movement direction (up, down, left, right).

    // Animation
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // Movement animation frames.
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2; // Attack animation frames.
    public int spriteCounter = 0; // Counts frames elapsed for animation.
    public int spriteNum = 1; // Current sprite frame number (1 or 2) for animation.

    // Collision and Solid Area
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // Area around the entity checked for collisions.
    public int solidAreaDefaultX, solidAreaDefaultY; // Default x, y coordinates of the solid area within the entity.
    public boolean collisionOn = false; // Flag to indicate if a collision has occurred.
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);

    // Action Control
    public int actionLockCounter = 0; // Counter to lock entity's action temporarily (e.g., idle/move control).
    public boolean invincible = false; // Flag for invincibility to prevent repeated damage.
    public int invincibleCounter = 0; // Tracks duration of invincibility effect.
    public boolean attacking = false; // Determines if the entity is attacking, triggering attack animations.
    public int invincibleFrameCounter = 0; // Count frames while the player is invincible


    // Character Status
    public int maxLife; // Max life points the entity can have.
    public int life; // Current life points of the entity.

    // Dialogue
    String[] dialogues = new String[20]; // Array to store dialogue text, allowing multiple phrases.
    int dialogueIndex = 0; // Current dialogue index for displaying text.

    // Object Properties
    public BufferedImage image, image2, image3; // Images representing the object (e.g., key, door).
    public boolean collision = false; // Indicates if the object can trigger collisions.

    // Constructor initializes the Game Panel
    public Entity(GamePanel gp) {
        this.gp = gp;
    }


    // Sets the action for the NPC, such as determining its direction or behavior.
    // This method can be overridden by subclasses to customize NPC behavior.
    public void setAction() {

    }

    // Handles the entity speaking by displaying dialogue text to the player
    // and adjusting the entity's direction to face the player.
    public void speak() {
        // Reset dialogue index if no more dialogue is available
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        // Display the current dialogue
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++; // Move to the next dialogue for future interactions

        // Face the entity towards the player’s direction
        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";
        }
    }


    // Updates the entity's state each frame, handling movement, collision detection,
    // and animation updates. This ensures smooth movement and interaction with the game world.
    public void update() {
        setAction();
        // Check for tile collision.
        collisionOn = false; // Reset collision state.
        gp.cChecker.checkTile(this); // Check if the entity is colliding with any tiles.
        gp.cChecker.checkObject(this, false); // Check if the entity is colliding with any object.
        gp.cChecker.checkEntity(this, gp.npc); // Check if the entity is colliding with any other NPC.
        gp.cChecker.checkEntity(this, gp.monster); // Check if the entity is colliding with any other monster.
        boolean contactPlayer = gp.cChecker.checkPlayer(this); // Check if the entity is colliding with the player, if yes true, no false

        // Checks if the entity is a monster and has contacted the player.
        // If so, reduces player's life and sets them to invincible to avoid consecutive damage.
        if (this.type == 2 && contactPlayer) {
            if (!gp.player.invincible) {
                gp.player.life--; // Decrease player's life by one unit.
                gp.player.invincible = true; // Trigger invincibility to prevent repeat hits.
            }
        }

        // If no collision detected, move the entity in the current direction.
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

        // If entity is invincible, increment the invincibility counter.
        if (invincible) {
            invincibleCounter++; // Track invincibility duration.

            // Disable invincibility after 40 frames and reset the counter.
            if (invincibleCounter > 40) {
                invincible = false; // End invincibility.
                invincibleCounter = 0; // Reset counter for next use.
            }
        }
    }

    // Draws the entity on the screen relative to the player's position.
    public void draw(Graphics2D g2) {

        BufferedImage image;
        // Calculate the object's position on the screen based on the player's position.
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;


        // Check if the entity is within the player's visible area.
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            image = switch (direction) {
                case "up" -> (spriteNum == 1) ? up1 : up2;
                case "down" -> (spriteNum == 1) ? down1 : down2;
                case "left" -> (spriteNum == 1) ? left1 : left2;
                case "right" -> (spriteNum == 1) ? right1 : right2;
                default -> down1;
            };

            // Apply invincibility blinking effect
            if (invincible) {
                // Toggle alpha between 0.3 and 1.0 every 10 frames
                float alpha = (invincibleFrameCounter / 10 % 2 == 0) ? 0.4f : 1.0f;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                invincibleFrameCounter++;
            } else {
                invincibleFrameCounter = 0; // Reset frame counter when invincibility ends
            }

            // Draw the entity's image on the screen at the calculated position.
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            // Reset alpha to 1f
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    // Helper method to load an image by name, scale it, and return the BufferedImage
    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool(); // Create an instance of UtilityTool for image scaling
        BufferedImage image = null; // Initialize the BufferedImage variable

        try {
            // Load the image from resources and scale it to the size of the game tiles
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, width, height); // Scale the loaded image
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load image", e); // Log error if image loading fails
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found!", e); // Log warning if image resource is not found
        }

        return image; // Return the scaled image
    }
}
