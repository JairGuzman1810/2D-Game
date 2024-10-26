package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// The KeyHandler class implements KeyListener to handle keyboard events
public class KeyHandler implements KeyListener {

    // Booleans to track if specific movement keys (W, A, S, D) are pressed
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean checkDrawTime = false;

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

    // Helper method to update the state of the movement keys based on the keyCode
    private void setKeyState(int keyCode, boolean isPressed) {
        // Use a switch statement to check which key was pressed/released and update the corresponding boolean
        switch (keyCode) {
            case KeyEvent.VK_W -> upPressed = isPressed;    // W key (up movement)
            case KeyEvent.VK_S -> downPressed = isPressed;  // S key (down movement)
            case KeyEvent.VK_A -> leftPressed = isPressed;  // A key (left movement)
            case KeyEvent.VK_D -> rightPressed = isPressed; // D key (right movement)
            case KeyEvent.VK_T -> {
                if (isPressed) {
                    checkDrawTime = !checkDrawTime; // Toggle checkDrawTime only on key press
                }
            }
        }
    }
}
