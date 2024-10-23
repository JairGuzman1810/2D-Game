package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;

// The Player class extends the Entity class, inheriting common attributes like position and speed.
// It adds specific logic for handling player movement and drawing the player on the screen.
public class Player extends Entity {
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
    }


    // Sets the default values for the player's position and speed.
    public void setDefaultValues() {
        x = 100;  // Set starting X position.
        y = 100;  // Set starting Y position.
        speed = 4;  // Set the player's movement speed.
    }

    // Update method, called on every frame, checks for key inputs and updates the player's position.
    public void update() {
        // Check key inputs and move the player accordingly.
        if (keyH.upPressed) {
            y -= speed;  // Move up by subtracting from the Y coordinate.
        } else if (keyH.downPressed) {
            y += speed;  // Move down by adding to the Y coordinate.
        } else if (keyH.leftPressed) {
            x -= speed;  // Move left by subtracting from the X coordinate.
        } else if (keyH.rightPressed) {
            x += speed;  // Move right by adding to the X coordinate.
        }
    }

    // Draws the player on the screen as a white rectangle at the current position.
    public void draw(Graphics2D g2) {
        // Set the color to white for the player.
        g2.setColor(Color.white);

        // Draw the player as a rectangle using its current X and Y coordinates.
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
    }
}
