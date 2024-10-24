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
// It adds specific logic for handling player movement and drawing the player on the screen.
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

    // Constructor initializes the Player with references to the game environment and key handler.
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // Calculate and set the player's position at the center of the screen.
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

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

    // Update method, called every frame, processes key inputs and moves the player accordingly.
    public void update() {
        // Skip update if no movement keys are pressed.
        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {
            return;
        }

        // Check and update the player's direction and position based on key inputs.
        if (keyH.upPressed) {
            direction = "up";
            worldY -= speed; // Move up in the world (Y-axis).
        } else if (keyH.downPressed) {
            direction = "down";
            worldY += speed; // Move down.
        } else if (keyH.leftPressed) {
            direction = "left";
            worldX -= speed; // Move left on the X-axis.
        } else {
            direction = "right";
            worldX += speed; // Move right.
        }

        // Increment spriteCounter to control the animation frame rate.
        spriteCounter++;

        // Toggle between two sprites every 12 frames to create walking animation.
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
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