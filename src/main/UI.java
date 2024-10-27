package main;

import java.awt.*;
import java.text.DecimalFormat;

// The UI class manages the display of various UI elements, including messages, keys, and end-game screens.
public class UI {

    // Reference to the GamePanel object for accessing game-related data and functionality.
    GamePanel gp;

    // Graphics2D instance used for rendering graphics within the UI class.
    Graphics2D g2;

    // Font objects for displaying text in the UI (Arial with size 40 and bold Arial with size 80).
    Font arial_40, arial_80B;

    // Boolean flag to indicate if a message should be displayed.
    public boolean messageOn = false;

    // String variable to store the message text.
    public String message = "";

    // Counter to track how long the message has been displayed.
    int messageCounter = 0;

    // Boolean flag to indicate if the game has finished.
    public boolean gameFinished = false;

    // Tracks the player's total time in the game and displays it on the screen
    double playTime;

    // Decimal format for displaying play time with two decimal places
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    // Constructor that initializes the UI, including fonts and the key image.
    public UI(GamePanel gp) {
        this.gp = gp;

        // Initialize fonts for UI text.
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

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
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        // Check the game state and draw elements accordingly.
        if (gp.gameState == gp.playState) {
            // Placeholder for future play-state UI elements.
        } else if (gp.gameState == gp.pauseState) {
            // Draws the pause screen when the game is paused.
            drawPauseScreen();
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

    // Calculates the x-coordinate for centering a text string on the screen.
    public int getXForCenteredText(String text) {
        // Get the pixel width of the text.
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        // Center the text by calculating an x-coordinate that aligns it to the middle of the screen.
        return gp.screenWidth / 2 - length / 2;
    }
}