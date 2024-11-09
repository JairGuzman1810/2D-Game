package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Comparator;

// GamePanel handles the main game loop, updates, and rendering.
// It extends JPanel and implements Runnable to manage the game loop in a separate thread.
public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16;  // Base tile size in pixels (16x16).
    final int scale = 3;  // Scale factor to enlarge tiles.

    // The final tile size after scaling (48x48 pixels).
    public final int tileSize = originalTileSize * scale;

    // Maximum number of tile columns and rows displayed on the screen at once.
    public final int maxScreenCol = 16;  // 16 columns of tiles on the screen.
    public final int maxScreenRow = 12;  // 12 rows of tiles on the screen.

    // The total screen width and height in pixels, calculated based on the number of tiles.
    public final int screenWidth = tileSize * maxScreenCol;  // 768 pixels wide (16 * 48).
    public final int screenHeight = tileSize * maxScreenRow;  // 576 pixels high (12 * 48).

    // WORLD SETTINGS
    // The total number of tile columns and rows in the entire game world.
    public final int maxWorldCol = 50;  // 50 columns of tiles in the world.
    public final int maxWorldRow = 50;  // 50 rows of tiles in the world.

    // Frames per second (FPS) target for smooth gameplay.
    int FPS = 60;  // The game loop will aim to run at 60 frames per second.

    //SYSTEM
    // TileManager handles the loading and drawing of the tiles in the game world.
    TileManager tileM = new TileManager(this);

    // KeyHandler captures and manages player keyboard inputs for movement.
    public KeyHandler keyH = new KeyHandler(this);

    // Sound system to manage background music
    Sound music = new Sound();

    // Sound system to manage sound effects.
    Sound se = new Sound();

    // Manages collision detection between the player and game world objects.
    public CollisionChecker cChecker = new CollisionChecker(this);

    //  Handles placing objects in the world
    public AssetSetter aSetter = new AssetSetter(this);

    //  Handles user interface
    public UI ui = new UI(this);

    // Manages interactive events like damage zones, healing zones, and teleportation within the game world.
    public EventHandler eHandler = new EventHandler(this);

    // Game thread that runs the game loop. This separates game logic from the UI thread.
    Thread gameThread;

    //ENTITY AND OBJECT
    // The Player object that represents the player character in the game.
    public Player player = new Player(this, keyH);

    // Array to hold the game objects, such as keys, doors, and chests
    public Entity[] obj = new Entity[20];

    // Array to hold the game NPCs
    public Entity[] npc = new Entity[10];

    // Array to hold the game monsters
    public Entity[] monster = new Entity[20];

    // Array to hold the interactive tiles
    public InteractiveTile[] iTile = new InteractiveTile[50];

    // ArrayList to hold all projectiles for rendering in the correct order.
    public ArrayList<Entity> projectileList = new ArrayList<>();

    // ArrayList to hold all entities for rendering in the correct order.
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME STATE
    // Tracks the current game state (e.g., playing, paused).
    public int gameState;
    // Constant for title state.
    public final int titleState = 0;
    // Constant for play state.
    public final int playState = 1;
    // Constant for pause state.
    public final int pauseState = 2;
    // Constant for dialogue state.
    public final int dialogueState = 3;
    // Constant for character stats state.
    public final int characterState = 4;


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

        // Add a focus listener to reset key states when focus is lost
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // No action needed when focus is regained
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Reset all key states when the window loses focus
                keyH.resetKeyStates();
            }
        });

        // Make the panel focusable to allow it to receive key inputs.
        this.setFocusable(true);
    }

    // Method to set up the initial game state, including placing objects
    public void setupGame() {
        // Calls the AssetSetter to place objects
        aSetter.setObject();

        // Calls the AssetSetter to place NPCs
        aSetter.setNPC();

        // Calls the AssetSetter to place monsters
        aSetter.setMonster();

        // Calls the AssetSetter to place destructive tiles.
        aSetter.setInteractiveTile();

        // Start playing background music (index 0 in the sound array).
        playMusic(0);

        // Set default state to title state.
        gameState = titleState;
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

        // Game loop runs continuously while gameThread is not null.
        while (gameThread != null) {
            // Update current time in nanoseconds.
            currentTime = System.nanoTime();

            // Add the time since the last frame to delta.
            delta += (currentTime - lastTime) / drawInterval;

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

            }
        }
    }

    // Update method to update the game state each frame.
    public void update() {
        // Check if game is in play state to update the player.
        if (gameState == playState) {
            player.update(); // Delegate the update to the player (handles player movement).

            // Update all NPCs.
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.update(); // Calls the update method for each NPC.
                }
            }


            // Iterate through the array of monsters to update their status and remove dead ones
            for (int i = 0; i < monster.length; i++) {
                // Check if the current monster slot is not empty
                if (monster[i] != null) {

                    // If the monster is alive and not in a dying state, update its behavior and position
                    if (monster[i].alive && !monster[i].dying) {
                        // Call the update method to handle the monster's actions, movements, and status changes
                        monster[i].update();
                    }

                    // Check if the monster is no longer alive.
                    // If dead, call `checkDrop()` to determine if it should drop an item, then remove it from the array.
                    if (!monster[i].alive) {
                        monster[i].checkDrop(); // Trigger item drop logic, if any.
                        monster[i] = null;      // Remove the dead monster by setting its slot to null.
                    }
                }
            }

            // Iterate through the array of projectiles in reverse to update their status and remove dead ones
            for (int i = projectileList.size() - 1; i >= 0; i--) {
                if (projectileList.get(i) != null) {

                    // If the projectile is alive, update its behavior and position
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }

                    // If the projectile is no longer alive, remove it from the list
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }

            for (InteractiveTile interactiveTile : iTile) {
                if (interactiveTile != null) {
                    interactiveTile.update();
                }
            }

        } else {
            // Reset keys
            keyH.resetKeyStates();
        }
    }

    // paintComponent is called to render the game onto the panel.
    @Override
    public void paintComponent(Graphics g) {
        // Call the superclass method to handle basic rendering.
        super.paintComponent(g);
        // Cast Graphics to Graphics2D for more advanced drawing options.
        Graphics2D g2 = (Graphics2D) g;

        // Measure draw time if the checkDrawTime flag is enabled.
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime(); // Record the start time for drawing
        }

        // Render the appropriate screen based on the current game state.
        if (gameState == titleState) {
            //Draw title screen
            ui.draw(g2);
        } else {

            // Draws the tiles onto the Graphics2D context.
            tileM.draw(g2);

            // Draws the interactive tiles.
            for (InteractiveTile interactiveTile : iTile) {
                if (interactiveTile != null) { // Check if the entity is not null.
                    interactiveTile.draw(g2);  // Draws the interactive tiles onto the Graphics2D context.
                }
            }

            // Add entities to the list;
            entityList.add(player); // Add the player entity to the list.

            for (Entity entity : npc) { // Iterate through the array of NPC entities.
                if (entity != null) { // Check if the entity is not null.
                    entityList.add(entity); // Add the non-null NPC to the entity list.
                }
            }

            for (Entity entity : obj) { // Iterate through the array of game objects.
                if (entity != null) { // Check if the entity is not null.
                    entityList.add(entity); // Add the non-null game object to the entity list.
                }
            }

            for (Entity entity : monster) { // Iterate through the array of monster entities.
                if (entity != null) { // Check if the entity is not null.
                    entityList.add(entity); // Add the non-null game monster to the entity list.
                }
            }

            for (Entity entity : projectileList) { // Iterate through the array of projectiles entities.
                if (entity != null) { // Check if the entity is not null.
                    entityList.add(entity); // Add the non-null game projectiles to the entity list.
                }
            }

            // Sort the entity list based on the worldY position for proper rendering order.
            entityList.sort(Comparator.comparingInt(e -> e.worldY)); // Sort entities by their worldY position to determine rendering order.

            // Draw entities;
            for (Entity entity : entityList) { // Iterate through the sorted entity list.
                entity.draw(g2); // Call the draw method on each entity to render it on the Graphics2D context.
            }

            // Clear the entity list to prepare for the next frame's rendering.
            entityList.clear();

            // Draw the UI elements (key count, messages, game over screen) using the Graphics2D context.
            ui.draw(g2);

            // Calculate and display the time taken to render the graphics if checkDrawTime is enabled.
            if (keyH.checkDrawTime) {
                long drawEnd = System.nanoTime(); // Record the end time for drawing
                long passed = drawEnd - drawStart; // Calculate the time difference
                g2.setColor(Color.white);
                g2.drawString("Draw Time: " + passed + " ns", 10, 400); // Display draw time on the screen
                System.out.println("Draw Time: " + passed + " ns"); // Log draw time to the console for further analysis
            }

            // Dispose of the Graphics2D object to free resources.
            g2.dispose();
        }


    }

    // Plays background music using the specified sound index.
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop(); // Loops the music indefinitely.
    }

    // Stops the currently playing background music.
    public void stopMusic() {
        music.stop();
    }

    // Plays a one-time sound effect using the specified sound index.
    public void playSE(int i) {
        se.setFile(i);
        se.play(); // Plays the sound effect without looping.
    }
}
