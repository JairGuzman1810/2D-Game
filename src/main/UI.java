package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

// The UI class manages the display of various UI elements, including messages, keys, and end-game screens.
public class UI {

    // Reference to the GamePanel object for accessing game-related data and functionality.
    GamePanel gp;

    // Font objects for displaying text in the UI (Arial with size 40 and bold Arial with size 80).
    Font arial_40, arial_80B;

    // Image object to store the graphic of the key.
    BufferedImage keyImage;

    // Boolean flag to indicate if a message should be displayed.
    public boolean messageOn = false;

    // String variable to store the message text.
    public String message = "";

    // Counter to track how long the message has been displayed.
    int messageCounter = 0;

    // Boolean flag to indicate if the game has finished.
    public boolean gameFinished = false;

    // Constructor that initializes the UI, including fonts and the key image.
    public UI(GamePanel gp) {
        this.gp = gp;

        // Initialize fonts for UI text.
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        // Load the key image from the key object.
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }

    // Method to set a message to be displayed on the screen.
    public void showMessage(String text) {
        message = text;  // Set the message text.
        messageOn = true; // Turn on the message display.
    }

    // The draw method renders UI elements on the screen, including keys, messages, and end-game screens.
    public void draw(Graphics2D g2) {

        // Check if the game has finished and display the end-game message.
        if (gameFinished) {

            // Set the font and color for the text.
            g2.setFont(arial_40);
            g2.setColor(Color.white);

            // Declare variables for the text content, its length, and its position on the screen.
            String text;
            int textLength;
            int x, y;

            // Display the message for finding the treasure.
            text = "You found the treasure!";

            // Get the width of the text for centering it on the screen.
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            // Calculate the x and y positions for centering the text.
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenHeight / 2 - (gp.tileSize * 3);

            // Draw the treasure-found message on the screen.
            g2.drawString(text, x, y);

            // Change the font to a larger, bold font for the congratulations message.
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);

            // Display the congratulations message.
            text = "Congratulations!";

            // Get the width of the text for centering it.
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            // Calculate the x and y positions for centering the text.
            x = gp.screenWidth / 2 - textLength / 2;
            y = gp.screenHeight / 2 + (gp.tileSize * 2);

            // Draw the congratulations message on the screen.
            g2.drawString(text, x, y);

            // Stop the game thread as the game has ended.
            gp.gameThread = null;

        } else {
            // Set the font and color for displaying the key count and messages.
            g2.setFont(arial_40);
            g2.setColor(Color.white);

            // Draw the key image and the player's key count on the screen.
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x " + gp.player.hasKey, 74, 65);

            // If a message is active, display it on the screen.
            if (messageOn) {
                // Set a smaller font for the message.
                g2.setFont(g2.getFont().deriveFont(30F));

                // Draw the message on the screen.
                g2.drawString(message, gp.tileSize / 2, gp.tileSize * 5);

                // Increment the message counter to track how long the message has been displayed.
                messageCounter++;

                // After 120 frames, remove the message from the screen. (approximately 2 seconds at 60 FPS).
                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }
}
