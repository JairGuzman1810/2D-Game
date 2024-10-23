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
    // This allows for better error tracking and debugging within the Player class.
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;

    // Reference to the KeyHandler, which captures the player's key inputs for movement.
    KeyHandler keyH;

    // Constructor initializes the Player with references to the game environment and key handler.
    // It also sets the player's default position and speed by calling setDefaultValues().
    public Player(GamePanel gp, KeyHandler keyH) {
        // Store references to the GamePanel and KeyHandler for use in the Player's methods.
        this.gp = gp;
        this.keyH = keyH;

        // Set the player's initial position and speed.
        setDefaultValues();
        getPlayerImage();
    }

    // Sets the default values for the player's position and speed.
    public void setDefaultValues() {
        // Set starting X and Y positions.
        x = 100;
        y = 100;
        // Set the player's movement speed.
        speed = 4;
        // Set the default direction to "down".
        direction = "down";
    }

    // The getPlayerImage method loads the images for each movement direction (up, down, left, right).
    // These images will be used to visually represent the player's movement in the game.
    public void getPlayerImage() {
        try {
            // Load images for the player's up, down, left, and right movement.
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_up_2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_down_2.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_left_2.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/boy_right_2.png")));
        } catch (IOException e) {
            // Log an error if image loading fails.
            logger.log(Level.SEVERE, "Failed to load player image", e);
        } catch (NullPointerException e) {
            // Log a warning if an image file is missing or not found.
            logger.log(Level.WARNING, "Image not found for player!", e);
        }
    }

    // Update method, called on every frame, checks for key inputs and updates the player's position.
    public void update() {
        // If no movement keys are pressed, return early and do nothing.
        if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed) {
            return;
        }

        // Check key inputs and move the player accordingly.
        if (keyH.upPressed) {
            direction = "up";    // Set the direction to "up".
            y -= speed;          // Move up by subtracting from the Y coordinate.
        } else if (keyH.downPressed) {
            direction = "down";  // Set the direction to "down".
            y += speed;          // Move down by adding to the Y coordinate.
        } else if (keyH.leftPressed) {
            direction = "left";  // Set the direction to "left".
            x -= speed;          // Move left by subtracting from the X coordinate.
        } else {
            direction = "right"; // Set the direction to "right".
            x += speed;          // Move right by adding to the X coordinate.
        }

        // Increment spriteCounter to track the animation frame duration.
        spriteCounter++;

        // After 12 frames, alternate between sprite 1 and 2 for animation.
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1; // Toggle spriteNum between 1 and 2.
            // Reset spriteCounter to ensure smooth animation transitions.
            spriteCounter = 0;
        }
    }


    // The draw method selects and draws the appropriate image for the player's current direction.
    public void draw(Graphics2D g2) {
        // Use a switch expression to select the image based on the player's current direction.
        // The selected image will depend on the direction and the sprite number (1 or 2).
        BufferedImage image = switch (direction) {
            case "up" -> // If the direction is "up"
                // Select the appropriate image based on the sprite number.
                    (spriteNum == 1) ? up1 : up2; // Use the ternary operator for clarity.
            case "down" -> // If the direction is "down"
                    (spriteNum == 1) ? down1 : down2; // Select the corresponding image.
            case "left" -> // If the direction is "left"
                    (spriteNum == 1) ? left1 : left2; // Choose the appropriate image for left.
            case "right" -> // If the direction is "right"
                    (spriteNum == 1) ? right1 : right2; // Select the corresponding image for right.
            default -> // If the direction is not recognized
                    down1; // Fallback to a default downward-facing image for safety.
        };

        // Draw the selected image at the player's current position (x, y) with the specified tile size.
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }


}
