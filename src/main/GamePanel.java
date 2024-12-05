package main;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
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
    public final int maxScreenCol = 20;  // 20 columns of tiles on the screen.
    public final int maxScreenRow = 12;  // 12 rows of tiles on the screen.

    // The total screen width and height in pixels, calculated based on the number of tiles.
    public final int screenWidth = tileSize * maxScreenCol;  // 960 pixels wide (20 * 48).
    public final int screenHeight = tileSize * maxScreenRow;  // 576 pixels high (12 * 48).

    // Full screen settings
    int screenWidth2 = screenWidth;     // Sets initial full screen width
    int screenHeight2 = screenHeight;   // Sets initial full screen height
    BufferedImage tempScreen;           // Buffered image used for rendering in full screen
    Graphics2D g2;                      // Graphics object for drawing on the buffered image
    public boolean fullScreenOn = false; // Tracks the full-screen mode state; true if full screen is enabled, false otherwise

    // WORLD SETTINGS
    // The total number of tile columns and rows in the entire game world.
    public final int maxWorldCol = 50;  // 50 columns of tiles in the world.
    public final int maxWorldRow = 50;  // 50 rows of tiles in the world.
    public final int maxMap = 10;       // The maximum number of maps in the game.
    public int currentMap = 0;          // Tracks the index of the current map being displayed and interacted with.

    // Frames per second (FPS) target for smooth gameplay.
    int FPS = 60;  // The game loop will aim to run at 60 frames per second.

    //SYSTEM
    // TileManager handles the loading and drawing of the tiles in the game world.
    public TileManager tileM = new TileManager(this);

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

    // Configuration manager to handle game settings.
    Config config = new Config(this);

    // Calculates paths for entities, considering obstacles.
    public PathFinder pFinder = new PathFinder(this);

    // Manages environmental effects, such as lighting, for enhancing the game's atmosphere.
    EnvironmentManager eManager = new EnvironmentManager(this);

    // Manages the display of the game map or minimap, providing a visual representation of the player's surroundings.
    Map map = new Map(this);

    // Handles the saving and loading of game progress.
    SaveLoad saveLoad = new SaveLoad(this);

    // Game thread that runs the game loop. This separates game logic from the UI thread.
    Thread gameThread;

    //ENTITY AND OBJECT
    // The Player object that represents the player character in the game.
    public Player player = new Player(this, keyH);

    // Array to hold the game objects, such as keys, doors, and chests.
    public Entity[][] obj = new Entity[maxMap][20];

    // Array to hold the game NPCs.
    public Entity[][] npc = new Entity[maxMap][10];

    // Array to hold the game monsters.
    public Entity[][] monster = new Entity[maxMap][20];

    // ArrayList to hold all particles.
    public ArrayList<Entity> particleList = new ArrayList<>();

    // Array to hold the interactive tiles.
    public InteractiveTile[][] iTile = new InteractiveTile[maxMap][50];

    // Array to hold all projectiles for rendering in the correct order.
    public Entity[][] projectile = new Entity[maxMap][50];

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
    // Constant for options state.
    public final int optionsState = 5;
    // Constant for game over state.
    public final int gameOverState = 6;
    // Constant for transition state.
    public final int transitionState = 7;
    // Constant for trade state.
    public final int tradeState = 8;
    // Constant for sleep state.
    public final int sleepState = 9;
    // Constant for map state.
    public final int mapState = 10;

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

        // Sets up environmental effects, such as lighting.
        eManager.setup();

        // Set default state to title state.
        gameState = titleState;

        // Prepares a temporary screen for full-screen rendering
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB_PRE);
        // Creates a graphics context from the temporary screen
        g2 = (Graphics2D) tempScreen.getGraphics();

        // If full-screen mode is enabled, it calls the method to switch to full screen.
        if (fullScreenOn) {
            setFullScreen();   // Calls method to enable full-screen mode.
        }
    }

    // Resets the game state, restoring the player's position, health, mana, and resetting NPCs and monsters.
// Optionally, if `restart` is true, resets all objects, interactive tiles, and lighting conditions.
    public void resetGame(boolean restart) {
        // Set player back to the default starting position.
        player.setDefaultPosition();
        // Restore player's life and mana to maximum levels.
        player.restoreStatus();
        // Reset NPCs to their initial positions.
        aSetter.setNPC();
        // Reset monsters to their initial positions.
        aSetter.setMonster();

        if (restart) {
            // Fully reset the player to initial default attributes.
            player.setDefaultValues();
            // Reset all objects in the game world.
            aSetter.setObject();
            // Reset interactive tiles to their initial state (e.g., traps, destructibles).
            aSetter.setInteractiveTile();
            // Reset the lighting system to the initial "day" state.
            eManager.lighting.resetDay();
        }
    }

    // Activates full-screen mode and adjusts screen dimensions accordingly
    public void setFullScreen() {
        // Get local screen device for full-screen support
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        gd.setFullScreenWindow(Main.window);  // Sets the full-screen window to the main window

        // Updates full screen width and height based on window dimensions
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
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

                // 2. DRAW: draw everything to the buffered image.
                drawToTempScreen();
                // Draw the buffered image to the screen.
                drawToScreen();

                // Decrement delta by 1 to indicate a frame has been processed.
                delta--;

            }
        }
    }

    // The update method is responsible for advancing the game state, including all game objects and entities.
    public void update() {
        // Check if the game is currently in the play state to determine if updates should occur.
        if (gameState == playState) {

            // Update the player's state.
            // This includes handling movement, interactions with objects, and any other player-specific logic.
            player.update();

            // Update all NPCs (non-playable characters) present on the current map.
            for (int i = 0; i < npc[1].length; i++) {
                // Check if an NPC exists in the current slot for the active map.
                if (npc[currentMap][i] != null) {
                    // Call the NPC's update method to handle its behavior for the current frame.
                    npc[currentMap][i].update();
                }
            }

            // Update all monsters (enemies) present on the current map.
            for (int i = 0; i < monster[1].length; i++) {
                // Ensure that the monster slot in the current map is occupied.
                if (monster[currentMap][i] != null) {
                    // Only update monsters that are alive and not in the process of dying.
                    if (monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        // Call the monster's update method to manage its AI, movement, or attacks.
                        monster[currentMap][i].update();
                    }

                    // Handle cleanup for dead monsters.
                    if (!monster[currentMap][i].alive) {
                        // Execute any logic for dropping items or rewards from the dead monster.
                        monster[currentMap][i].checkDrop();
                        // Remove the monster by setting its slot to null, freeing up memory.
                        monster[currentMap][i] = null;
                    }
                }
            }

            // Iterate over all active projectiles (e.g., bullets, magic spells) to update and clean them up.
            for (int i = 0; i < projectile[1].length; i++) {
                // Check if the projectile exists in the list.
                if (projectile[currentMap][i] != null) {
                    // Update the projectile if it is still active and moving.
                    if (projectile[currentMap][i].alive) {
                        projectile[currentMap][i].update();
                    }

                    // Remove projectiles that are no longer alive from the list to conserve resources.
                    if (!projectile[currentMap][i].alive) {
                        projectile[currentMap][i] = null;
                    }
                }
            }

            // Iterate over all active particles (e.g., visual effects like smoke or sparks) for updates and cleanup.
            for (int i = particleList.size() - 1; i >= 0; i--) {
                // Ensure the particle exists before attempting to update it.
                if (particleList.get(i) != null) {
                    // Update the particle's animation or behavior if it is still active.
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    }

                    // Remove particles that have finished their life cycle from the list.
                    if (!particleList.get(i).alive) {
                        particleList.remove(i);
                    }
                }
            }

            // Update all interactive tiles (e.g., switches, traps) on the current map.
            for (int i = 0; i < iTile[1].length; i++) {
                // Ensure the interactive tile exists in the current slot.
                if (iTile[currentMap][i] != null) {
                    // Call the update method for the interactive tile to handle its state or behavior.
                    iTile[currentMap][i].update();
                }
            }

            // Updates the environmental effects, such as lighting, to reflect the current game state.
            eManager.update();

        } else {
            // If the game is not in the play state (e.g., paused, in a menu),
            // reset the states of all keys to prevent unwanted input.
            keyH.resetKeyStates();
        }
    }

    // drawToTempScreen is called to render the game onto the temp screen.
    public void drawToTempScreen() {

        // Clears the temporary screen for fresh rendering
        g2.clearRect(0, 0, screenWidth2, screenHeight2);

        // Measure draw time if the checkDrawTime flag is enabled.
        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime(); // Record the start time for drawing
        }

        // Render the appropriate screen based on the current game state.
        if (gameState == titleState) {
            //Draw title screen
            ui.draw(g2);
        } else if (gameState == mapState) {
            //Draw full map screen
            map.drawFullMapScreen(g2);
        } else {

            // Draws the tiles onto the Graphics2D context.
            tileM.draw(g2);

            // Draws the interactive tiles.
            for (int i = 0; i < iTile[1].length; i++) {
                if (iTile[currentMap][i] != null) { // Check if the entity is not null.
                    iTile[currentMap][i].draw(g2);  // Draws the interactive tiles onto the Graphics2D context.
                }
            }

            // Add entities to the list;
            entityList.add(player); // Add the player entity to the list.

            for (int i = 0; i < npc[1].length; i++) { // Iterate through the array of NPC entities.
                if (npc[currentMap][i] != null) {  // Check if the entity is not null.
                    entityList.add(npc[currentMap][i]); // Add the non-null NPC to the entity list.
                }
            }

            for (int i = 0; i < obj[1].length; i++) { // Iterate through the array of game objects.
                if (obj[currentMap][i] != null) { // Check if the entity is not null.
                    entityList.add(obj[currentMap][i]); // Add the non-null game object to the entity list.
                }
            }

            for (int i = 0; i < monster[1].length; i++) { // Iterate through the array of monster entities.
                if (monster[currentMap][i] != null) { // Check if the entity is not null.
                    entityList.add(monster[currentMap][i]); // Add the non-null game monster to the entity list.
                }
            }

            for (int i = 0; i < projectile[1].length; i++) { // Iterate through the array of projectiles entities.
                if (projectile[currentMap][i] != null) { // Check if the entity is not null.
                    entityList.add(projectile[currentMap][i]); // Add the non-null game projectiles to the entity list.
                }
            }

            for (Entity entity : particleList) { // Iterate through the array of particles entities.
                if (entity != null) { // Check if the entity is not null.
                    entityList.add(entity); // Add the non-null game particles to the entity list.
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

            // Draws environmental effects.
            eManager.draw(g2);

            // Draws minimap
            map.drawMiniMap(g2);

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
        }
    }

    // Draws the contents of the temporary screen to the main display screen
    public void drawToScreen() {
        Graphics g = getGraphics();  // Gets the current graphics context
        // Draws the tempScreen image onto the display screen with specified dimensions
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();   // Disposes of graphics to free up resources
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
