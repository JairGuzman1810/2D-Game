package main;

import entity.Entity;
import object.OBJ_CoinBronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// The UI class manages the display of various UI elements, including messages, keys, and end-game screens.
public class UI {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(UI.class.getName());

    // Reference to the GamePanel object for accessing game-related data and functionality.
    GamePanel gp;

    // Graphics2D instance used for rendering graphics within the UI class.
    Graphics2D g2;

    // Font objects for displaying text in the UI (Arial with size 40 and bold Arial with size 80).
    public Font pixelOperator;

    // Images representing full, half, and blank hearts for displaying player life status.
    BufferedImage heart_full, heart_half, heart_blank;
    // Images representing full and blank crystals for displaying player mana status.
    BufferedImage crystal_full, crystal_blank;
    // Image representing a coin, used to display beside the item's value during transactions.
    BufferedImage coin;


    // Boolean flag to indicate if a message should be displayed.
    public boolean messageOn = false;

    // List to hold messages to be displayed
    ArrayList<String> message = new ArrayList<>();
    // List to keep track of the duration each message has been displayed
    ArrayList<Integer> messageCounter = new ArrayList<>();
    // Boolean flag to indicate if the game has finished.
    public boolean gameFinished = false;

    // Stores the current dialogue text to be displayed on the dialogue screen.
    public String currentDialogue = "";

    // Stores the selected command index on the title screen.
    public int commandNum = 0;
    // Current player column index for item slots in the inventory
    public int playerSlotCol = 0;
    // Current player row index for item slots in the inventory
    public int playerSlotRow = 0;
    // Current column index of the selected slot in the NPC's inventory, used for visualizing the selected item.
    public int npcSlotCol = 0;
    // Current row index of the selected slot in the NPC's inventory, used for visualizing the selected item.
    public int npcSlotRow = 0;
    // Substate variable to manage internal states within certain interfaces, such as trading or the pause menu.
    int subState = 0;

    // Counter for controlling the screen transition effect, such as gradually darkening the screen.
    int counter = 0;

    // Reference to the NPC whose inventory is currently being accessed in the UI, allowing interaction with their items.
    public Entity npc;

    // Tracks the current character index for displaying text letter by letter.
    int charIndex = 0;
    // Accumulates characters to form the dialogue line being displayed.
    String combinedText = "";


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

        // Create an instance of OBJ_Heart to load heart images.
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;   // Full heart image.
        heart_half = heart.image2;  // Half heart image.
        heart_blank = heart.image3;  // Blank heart image.

        // Create an instance of OBJ_ManaCrystal to load crystal images.
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;   // Full crystal image.
        crystal_blank = crystal.image2; // Blank crystal image.

        // Create an instance of OBJ_CoinBronze to load coin image.
        Entity coinBronze = new OBJ_CoinBronze(gp);
        coin = coinBronze.down1; // Coin image.

    }

    // Adds a new message to be displayed on the screen.
    public void addMessage(String text) {
        message.add(text); // Add the message to the list
        messageCounter.add(0); // Initialize the counter for this message
    }

    // The draw method renders UI elements on the screen
    // It adapts based on the current game state, displaying specific elements for each state.
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // Set the font and color for the UI text display.
        g2.setFont(pixelOperator);
        g2.setColor(Color.white);

        // Check the game state and draw elements accordingly.
        if (gp.gameState == gp.titleState) {
            // Draws the title screen when in title state.
            drawTitleScreen();
        } else if (gp.gameState == gp.playState) {
            // Placeholder for future play-state UI elements.
            drawPlayerLife();
            drawMessage();
        } else if (gp.gameState == gp.pauseState) {
            // Draws the pause screen when the game is paused.
            drawPlayerLife();
            drawPauseScreen();
        } else if (gp.gameState == gp.dialogueState) {
            // Draw the dialogue screen when the game is in dialogue state.
            drawDialogueScreen();
        } else if (gp.gameState == gp.characterState) {
            // Draw the character stats screen when the game is in character state.
            drawCharacterScreen();
            drawInventory(gp.player, true);
        } else if (gp.gameState == gp.optionsState) {
            // Draw the character options screen when the game is in option state.
            drawOptionsScreen();
        } else if (gp.gameState == gp.gameOverState) {
            // Draw the game over screen when the game is in game over state.
            drawGameOverScreen();
        } else if (gp.gameState == gp.transitionState) {
            // Draw the transition when the game is in transition state.
            drawTransition();
        } else if (gp.gameState == gp.tradeState) {
            // Draw the trade screen when the game is in trade state.
            drawTradeScreen();
        } else if (gp.gameState == gp.sleepState) {
            // Draw the sleep screen when the game is in sleep state.
            drawSleepScreen();
        }
    }

    // Draws the player's life status on the screen using heart images.
    public void drawPlayerLife() {
        int x = gp.tileSize / 2; // Starting X position for drawing hearts.
        int y = gp.tileSize / 2; // Starting Y position for drawing hearts.
        int i = 0; // Counter for heart images.

        // Draw max life
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null); // Draw blank heart for max life.
            i++;
            x += gp.tileSize; // Move X position for the next heart.
        }

        x = gp.tileSize / 2; // Reset X position for drawing current life.
        i = 0; // Reset counter for current life.

        // Draw current life
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null); // Draw half heart if player has only half a life.
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null); // Draw full heart for each life.
            }
            i++;
            x += gp.tileSize; // Move X position for the next heart.
        }

        // Draw max mana
        x = gp.tileSize / 2 - 5; // Starting X position for drawing crystals.
        y = (int) (gp.tileSize * 1.5); // Starting Y position for drawing crystals.
        i = 0; // Reset counter for max mana.

        while (i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null); // Draw blank crystal for max mana.
            i++;
            x += 35; // Move X position for the next crystal.
        }

        // Draw mana
        x = gp.tileSize / 2 - 5; // Reset X position for drawing current mana.
        i = 0; // Reset counter for current life.


        // Draw current life
        while (i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null); // Draw crystal if player has only half a complete mana slot.
            i++;
            x += 35; // Move X position for the next crystal.
        }
    }

    // Draws the messages on the screen.
    // The messages are displayed at a specific position and fade out after a certain duration.
    public void drawMessage() {
        int messageX = gp.tileSize; // X coordinate for message drawing
        int messageY = gp.tileSize * 4; // Y coordinate for message drawing
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F)); // Set the font for the messages

        // List to keep track of indices of messages that need to be removed
        ArrayList<Integer> indicesToRemove = new ArrayList<>();

        // Iterate over all messages
        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) { // Check if the message is not null
                g2.setColor(Color.black); // Set color for shadow effect
                g2.drawString(message.get(i), messageX + 2, messageY + 2); // Draw shadow

                g2.setColor(Color.white); // Set color for the actual message
                g2.drawString(message.get(i), messageX, messageY); // Draw message

                // Update the message display duration counter
                int counter = messageCounter.get(i) + 1; // Increment the counter
                messageCounter.set(i, counter); // Update the counter in the list
                messageY += 50; // Move the Y position down for the next message

                // Check if the message has been displayed for longer than 180 frames
                if (messageCounter.get(i) > 180) {
                    indicesToRemove.add(i); // Mark this message for removal
                }
            }
        }

        // Remove marked messages and counters in reverse order to avoid shifting issues
        for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
            int index = indicesToRemove.get(i);
            message.remove(index); // Remove the message
            messageCounter.remove(index); // Remove the corresponding counter
        }
    }

    // Renders the title screen UI elements, including game title, main character image, and menu options.
    public void drawTitleScreen() {
        // Sets font for the title text.
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96f));
        String text = "Blue Boy Adventure";

        // Calculates position to center the title text and adds a shadow effect.
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;
        g2.setColor(Color.gray);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // Displays the main character image below the title.
        x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.left2, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // Sets font for menu options.
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        // Draws "NEW GAME" option with selection indicator.
        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += (int) (gp.tileSize * 3.5);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        // Draws "LOAD GAME" option with selection indicator.
        text = "LOAD GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        // Draws "QUIT" option with selection indicator.
        text = "QUIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
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

    // Displays the dialogue window and handles the logic for showing text to the player.
    public void drawDialogueScreen() {
        // Define dimensions and position for the dialogue window.
        int x = gp.tileSize * 3; // Left padding.
        int y = gp.tileSize / 2; // Top padding.
        int width = gp.screenWidth - (gp.tileSize * 6); // Window width.
        int height = gp.tileSize * 4; // Window height.

        drawSubWindow(x, y, width, height); // Draw the dialogue background.

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32)); // Set font for text.
        x += gp.tileSize; // Adjust x for inner padding.
        y += gp.tileSize; // Adjust y for inner padding.

        // Check if the current dialogue line exists.
        if (npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {
            // Convert the dialogue line to a character array for letter-by-letter display.
            char[] characters = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();

            if (charIndex < characters.length) {
                gp.playSE(17); // Play a sound effect for dialogue typing.
                String s = String.valueOf(characters[charIndex]); // Get the current character.
                combinedText += s; // Append the character to the displayed text.
                currentDialogue = combinedText; // Update the displayed dialogue text.
                charIndex++; // Move to the next character.
            }

            if (gp.keyH.enterPressed) { // Advance dialogue when the Enter key is pressed.
                charIndex = 0; // Reset the character index for the next line.
                combinedText = ""; // Clear accumulated text.
                if (gp.gameState == gp.dialogueState) {
                    npc.dialogueIndex++; // Move to the next dialogue line.
                    gp.keyH.enterPressed = false; // Reset Enter key state.
                }
            }
        } else {
            npc.dialogueIndex = 0; // Reset dialogue index after finishing all lines.

            if (gp.gameState == gp.dialogueState) {
                gp.gameState = gp.playState; // Return to gameplay state after dialogue.
            }
        }

        // Render each line of the current dialogue, splitting by line breaks.
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y); // Draw the line at the specified position.
            y += 40; // Adjust y position for the next line.
        }
    }

    // Draws the character screen displaying the player's stats, including health, mana, and abilities.
    public void drawCharacterScreen() {
        // Define the position and size of the character information window.
        int x = gp.tileSize * 2; // X position with padding from the left edge of the screen.
        int y = gp.tileSize;  // Y position with padding from the top of the screen.

        int width = gp.tileSize * 5; // Width of the character stats window.
        int height = gp.tileSize * 10; // Height of the character stats window.

        // Draw the sub-window for displaying character stats.
        drawSubWindow(x, y, width, height);

        // Set the font and color for displaying text in the character screen.
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // Define the starting position for the text labels.
        int textX = x + 20;
        int textY = y + gp.tileSize;
        final int lineHeight = 35;

        // Display the labels for character stats (Level, Life, Mana, etc.)
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("EXP", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coins", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);

        // Define the position for displaying the corresponding values of the character stats.
        int tailX = x + width - 30;  // X position for aligning the values to the right.
        textY = y + gp.tileSize;  // Reset the Y position for the values.

        // Display the values for each stat next to the corresponding label.
        String value;

        value = String.valueOf(gp.player.level); // Level value
        textX = getXForAlignToRight(value, tailX); // Align value to the right
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = gp.player.life + "/" + gp.player.maxLife; // Life value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = gp.player.mana + "/" + gp.player.maxMana; // Mana value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength); // Strength value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity); // Dexterity value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack); // Attack value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense); // Defense value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp); // EXP value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp); // Next level EXP value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin); // Coins value
        textX = getXForAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Display the player's weapon and shield images.
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 24, null);
    }

    // Draws an inventory interface for a specified entity (player or NPC), including item slots,
    // an optional selection cursor, and item descriptions.
    // The cursor is displayed only if the 'cursor' parameter is true.
    public void drawInventory(Entity entity, boolean cursor) {

        // Initialize frame position, dimensions, and slot tracking variables
        int frameX;
        int frameY;
        int frameWidth;
        int frameHeight;
        int slotCol;
        int slotRow;

        // Set parameters based on whether the entity is the player or an NPC
        if (entity == gp.player) {
            frameX = gp.tileSize * 12; // X position of the player's inventory frame
            frameY = gp.tileSize; // Y position of the player's inventory frame
            frameWidth = gp.tileSize * 6; // Width of the player's inventory frame
            frameHeight = gp.tileSize * 5; // Height of the player's inventory frame
            slotCol = playerSlotCol; // Column position of the player's cursor
            slotRow = playerSlotRow; // Row position of the player's cursor
        } else {
            frameX = gp.tileSize * 2; // X position of the NPC's inventory frame
            frameY = gp.tileSize; // Y position of the NPC's inventory frame
            frameWidth = gp.tileSize * 6; // Width of the NPC's inventory frame
            frameHeight = gp.tileSize * 5; // Height of the NPC's inventory frame
            slotCol = npcSlotCol; // Column position of the NPC's cursor
            slotRow = npcSlotRow; // Row position of the NPC's cursor
        }

        drawSubWindow(frameX, frameY, frameWidth, frameHeight); // Draw the inventory frame

        // Define starting positions and slot sizes
        final int slotXStart = frameX + 20; // Initial X position for item slots
        final int slotYStart = frameY + 20; // Initial Y position for item slots
        int slotX = slotXStart; // Current X position for slots
        int slotY = slotYStart; // Current Y position for slots
        int slotSize = gp.tileSize + 3; // Size of each slot, including padding

        // Iterate through the entity's inventory and draw each item
        for (int i = 0; i < entity.inventory.size(); i++) {
            // Highlight equipped items (e.g., current weapon, shield or light)
            if (entity.inventory.get(i) == entity.currentWeapon || entity.inventory.get(i) == entity.currentShield || entity.inventory.get(i) == entity.currentLight) {
                g2.setColor(new Color(240, 190, 90)); // Highlight color
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null); // Draw item image

// Display the amount of the item if greater than 1.
            if (entity.inventory.get(i).amount > 1) {
                g2.setFont(g2.getFont().deriveFont(32f)); // Set font size for the amount.

                // Calculate X and Y positions for the amount text.
                int amountX = getXForAlignToRight(String.valueOf(entity.inventory.get(i).amount), slotX + 44);
                int amountY = slotY + gp.tileSize;

                // Draw a shadow for the text to enhance visibility.
                g2.setColor(new Color(60, 60, 60));
                g2.drawString(String.valueOf(entity.inventory.get(i).amount), amountX, amountY);

                // Draw the amount in white to display it clearly.
                g2.setColor(Color.white);
                g2.drawString(String.valueOf(entity.inventory.get(i).amount), amountX - 3, amountY - 3);
            }

            slotX += slotSize; // Move to the next horizontal slot

            // Move to the next row after every 5 items
            if ((i + 1) % 5 == 0) {
                slotX = slotXStart; // Reset X position for the next row
                slotY += slotSize; // Move down to the next row
            }
        }

        // Draw the cursor and description if enabled
        if (cursor) {
            // Calculate cursor position based on selected slot
            int cursorX = slotXStart + (slotSize * slotCol); // X position of the cursor
            int cursorY = slotYStart + (slotSize * slotRow); // Y position of the cursor
            int cursorWidth = gp.tileSize; // Width of the cursor
            int cursorHeight = gp.tileSize; // Height of the cursor

            // Draw the cursor to indicate the selected item
            g2.setColor(Color.white); // Cursor color
            g2.setStroke(new BasicStroke(3)); // Cursor stroke width
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10); // Draw cursor outline

            // Define description frame position and dimensions
            int dFrameY = frameY + frameHeight; // Y position of the description frame
            int dFrameHeight = gp.tileSize * 3; // Height of the description frame

            // Set text position and font for the description
            int textX = frameX + 20; // X position for the description text
            int textY = dFrameY + gp.tileSize; // Y position for the description text
            g2.setFont(g2.getFont().deriveFont(28F)); // Font size for the description text

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow); // Get the index of the selected item

            // Draw the description frame and text if a valid item is selected
            if (itemIndex < entity.inventory.size()) {
                drawSubWindow(frameX, dFrameY, frameWidth, dFrameHeight); // Draw description frame
                // Split item description into lines and draw each line
                for (String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY); // Draw each line of the description
                    textY += 32; // Move down to the next line
                }
            }
        }
    }


    // Draws the options screen, displaying various game settings such as full screen toggle,
    // volume controls, and game controls.
    public void drawOptionsScreen() {
        // Set the color for drawing text to white
        g2.setColor(Color.white);

        // Set the font for the options screen text with a size of 32
        g2.setFont(g2.getFont().deriveFont(32F));

        // Define dimensions for the options sub-window (position and size)
        int frameX = gp.tileSize * 6;       // X-position for the frame
        int frameY = gp.tileSize;           // Y-position for the frame
        int frameWidth = gp.tileSize * 8;   // Frame width, spanning 8 tiles
        int frameHeight = gp.tileSize * 10; // Frame height, spanning 10 tiles

        // Draw the main options sub-window with specified dimensions
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Determine which options sub-screen to display based on the `subState` value
        switch (subState) {
            case 0 -> options_top(frameX, frameY);                   // Display the main options menu
            case 1 -> options_fullScreenNotification(frameX, frameY); // Display full-screen notification
            case 2 -> options_controls(frameX, frameY);              // Display control options
            case 3 -> options_endGameConfirmation(frameX, frameY);   // Display end-game confirmation
        }

        // Reset the enter key press flag after processing input on the options screen
        gp.keyH.enterPressed = false;
    }


    // Displays the main options screen, allowing the player to adjust settings such as
    // full screen toggle, music and sound effect volumes, control options, and an option
    // to end the game
    public void options_top(int frameX, int frameY) {
        int textX;
        int textY;

        // Draw the main "Options" title, centered at the top of the frame
        String text = "Options";
        textX = getXForCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        // Draw "Full Screen" toggle option
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2.drawString("Full Screen", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY); // Draws a pointer indicating selection
            if (gp.keyH.enterPressed) {
                gp.fullScreenOn = !gp.fullScreenOn; // Toggles full screen
                subState = 1; // Show confirmation sub-window
            }
        }

        // Draw "Music" volume control option
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY); // Pointer for music selection
        }

        // Draw "SE" (Sound Effects) volume control option
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY); // Pointer for SE selection
        }

        // Draw "Control" option for viewing or changing controls
        textY += gp.tileSize;
        g2.drawString("Control", textX, textY);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY); // Pointer for control option
            if (gp.keyH.enterPressed) {
                subState = 2; // Move to control configuration screen
                commandNum = 0; // Reset command index for the control menu
            }
        }

        // Draw "End Game" option to confirm ending the current game
        textY += gp.tileSize;
        g2.drawString("End Game", textX, textY);
        if (commandNum == 4) {
            g2.drawString(">", textX - 25, textY); // Pointer for end game selection
            if (gp.keyH.enterPressed) {
                subState = 3;   // Move to end game confirmation screen
                commandNum = 0; // Reset command index for the control menu
            }
        }

        // Draw "Back" option to exit options menu and return to game
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNum == 5) {
            g2.drawString(">", textX - 25, textY); // Pointer for back option
            if (gp.keyH.enterPressed) {
                gp.gameState = gp.playState; // Switch back to play state
                commandNum = 0;              // Reset command index for the control menu
            }
        }

        // Draw full screen checkbox to indicate current full screen state
        textX = (int) (frameX + gp.tileSize * 4.5);
        textY = frameY + gp.tileSize * 2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24); // Checkbox outline
        if (gp.fullScreenOn) {
            g2.fillRect(textX, textY, 24, 24); // Filled if full screen is enabled
        }

        // Draw volume bar for music, scaled to current music volume
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24); // Outline of music volume bar
        int volumeWidth = 24 * gp.music.volumeScale; // Width based on volume scale
        g2.fillRect(textX, textY, volumeWidth, 24); // Fill based on current volume

        // Draw volume bar for sound effects, scaled to current SE volume
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24); // Outline of SE volume bar
        volumeWidth = 24 * gp.se.volumeScale; // Width based on SE volume scale
        g2.fillRect(textX, textY, volumeWidth, 24); // Fill based on current volume

        // Save game configuration settings.
        gp.config.saveConfig();
    }

    // Displays a notification informing the player that the full screen change
// will take effect after restarting the game.
    public void options_fullScreenNotification(int frameX, int frameY) {
        // Calculate the position for the text to be drawn based on the frame's position
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        // The message that will inform the player about the full screen change
        currentDialogue = "The change will take \neffect after restarting \nthe game.";

        // Loop through each line of the current dialogue and draw it at the appropriate position
        // Split the message into multiple lines if there is a newline character
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);  // Draw the text at the calculated position
            textY += 40;  // Increase the Y position to leave space for the next line
        }

        // Position the "Back" option at the bottom of the screen
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);  // Draw the "Back" text option

        // If the "Back" option is selected (commandNum == 0), highlight it with a ">" symbol
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY); // Indicate the selected option with a pointer
            if (gp.keyH.enterPressed) {  // Check if the enter key is pressed
                subState = 0;  // Change the subState to return to the main options menu
            }
        }
    }

    // Displays the controls menu where the player can view the key mappings for various actions.
    public void options_controls(int frameX, int frameY) {
        // Set up initial position for centered title
        int textX;
        int textY;

        // Draw the title "Controls" at the center of the options screen
        String text = "Controls";
        textX = getXForCenteredText(text);  // Get X position to center the text
        textY = frameY + gp.tileSize;  // Set the Y position below the frame
        g2.drawString(text, textX, textY);  // Draw the title

        // Set initial position for control options listing
        textX = frameX + gp.tileSize;  // Set the X position for the control labels
        textY += gp.tileSize;  // Start the Y position slightly below the title

        // List all control actions with their corresponding keys
        g2.drawString("Move", textX, textY);  // Draw the action "Move"
        textY += gp.tileSize;  // Move down for the next option

        g2.drawString("Confirm/Attack", textX, textY);  // Draw the action "Confirm/Attack"
        textY += gp.tileSize;  // Move down for the next option

        g2.drawString("Shoot/Cast", textX, textY);  // Draw the action "Shoot/Cast"
        textY += gp.tileSize;  // Move down for the next option

        g2.drawString("Character Screen", textX, textY);  // Draw the action "Character Screen"
        textY += gp.tileSize;  // Move down for the next option

        g2.drawString("Pause", textX, textY);  // Draw the action "Pause"
        textY += gp.tileSize;  // Move down for the next option

        g2.drawString("Options", textX, textY);  // Draw the action "Options"

        // Set position for the key mappings
        textX = frameX + gp.tileSize * 6;  // Adjust X for the key display area
        textY = frameY + gp.tileSize * 2;  // Start the Y position slightly below the control actions

        // Draw key mappings corresponding to the listed controls
        g2.drawString("WASD", textX, textY);  // Draw the key for "Move"
        textY += gp.tileSize;  // Move down for the next key mapping

        g2.drawString("ENTER", textX, textY);  // Draw the key for "Confirm/Attack"
        textY += gp.tileSize;  // Move down for the next key mapping

        g2.drawString("F", textX, textY);  // Draw the key for "Shoot/Cast"
        textY += gp.tileSize;  // Move down for the next key mapping

        g2.drawString("C", textX, textY);  // Draw the key for "Character Screen"
        textY += gp.tileSize;  // Move down for the next key mapping

        g2.drawString("P", textX, textY);  // Draw the key for "Pause"
        textY += gp.tileSize;  // Move down for the next key mapping

        g2.drawString("ESC", textX, textY);  // Draw the key for "Options"

        // Draw "Back" option at the bottom to return to previous menu
        textX = frameX + gp.tileSize;  // Adjust X position for "Back" option
        textY = frameY + gp.tileSize * 9;  // Set Y position for the "Back" option
        g2.drawString("Back", textX, textY);  // Draw the "Back" text option

        // If the "Back" option is selected (commandNum == 0), highlight it with a ">" symbol
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);  // Indicate the selected option with a pointer
            if (gp.keyH.enterPressed) {  // Check if the enter key is pressed
                subState = 0;  // Return to main options menu
                commandNum = 3;  // Reset the command number to default control option
            }
        }
    }

    // Displays a confirmation window asking the player if they want to quit the game and return to the title screen.
    public void options_endGameConfirmation(int frameX, int frameY) {
        // Set the initial position for the text
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        // Dialogue for confirmation
        currentDialogue = "Quit the game and return \nto the title screen?";

        // Split the current dialogue into lines and draw each line at the specified position
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);  // Draw each line of the dialogue
            textY += 40;  // Move Y position down for the next line
        }

        // Draw "Yes" option and handle input to quit the game
        String text = "Yes";
        textX = getXForCenteredText(text);  // Center the text
        textY += gp.tileSize * 3;  // Position below the dialogue text
        g2.drawString(text, textX, textY);  // Draw the "Yes" option
        if (commandNum == 0) {  // If "Yes" is selected
            g2.drawString(">", textX - 25, textY);  // Highlight the selected option
            if (gp.keyH.enterPressed) {  // Check if the enter key is pressed
                subState = 0;  // Reset subState
                gp.gameState = gp.titleState;  // Change game state to title screen
                gp.resetGame(true); // Reset game
                gp.stopMusic(); // Stop music
            }
        }

        // Draw "No" option and handle input to cancel quitting
        text = "No";
        textX = getXForCenteredText(text);  // Center the "No" text
        textY += gp.tileSize;  // Position below the "Yes" option
        g2.drawString(text, textX, textY);  // Draw the "No" option
        if (commandNum == 1) {  // If "No" is selected
            g2.drawString(">", textX - 25, textY);  // Highlight the selected option
            if (gp.keyH.enterPressed) {  // Check if the enter key is pressed
                subState = 0;  // Reset subState
                commandNum = 4;  // Reset commandNum to a previous value (or default)
            }
        }
    }

    // Draws the game over screen with an option to retry or quit to the title screen.
    public void drawGameOverScreen() {
        // Draw a semi-transparent black overlay to dim the screen during the game over state.
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;

        // Set the font and size for the game over text.
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over"; // Game over message text
        // Shadow effect for the "Game Over" text
        g2.setColor(Color.black);
        x = getXForCenteredText(text); // Center the text horizontally on the screen
        y = gp.tileSize * 4; // Set the Y position below the top of the screen
        g2.drawString(text, x, y);
        // Main text color
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4); // Draw the main "Game Over" text slightly offset to create a shadow effect

        // Set the font and size for the retry option.
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry"; // Retry option text
        x = getXForCenteredText(text); // Center the retry option text
        y += gp.tileSize * 4; // Set the Y position below the "Game Over" text
        g2.drawString(text, x, y); // Draw the "Retry" text
        // Highlight the selected option (if commandNum == 0, show the ">" symbol).
        if (commandNum == 0) {
            g2.drawString(">", x - 40, y); // Draw a pointer to indicate the selected option
        }

        // Draw the "Quit" option text for exiting to the title screen.
        text = "Quit"; // Quit option text
        x = getXForCenteredText(text); // Center the quit option text
        y += 55; // Set the Y position below the retry option
        g2.drawString(text, x, y); // Draw the "Quit" text
        // Highlight the selected option (if commandNum == 1, show the ">" symbol).
        if (commandNum == 1) {
            g2.drawString(">", x - 40, y); // Draw a pointer to indicate the selected option
        }
    }

    // This method handles the screen transition effect when changing game states.
    public void drawTransition() {

        counter++; // Increment the transition counter to track the fade-in effect progress

        // Set the color for the transition screen (black), with an alpha value that increases as the counter grows.
        // This creates a fade-in effect, starting from fully transparent to fully opaque.
        g2.setColor(new Color(0, 0, 0, counter * 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight); // Draw a full-screen rectangle to cover the entire screen with the transition color

        // Once the counter reaches 50 (indicating the transition effect is complete)
        if (counter == 50) {
            counter = 0; // Reset the counter for the next transition

            // Change the game state to the play state, moving from the transition state to the active game state
            gp.gameState = gp.playState;

            // Set the current map to the temporary map stored in the event handler (usually used for state changes like teleportation)
            gp.currentMap = gp.eHandler.tempMap;

            // Set the player's position based on the temporary column and row provided by the event handler
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;

            // Update the previous event coordinates to reflect the player's new position
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }
    }

    // This method controls the drawing of the trade screen based on the current subState (Buy, Sell, or Select)
    public void drawTradeScreen() {

        // Switch between different trade states (select, buy, sell) based on the current subState
        switch (subState) {
            case 0 -> trade_select(); // Display the trade selection menu
            case 1 -> trade_buy();    // Display the buying screen
            case 2 -> trade_sell();   // Display the selling screen
        }

        // Reset the 'enterPressed' flag after the screen update
        gp.keyH.enterPressed = false;
    }

    // This method draws the trade selection menu (Buy, Sell, Leave)
    public void trade_select() {

        npc.dialogueSet = 0;

        drawDialogueScreen(); // Draw the dialogue screen as the background

        // Set up the trade selection window's position and size
        int x = gp.tileSize * 15;
        int y = gp.tileSize * 4;
        int width = gp.tileSize * 3;
        int height = (int) (gp.tileSize * 3.5);

        drawSubWindow(x, y, width, height); // Draw the trade selection window

        // Adjust position to draw the options inside the trade window
        x += gp.tileSize;
        y += gp.tileSize;

        // Draw the "Buy" option and highlight it if it's the selected option
        g2.drawString("Buy", x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - 24, y); // Show a cursor to indicate selection
            if (gp.keyH.enterPressed) { // If Enter is pressed, change subState to Buy
                subState = 1;
            }
        }

        // Draw the "Sell" option and highlight it if it's the selected option
        y += gp.tileSize;
        g2.drawString("Sell", x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - 24, y); // Show a cursor to indicate selection
            if (gp.keyH.enterPressed) { // If Enter is pressed, change subState to Sell
                subState = 2;
            }
        }

        // Draw the "Leave" option and handle the logic for exiting the trade screen
        y += gp.tileSize;
        g2.drawString("Leave", x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - 24, y); // Show a cursor to indicate selection
            if (gp.keyH.enterPressed) { // If Enter is pressed, exit the trade and return to dialogue state
                commandNum = 0;
                npc.startDialogue(npc, 1);
            }
        }
    }

    // This method handles the drawing of the buy screen during a trade
    public void trade_buy() {

        // Draw both the NPC's inventory and the player's inventory
        drawInventory(npc, true);  // Draw the NPC's inventory (items for sale)
        drawInventory(gp.player, false); // Draw the player's inventory (items they own)

        // Draw the hint window for returning (ESC key)
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] back", x + 24, y + 60);

        // Draw the window displaying the player's coin balance
        x = gp.tileSize * 12;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your Coins: " + gp.player.coin, x + 24, y + 60);

        // Get the index of the item selected for purchase in the NPC's inventory
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);

        // If the item index is valid (item exists in the NPC's inventory)
        if (itemIndex < npc.inventory.size()) {
            // Set up the price window for the selected item
            x = (int) (gp.tileSize * 5.5);
            y = (int) (gp.tileSize * 5.5);
            width = (int) (gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null); // Draw the coin image

            // Get the price of the selected item
            int price = npc.inventory.get(itemIndex).price;
            String text = String.valueOf(price);

            // Draw the price text aligned to the right
            x = getXForAlignToRight(text, gp.tileSize * 8 - 20);
            g2.drawString(text, x, y + 34);

            // If Enter is pressed, attempt to buy the item
            if (gp.keyH.enterPressed) {
                // Check if the player has enough coins
                if (npc.inventory.get(itemIndex).price > gp.player.coin) {
                    subState = 0;
                    npc.startDialogue(npc, 2); // Not enough coins
                } else {

                    if (gp.player.canObtainItem(npc.inventory.get(itemIndex))) {
                        gp.player.coin -= npc.inventory.get(itemIndex).price; // Deduct the price from player's coins
                    } else {
                        subState = 0;
                        npc.startDialogue(npc, 3); // Inventory is full
                    }

                }

            }
        }
    }

    // This method handles the drawing of the sell screen during a trade
    public void trade_sell() {

        // Draw the player's inventory, highlighting the items they can sell
        drawInventory(gp.player, true);

        // Draw the hint window for returning (ESC key)
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 9;
        int width = gp.tileSize * 6;
        int height = gp.tileSize * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] back", x + 24, y + 60);

        // Draw the window displaying the player's coin balance
        x = gp.tileSize * 12;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your Coins: " + gp.player.coin, x + 24, y + 60);

        // Get the index of the item selected for sale in the player's inventory
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);

        // If the item index is valid (item exists in the player's inventory)
        if (itemIndex < gp.player.inventory.size()) {
            // Set up the price window for the selected item
            x = (int) (gp.tileSize * 15.5);
            y = (int) (gp.tileSize * 5.5);
            width = (int) (gp.tileSize * 2.5);
            height = gp.tileSize;
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x + 10, y + 8, 32, 32, null); // Draw the coin image

            // Calculate the price for selling the item (half of the original price)
            int price = gp.player.inventory.get(itemIndex).price / 2;
            String text = String.valueOf(price);

            // Draw the price text aligned to the right
            x = getXForAlignToRight(text, gp.tileSize * 18 - 20);
            g2.drawString(text, x, y + 34);

            // If Enter is pressed, attempt to sell the item
            if (gp.keyH.enterPressed) {
                // Check if the player is trying to sell an equipped item (weapon or shield)
                if (gp.player.currentWeapon == gp.player.inventory.get(itemIndex) || gp.player.currentShield == gp.player.inventory.get(itemIndex)) {
                    commandNum = 0;
                    subState = 0;
                    npc.startDialogue(npc, 4);  // Show message for equipped items
                } else {
                    // Remove the item from the player's inventory and add its price to the player's coins
                    if (gp.player.inventory.get(itemIndex).amount > 1) {
                        gp.player.inventory.get(itemIndex).amount--;
                    } else {
                        gp.player.inventory.remove(itemIndex);
                        gp.player.coin += price; // Player gains coins from selling the item
                    }
                }
            }
        }
    }

    // Draw the sleep screen transition effect by gradually adjusting the lighting filter's transparency.
    public void drawSleepScreen() {
        // Increment the transition counter.
        counter++;

        // Gradually darken the screen during the first 120 frames.
        if (counter < 120) {
            gp.eManager.lighting.filterAlpha += 0.01f;
            if (gp.eManager.lighting.filterAlpha > 1f) {
                gp.eManager.lighting.filterAlpha = 1f; // Cap the alpha value at 1 (fully dark).
            }
        }

        // Gradually brighten the screen after 120 frames.
        if (counter >= 120) {
            gp.eManager.lighting.filterAlpha -= 0.01f;
            if (gp.eManager.lighting.filterAlpha < 0f) {
                gp.eManager.lighting.filterAlpha = 0f; // Reset the alpha value to 0 (fully bright).
                counter = 0; // Reset the counter for the next transition.
                gp.eManager.lighting.dayState = gp.eManager.lighting.day; // Reset to daytime state.
                gp.eManager.lighting.dayCounter = 0; // Reset the day counter.
                gp.gameState = gp.playState; // Return to the play state.
                gp.player.getImage(); // Reload the player's regular images.
            }
        }
    }

    // Calculate the index of the item based on current slot position
    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        return slotCol + (slotRow * 5); // Calculate index by adding column and row offsets
    }

    // Draws a rounded rectangle sub-window for displaying UI elements.
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 220); // Semi-transparent black for the window background.

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

    // Align text to the right based on the tail position
    public int getXForAlignToRight(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth(); // Get width of the text

        return tailX - length; // Calculate X position for right alignment
    }
}