package main;

import javax.swing.*;

// Main class that serves as the entry point for the game application
public class Main {

    public static JFrame window;

    public static void main(String[] args) {
        // Create a new JFrame to serve as the main window of the game
        window = new JFrame();

        // Set the default close operation to exit the application when the window is closed
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Prevent the window from being resizable
        window.setResizable(false);

        // Set the title of the window
        window.setTitle("2D Adventure");

        //window.setUndecorated(true);

        // Create an instance of GamePanel, which contains the game logic and rendering
        GamePanel gamePanel = new GamePanel();

        // Add the GamePanel to the JFrame
        window.add(gamePanel);

        // Load saved configuration settings, such as full-screen mode preference
        gamePanel.config.loadConfig();

        // If full-screen mode is enabled in the configuration, remove window decorations
        if (gamePanel.fullScreenOn) {
            window.setUndecorated(true);
        }


        // Pack the components within the window, sizing it to fit the preferred sizes of its components
        window.pack();

        // Center the window on the screen
        window.setLocationRelativeTo(null);

        // Make the window visible
        window.setVisible(true);

        // Call the setup method to place objects in the game world
        gamePanel.setupGame();

        // Start the game loop in the GamePanel
        gamePanel.startGameThread();
    }
}
