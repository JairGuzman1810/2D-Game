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

    // A logger to handle logging messages, such as errors during image loading.
    private static final Logger logger = Logger.getLogger(Entity.class.getName());

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;

    // The entity's position in the game world (worldX, worldY).
    public int worldX, worldY;

    // The speed of the entity, determining how fast it moves per frame.
    public int speed;

    // BufferedImages representing different frames of the player's movement in each direction.
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    // Stores the current direction of the player's movement (up, down, left, right).
    public String direction;

    // spriteCounter keeps track of the number of frames elapsed for animating the sprite.
    public int spriteCounter = 0;

    // spriteNum indicates which sprite image (1 or 2) to display for animation.
    public int spriteNum = 1;

    // A Rectangle representing the entity's solid area used for collision detection.
    // This defines the area around the entity that is checked for collisions.
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    // These variables store the default x and y coordinates of the solid area within the entity.
    // They are used to reset the solid area position if needed, for example, after a collision detection update.
    public int solidAreaDefaultX, solidAreaDefaultY;

    // A flag that indicates whether a collision has occurred (true) or not (false).
    public boolean collisionOn = false;

    // Counter to lock the NPC's action temporarily (e.g., to control how long the NPC stays idle or continues moving in one direction).
    public int actionLockCounter = 0;

    // Constructor initializes the Game Panel
    public Entity(GamePanel gp) {
        this.gp = gp;
    }


    // Sets the action for the NPC, such as determining its direction or behavior.
    // This method can be overridden by subclasses to customize NPC behavior.
    public void setAction() {

    }


    // Updates the NPC's state each frame, handling movement, collision detection,
    // and animation updates. This ensures smooth movement and interaction with the game world.
    public void update() {
        setAction();
        // Check for tile collision.
        collisionOn = false; // Reset collision state.
        gp.cChecker.checkTile(this); // Check if the NPC is colliding with any tiles.
        gp.cChecker.checkObject(this, false); // Check if the NPC is colliding with any object.
        gp.cChecker.checkPlayer(this); // Check if the NPC is colliding with the player.

        // If no collision detected, move the NPC in the current direction.
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

    // Draws the NPC on the screen relative to the player's position.
    public void draw(Graphics2D g2) {

        BufferedImage image;
        // Calculate the object's position on the screen based on the player's position.
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;


        // Check if the NPC is within the player's visible area.
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

            // Draw the NPC's image on the screen at the calculated position.
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    // Helper method to load an image by name, scale it, and return the BufferedImage
    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool(); // Create an instance of UtilityTool for image scaling
        BufferedImage image = null; // Initialize the BufferedImage variable

        try {
            // Load the image from resources and scale it to the size of the game tiles
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize); // Scale the loaded image
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load image", e); // Log error if image loading fails
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found!", e); // Log warning if image resource is not found
        }

        return image; // Return the scaled image
    }
}
