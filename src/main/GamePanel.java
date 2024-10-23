package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

// GamePanel handles the main game loop, updates, and rendering.
// It extends JPanel and implements Runnable to manage the game loop in a separate thread.
public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;  // Base tile size in pixels (16x16).
    final int scale = 3;  // Scale factor to enlarge tiles.

    public final int tileSize = originalTileSize * scale;  // Final tile size (48x48).
    final int maxScreenCol = 16;  // Number of tile columns on the screen.
    final int maxScreenRow = 12;  // Number of tile rows on the screen.
    final int screenWidth = tileSize * maxScreenCol;  // Screen width in pixels (768px).
    final int screenHeight = tileSize * maxScreenRow;  // Screen height in pixels (576px).

    // FPS
    int FPS = 60;  // Target frames per second for the game loop.

    // KeyHandler to capture player inputs.
    KeyHandler keyH = new KeyHandler();

    // Game loop thread to run the game independently of the UI thread.
    Thread gameThread;

    // Player object representing the player character.
    Player player = new Player(this, keyH);

    // Constructor to initialize the GamePanel settings.
    public GamePanel() {
        // Set the size of the panel to match the screen dimensions.
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));

        // Set the background color to black.
        this.setBackground(Color.black);

        // Enable double buffering to reduce flickering during rendering.
        this.setDoubleBuffered(true);

        // Add the KeyHandler to the panel to capture key events.
        this.addKeyListener(keyH);

        // Make the panel focusable to allow it to receive key inputs.
        this.setFocusable(true);
    }

    // Method to start the game thread, which runs the game loop.
    public void startGameThread() {
        // Initialize the thread with the GamePanel's run method.
        gameThread = new Thread(this);

        // Start the thread, which triggers the run method.
        gameThread.start();
    }

    // The run method contains the game loop that controls updates and rendering.
    @Override
    public void run() {
        // Calculate the time per frame in nanoseconds based on the desired FPS (1 second = 1,000,000,000 nanoseconds).
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;  // Accumulates the time between frames to control the update rate.
        long lastTime = System.nanoTime();  // Get the initial time in nanoseconds.
        long currentTime;  // Stores the current time on each loop iteration.
        int timer = 0;  // Tracks elapsed time to calculate FPS.
        int drawCount = 0;  // Tracks how many frames have been drawn.

        // Game loop runs continuously while gameThread is not null.
        while (gameThread != null) {
            // Update current time in nanoseconds.
            currentTime = System.nanoTime();

            // Add the time since the last frame to delta.
            delta += (currentTime - lastTime) / drawInterval;

            // Accumulate the time to track FPS.
            timer += (int) (currentTime - lastTime);

            // Update lastTime for the next iteration.
            lastTime = currentTime;

            // If enough time has passed to process a frame, update and render.
            if (delta >= 1) {
                // 1. UPDATE: update the game state (e.g., player movement).
                update();

                // 2. DRAW: render the game with the updated state.
                repaint();

                // Decrement delta by 1 to indicate a frame has been processed.
                delta--;

                // Increment the frame counter.
                drawCount++;
            }

            // After 1 second (1,000,000,000 nanoseconds), print the FPS.
            if (timer >= 1000000000) {
                // Output the current FPS to the console.
                System.out.println("FPS: " + drawCount);

                // Reset the frame counter and timer.
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // Update method to update the game state each frame.
    public void update() {
        // Delegate the update to the player (handles player movement).
        player.update();
    }

    // paintComponent is called to render the game onto the panel.
    @Override
    public void paintComponent(Graphics g) {
        // Call the superclass method to handle basic rendering.
        super.paintComponent(g);

        // Cast Graphics to Graphics2D for more advanced drawing options.
        Graphics2D g2 = (Graphics2D) g;

        // Draw the player using the current Graphics2D context.
        player.draw(g2);

        // Dispose of the Graphics2D object to free resources.
        g2.dispose();
    }
}
