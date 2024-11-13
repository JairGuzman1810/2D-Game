package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// The KeyHandler class implements KeyListener to handle keyboard events
public class KeyHandler implements KeyListener {

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;

    // Booleans to track the current state of movement keys (W, A, S, D) and action key (Enter)
    // to determine if each key is currently pressed
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;

    // Boolean to track if draw time debugging is enabled; if true, draw times are printed to the console and show in the UI.
    public boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    // This method is required by the KeyListener interface but not used in this case
    @Override
    public void keyTyped(KeyEvent e) {
        // No action needed for keyTyped
    }

    // This method is called when a key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        // Call the helper method to update key state to 'pressed'
        setKeyState(e.getKeyCode(), true);
    }

    // This method is called when a key is released
    @Override
    public void keyReleased(KeyEvent e) {
        // Call the helper method to update key state to 'released'
        setKeyState(e.getKeyCode(), false);
    }

    // Helper method to update the state of movement keys based on keyCode
    private void setKeyState(int keyCode, boolean isPressed) {
        // Check the current game state and call the corresponding handler method
        if (gp.gameState == gp.titleState) {            // Check if the game is in the title state
            handleTitleState(keyCode, isPressed);       // Handle key events for the title state
        } else if (gp.gameState == gp.playState) {      // Check if the game is in the play state
            handlePlayState(keyCode, isPressed);        // Handle key events for the play state
        } else if (gp.gameState == gp.pauseState) {     // Check if the game is in the pause state
            handlePauseState(keyCode, isPressed);       // Handle key events for the pause state
        } else if (gp.gameState == gp.dialogueState) {  // Check if the game is in the dialogue state
            handleDialogueState(keyCode, isPressed);    // Handle key events for the dialogue state
        } else if (gp.gameState == gp.characterState) { // Check if the game is in the character state
            handleCharacterState(keyCode, isPressed);   // Handle key events for the character stats state
        } else if (gp.gameState == gp.optionsState) {   // Check if the game is in the options state
            handleOptionsState(keyCode, isPressed);     // Handle key events for the options state

        }
    }

    // Handles key events in the title state
    private void handleTitleState(int keyCode, boolean isPressed) {
        if (isPressed) {
            switch (keyCode) {
                case KeyEvent.VK_W ->
                        gp.ui.commandNum = (gp.ui.commandNum - 1 + 3) % 3; // Decrement commandNum, wrap to 2
                case KeyEvent.VK_S -> gp.ui.commandNum = (gp.ui.commandNum + 1) % 3; // Increment commandNum, wrap to 0
                case KeyEvent.VK_ENTER -> {
                    // Perform action based on current commandNum
                    if (gp.ui.commandNum == 0) {
                        gp.gameState = gp.playState; // Start game if commandNum is 0
                        gp.playMusic(0);           // Start music
                    } else if (gp.ui.commandNum == 1) {
                        // Placeholder for additional functionality
                    } else {
                        System.exit(0); // Exit the game if commandNum is 2
                    }
                }
            }
        }
    }

    // Handles key events in the play state
    private void handlePlayState(int keyCode, boolean isPressed) {
        if (isPressed) {
            switch (keyCode) {
                case KeyEvent.VK_W -> upPressed = true;                 // Sets upPressed to true for moving player up
                case KeyEvent.VK_S ->
                        downPressed = true;                             // Sets downPressed to true for moving player down
                case KeyEvent.VK_A ->
                        leftPressed = true;                             // Sets leftPressed to true for moving player left
                case KeyEvent.VK_D ->
                        rightPressed = true;                             // Sets rightPressed to true for moving player right
                case KeyEvent.VK_T -> checkDrawTime = !checkDrawTime;   // Toggles the display of frame rendering times
                case KeyEvent.VK_P -> gp.gameState = gp.pauseState;     // Switches game to pause state if P is pressed
                case KeyEvent.VK_ENTER ->
                        enterPressed = true;                            // Tracks Enter key for dialogue and selection actions
                case KeyEvent.VK_C ->
                        gp.gameState = gp.characterState;               // Switches game to character stats state if C is pressed
                case KeyEvent.VK_F ->
                        shotKeyPressed = true;                          // Sets shotKeyPressed to true for shot a projectile
                case KeyEvent.VK_ESCAPE ->
                        gp.gameState = gp.optionsState;                 // Switches game to options state if ESC is pressed
            }
        } else {
            // Reset movement keys if they are released
            switch (keyCode) {
                case KeyEvent.VK_W -> upPressed = false;        // Reset upPressed to false
                case KeyEvent.VK_S -> downPressed = false;      // Reset downPressed to false
                case KeyEvent.VK_A -> leftPressed = false;      // Reset leftPressed to false
                case KeyEvent.VK_D -> rightPressed = false;     // Reset rightPressed to false
                case KeyEvent.VK_ENTER -> enterPressed = false; // Reset enterPressed to false
                case KeyEvent.VK_F -> shotKeyPressed = false;   // Reset shotKeyPressed to false
            }
        }
    }

    // Handles key events in the pause state
    private void handlePauseState(int keyCode, boolean isPressed) {
        if (isPressed && keyCode == KeyEvent.VK_P) {
            gp.gameState = gp.playState; // Resumes gameplay
        }
    }

    // Handles key events in the character state
    private void handleDialogueState(int keyCode, boolean isPressed) {
        if (isPressed && keyCode == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState; // Exits character and resumes gameplay
        }
    }

    // Handles key events in the character state
    private void handleCharacterState(int keyCode, boolean isPressed) {
        if (isPressed) { // Only respond if the key is pressed down
            switch (keyCode) {
                case KeyEvent.VK_C ->
                        gp.gameState = gp.playState; // Pressing 'C' exits character state and returns to play state
                case KeyEvent.VK_W -> { // Pressing 'W' moves the slot selector up
                    gp.ui.slotRow--; // Decrease row index to move up
                    if (gp.ui.slotRow < 0) gp.ui.slotRow = 3; // Wrap to bottom if it goes above the limit
                    gp.playSE(9); // Play sound effect for slot movement
                }
                case KeyEvent.VK_S -> { // Pressing 'S' moves the slot selector down
                    gp.ui.slotRow++; // Increase row index to move down
                    if (gp.ui.slotRow > 3) gp.ui.slotRow = 0; // Wrap to top if it exceeds the limit
                    gp.playSE(9); // Play sound effect for slot movement
                }
                case KeyEvent.VK_A -> { // Pressing 'A' moves the slot selector left
                    gp.ui.slotCol--; // Decrease column index to move left
                    if (gp.ui.slotCol < 0) gp.ui.slotCol = 4; // Wrap to right if it goes beyond the limit
                    gp.playSE(9); // Play sound effect for slot movement
                }
                case KeyEvent.VK_D -> { // Pressing 'D' moves the slot selector right
                    gp.ui.slotCol++; // Increase column index to move right
                    if (gp.ui.slotCol > 4) gp.ui.slotCol = 0; // Wrap to left if it exceeds the limit
                    gp.playSE(9); // Play sound effect for slot movement
                }
                case KeyEvent.VK_ENTER -> gp.player.selectItem();
            }
        }
    }

    // Handles key events in the options state, allowing the user to navigate commands and adjust settings
    private void handleOptionsState(int keyCode, boolean isPressed) {
        if (!isPressed) return;  // Only respond if the key is pressed down

        // Map key events to corresponding actions
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE -> gp.gameState = gp.playState; // Exit options state and return to play state
            case KeyEvent.VK_ENTER -> enterPressed = true;           // Track 'Enter' key press for selection actions
            case KeyEvent.VK_W -> navigateCommand(-1);               // Move up in command list
            case KeyEvent.VK_S -> navigateCommand(1);                // Move down in command list
            case KeyEvent.VK_A -> adjustVolume(-1);                  // Decrease volume for music or sound effects
            case KeyEvent.VK_D -> adjustVolume(1);                   // Increase volume for music or sound effects
        }
    }

    // Handles command navigation based on direction for subStates that allow navigation
    private void navigateCommand(int direction) {
        int maxCommand = getMaxCommand();  // Determine the maximum number of commands in the current subState

        // Adjust commandNum if navigation is allowed in the current subState
        if (maxCommand > 1) {
            gp.ui.commandNum = (gp.ui.commandNum + direction + maxCommand) % maxCommand; // Update command index, wrap around
            gp.playSE(9);  // Play sound effect to indicate navigation
        }
    }

    // Returns the maximum command count for the current subState, or 1 if navigation is disabled
    private int getMaxCommand() {
        return switch (gp.ui.subState) {
            case 0 -> 6;    // SubState 0 allows navigation across 6 commands
            case 3 -> 3;    // SubState 3 allows navigation across 3 commands
            default -> 1;   // For other subStates, navigation is restricted (single command only)
        };
    }

    // Adjusts volume for music or sound effects based on the command selection and direction of adjustment
    private void adjustVolume(int direction) {
        if (gp.ui.subState != 0) return;  // Only adjust volume if in the primary settings subState (subState 0)

        // Adjust music or sound effects volume based on the selected command
        switch (gp.ui.commandNum) {
            case 1 -> adjustMusicVolume(direction);  // Adjusts music volume
            case 2 -> adjustSEVolume(direction);     // Adjusts sound effect volume
        }
    }

    // Adjusts the music volume within preset limits and plays a sound effect if the volume changes
    private void adjustMusicVolume(int direction) {
        int newVolume = gp.music.volumeScale + direction;  // Calculate new volume level
        if (newVolume >= 0 && newVolume <= 5) {            // Ensure volume remains within valid range (0-5)
            gp.music.volumeScale = newVolume;              // Apply new volume
            gp.music.checkVolume();                        // Re-apply volume settings
            gp.playSE(9);                                  // Play sound effect to confirm change
        }
    }

    // Adjusts the sound effect volume within preset limits and plays a sound effect if the volume changes
    private void adjustSEVolume(int direction) {
        int newVolume = gp.se.volumeScale + direction;     // Calculate new volume level
        if (newVolume >= 0 && newVolume <= 5) {            // Ensure volume remains within valid range (0-5)
            gp.se.volumeScale = newVolume;                 // Apply new volume
            gp.playSE(9);                                  // Play sound effect to confirm change
        }
    }

    // Method to reset all movement keys to false
    public void resetKeyStates() {
        upPressed = false;      // Resets upPressed state
        downPressed = false;    // Resets downPressed state
        leftPressed = false;    // Resets leftPressed state
        rightPressed = false;   // Resets rightPressed state
        shotKeyPressed = false; // Resets shotKeyPressed state
    }
}
