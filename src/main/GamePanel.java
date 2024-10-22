package main;

import javax.swing.*;
import java.awt.*;

// GamePanel is the main panel that runs the game loop and handles rendering and updates
public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;  // The base size of each tile (16x16 pixels)
    final int scale = 3;               // Scale factor to increase tile size

    final int tileSize = originalTileSize * scale;  // Final size of each tile (48x48 pixels)
    final int maxScreenCol = 16;       // Number of columns of tiles on the screen
    final int maxScreenRow = 12;       // Number of rows of tiles on the screen
    final int screenWidth = tileSize * maxScreenCol;   // Screen width in pixels (768 pixels)
    final int screenHeight = tileSize * maxScreenRow;  // Screen height in pixels (576 pixels)

    // FPS
    int FPS = 60;  // Frames per second for the game loop

    // KeyHandler instance to manage key inputs
    KeyHandler keyH = new KeyHandler();

    // Game loop thread
    Thread gameThread;

    // Player's default position and speed
    int playerX = 100;        // Player's starting X position
    int playerY = 100;        // Player's starting Y position
    int playerSpeed = 4;      // Player's movement speed in pixels

    // Constructor to set up the panel's properties
    public GamePanel() {
        // Set the preferred size of the game panel to match the screen dimensions
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));

        // Set the background color to black
        this.setBackground(Color.black);

        // Enable double buffering to improve rendering performance
        this.setDoubleBuffered(true);

        // Add the KeyListener to capture key inputs
        this.addKeyListener(keyH);

        // Set the panel focusable so it can capture key events
        this.setFocusable(true);
    }

    // Method to start the game loop thread
    public void startGameThread() {
        gameThread = new Thread(this);  // Initialize the thread with the GamePanel's run method
        gameThread.start();              // Start the thread (which calls the run method)
    }

    // The run method implements the game loop
    @Override
    public void run() {
        // Calculate the time per frame in nanoseconds based on the desired FPS (frames per second)
        double drawInterval = (double) 1000000000 / FPS; // 1 second = 1,000,000,000 nanoseconds
        double delta = 0; // Variable to keep track of accumulated time
        long lastTime = System.nanoTime(); // Get the current time in nanoseconds
        long currentTime; // Variable to store the current time during the loop
        int timer = 0;
        int drawCount = 0;

        // The game loop runs continuously as long as gameThread is not null
        while (gameThread != null) {
            currentTime = System.nanoTime(); // Update current time

            // Calculate the elapsed time since the last frame and add it to delta
            delta += (currentTime - lastTime) / drawInterval;

            timer += (int) (currentTime - lastTime);

            // Update lastTime to the current time for the next iteration
            lastTime = currentTime;

            // If enough time has passed to render a new frame
            if (delta >= 1) {
                // 1. UPDATE: update game state (like player movement)
                update();

                // 2. DRAW: redraw the game with updated state
                repaint();

                // Decrement delta by 1 to indicate a frame has been rendered
                delta--;

                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // The update method is called on each loop iteration to update the game state
    public void update() {
        // Handle vertical and horizontal movement based on key inputs
        if (keyH.upPressed) {
            playerY -= playerSpeed;  // Move up
        } else if (keyH.downPressed) {
            playerY += playerSpeed;  // Move down
        } else if (keyH.leftPressed) {
            playerX -= playerSpeed;  // Move left
        } else if (keyH.rightPressed) {
            playerX += playerSpeed;  // Move right
        }
    }

    // The paintComponent method is called to render the graphics of the game
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // Call to the superclass to ensure proper rendering

        // Cast the Graphics object to Graphics2D for more advanced operations
        Graphics2D g2 = (Graphics2D) g;

        // Set the drawing color to white for the player
        g2.setColor(Color.white);

        // Draw a white rectangle to represent the player at its current position (playerX, playerY)
        g2.fillRect(playerX, playerY, tileSize, tileSize);

        // Dispose of the Graphics2D object to free resources
        g2.dispose();
    }
}
