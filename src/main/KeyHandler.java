package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// The KeyHandler class implements KeyListener to handle keyboard events
public class KeyHandler implements KeyListener {

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;

    // Booleans to track the current state of movement keys (W, A, S, D) and action key (Enter)
    // to determine if each key is currently pressed
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;


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
        // Call the helper method to update key state to 'pressed' (true)
        setKeyState(e.getKeyCode(), true);
    }

    // This method is called when a key is released
    @Override
    public void keyReleased(KeyEvent e) {
        // Call the helper method to update key state to 'released' (false)
        setKeyState(e.getKeyCode(), false);
    }

    // Helper method to update the state of movement keys based on keyCode
    private void setKeyState(int keyCode, boolean isPressed) {
        // Check if the game is in play state to enable movement and toggling actions
        if (gp.gameState == gp.playState) {
            switch (keyCode) {
                case KeyEvent.VK_W -> upPressed = isPressed;    // Sets W key state to move player up
                case KeyEvent.VK_S -> downPressed = isPressed;  // Sets S key state to move player down
                case KeyEvent.VK_A -> leftPressed = isPressed;  // Sets A key state to move player left
                case KeyEvent.VK_D -> rightPressed = isPressed; // Sets D key state to move player right
                case KeyEvent.VK_T -> {
                    if (isPressed) checkDrawTime = !checkDrawTime; // Toggles display of frame rendering times
                }
                case KeyEvent.VK_P -> {
                    if (isPressed) gp.gameState = gp.pauseState; // Switches game to pause state if P is pressed
                }
                case KeyEvent.VK_ENTER ->
                        enterPressed = isPressed; // Tracks Enter key for dialogue and selection actions
            }
        }
        // If game is paused, only unpause when P key is pressed
        else if (gp.gameState == gp.pauseState) {
            if (keyCode == KeyEvent.VK_P && isPressed) {
                gp.gameState = gp.playState; // Resumes game play state from pause
            }
        }
        // If in dialogue state, pressing Enter resumes play state
        else if (gp.gameState == gp.dialogueState) {
            if (keyCode == KeyEvent.VK_ENTER && isPressed) {
                gp.gameState = gp.playState; // Exits dialogue and resumes gameplay
            }
        }
    }


    // Method to reset all movement keys to false
    public void resetKeyStates() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }
}