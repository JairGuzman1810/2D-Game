package main;

import object.OBJ_Chest;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

// The UI class manages the display of various UI elements, including messages, keys, and end-game screens.
public class UI {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Chest.class.getName());

    // Reference to the GamePanel object for accessing game-related data and functionality.
    GamePanel gp;

    // Graphics2D instance used for rendering graphics within the UI class.
    Graphics2D g2;

    // Font objects for displaying text in the UI (Arial with size 40 and bold Arial with size 80).
    Font pixelOperator;

    // Boolean flag to indicate if a message should be displayed.
    public boolean messageOn = false;

    // String variable to store the message text.
    public String message = "";

    // Counter to track how long the message has been displayed.
    int messageCounter = 0;

    // Boolean flag to indicate if the game has finished.
    public boolean gameFinished = false;

    // Stores the current dialogue text to be displayed on the dialogue screen.
    public String currentDialogue = "";


    // Constructor that initializes the UI, including fonts and the key image.
    public UI(GamePanel gp) {
        this.gp = gp;


        try {
            // Initialize fonts for UI text.
            InputStream is = getClass().getResourceAsStream("/font/PixelOperator.ttf");
            assert is != null;
            pixelOperator = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load PixelOperator font", e);
        } catch (FontFormatException e) {
            logger.log(Level.WARNING, "Invalid font format for PixelOperator font", e);
        }


    }

    // Method to set a message to be displayed on the screen.
    public void showMessage(String text) {
        message = text;  // Set the message text.
        messageOn = true; // Turn on the message display.
    }

    // The draw method renders UI elements on the screen
    // It adapts based on the current game state, displaying specific elements for each state.
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // Set the font and color for the UI text display.
        g2.setFont(pixelOperator);
        g2.setColor(Color.white);

        // Check the game state and draw elements accordingly.
        if (gp.gameState == gp.playState) {
            // Placeholder for future play-state UI elements.
        } else if (gp.gameState == gp.pauseState) {
            // Draws the pause screen when the game is paused.
            drawPauseScreen();
        } else if (gp.gameState == gp.dialogueState) {
            // Draw the dialogue screen when the game is in dialogue state.
            drawDialogueScreen();
        }
    }


    // Draws the "PAUSED" message on the screen when the game is in the pause state.
    public void drawPauseScreen() {
        // Set the font size for the pause message.
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80));
        String text = "PAUSED";

        // Calculate the x and y positions for centering the text.
        int x = getXForCenteredText(text);
        int y = gp.screenHeight / 2;

        // Draw the centered pause message on the screen.
        g2.drawString(text, x, y);
    }

    // Draws the dialogue screen for displaying text dialogue in the game.
    public void drawDialogueScreen() {
        // Define the position and size of the dialogue window.
        int x = gp.tileSize * 2; // X position with padding from the left.
        int y = gp.tileSize / 2;  // Y position with padding from the top.

        int width = gp.screenWidth - (gp.tileSize * 4); // Width of the dialogue window.
        int height = gp.tileSize * 4; // Height of the dialogue window.

        // Draw the sub-window for the dialogue.
        drawSubWindow(x, y, width, height);

        // Set the font size for the dialogue text.
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32));
        x += gp.tileSize; // Adjust X position for padding within the window.
        y += gp.tileSize; // Adjust Y position for padding within the window.

        // Split the current dialogue into lines and draw each line in the window.
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y); // Draw the line at the specified position.
            y += 40; // Move Y position down for the next line.
        }
    }

    // Draws a rounded rectangle sub-window for displaying UI elements.
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 200); // Semi-transparent black for the window background.

        g2.setColor(c);
        // Fill the rounded rectangle to create the window background.
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255); // White color for the window border.
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5)); // Set the stroke for the border.

        // Draw the rounded rectangle border around the window.
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    // Calculates the x-coordinate for centering a text string on the screen.
    public int getXForCenteredText(String text) {
        // Get the pixel width of the text.
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        // Center the text by calculating an x-coordinate that aligns it to the middle of the screen.
        return gp.screenWidth / 2 - length / 2;
    }
}