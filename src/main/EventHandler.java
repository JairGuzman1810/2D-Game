package main;

import entity.Entity;

// Handles game events such as damage pits, healing pools, and teleport tiles.
// The EventHandler class manages these events by checking if the player collides with specific event locations.
public class EventHandler {

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;
    // 2D array to store event rectangles for each tile in the game world,
    // representing areas where events can be triggered.
    EventRect[][][] eventRect;

    // Entity used to manage event dialogues and interactions.
    Entity eventMaster;

    // Stores the last position where an event was triggered.
    int previousEventX, previousEventY;
    // Indicates if the player can trigger a new event, reset when moving away from last event.
    boolean canTouchEvent = false;

    // Temporary variables to store target map, column, and row during a teleportation event.
    // These values are used for transitioning the player to the specified location.
    int tempMap, tempCol, tempRow;

    // Constructor initializes the EventHandler with the game environment and configures the event rectangles.
    public EventHandler(GamePanel gp) {
        this.gp = gp; // Assign the GamePanel instance to access game properties and states.
        eventMaster = new Entity(gp); // Entity to store dialogues and manage event-related interactions.

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow]; // Initialize the 3D array for event rectangles.

        int map = 0;
        int col = 0;
        int row = 0;

        // Initialize each EventRect in the grid, setting default size and position.
        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[map][col][row] = new EventRect(); // Create a new EventRect for each grid cell.
            eventRect[map][col][row].x = 23; // Starting X position for the event rectangle.
            eventRect[map][col][row].y = 23; // Starting Y position for the event rectangle.
            eventRect[map][col][row].height = 2; // Width of the event rectangle.
            eventRect[map][col][row].width = 2; // Height of the event rectangle.
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x; // Store default X position for reset.
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y; // Store default Y position for reset.

            col++;

            // Move to the next row if end of column is reached.
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;

                // Move to the next map if end of row is reached.
                if (row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

        setDialogue(); // Initialize predefined dialogues for various events.
    }

    // Initializes dialogues for event interactions (e.g., pits, healing pools).
    public void setDialogue() {
        eventMaster.dialogues[0][0] = "You fall into a pit!";

        eventMaster.dialogues[1][0] = "You drink the water.\nYour HP has been recovered.\n(The progress has been saved)";
    }

    // Checks for player collisions with any events (damage pits, healing pools, or teleport tiles).
    public void checkEvent() {
        // Calculate the distance between the player and the last event position.
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);

        int distance = Math.max(xDistance, yDistance);

        // Allow new event triggering if player has moved sufficiently far away.
        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }

        // Trigger event if player is in the designated area and facing the required direction.
        if (canTouchEvent) {
            if (hit(0, 27, 16, "right")) damagePit(gp.dialogueState); // Damage pit event.
            else if (hit(0, 23, 19, "any")) damagePit(gp.dialogueState); // Another damage pit event.
            else if (hit(0, 23, 12, "up")) healingPool(gp.dialogueState); // Healing pool event.
            else if (hit(0, 10, 40, "any")) teleport(1, 12, 13); // Teleport event.
            else if (hit(1, 12, 13, "any")) teleport(0, 10, 40); // Teleport event.
            else if (hit(1, 12, 9, "up")) speak(gp.npc[1][0]); // Initiate dialogue with an NPC when facing up.
        }
    }

    // Checks if the player has collided with an event at a specific location and direction.
    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false; // Indicates if an event collision has occurred.


        if (map == gp.currentMap) {
            // Sets the player's collision area based on their current world position.
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            // Sets the event's collision area based on its position in the game world.
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            // Checks if the player's collision area intersects with the event's area and if the event hasn't been done.
            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
                // Checks if the player is facing the required direction or any direction is allowed.
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true; // Sets hit to true, indicating that an event collision occurred.
                    previousEventX = gp.player.worldX; // Updates the last event X position.
                    previousEventY = gp.player.worldY; // Updates the last event Y position.
                }
            }

            // Resets the player's collision area to its default values.
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;

            // Resets the event's collision area to its default values.
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit; // Returns true if an event was triggered, false otherwise.
    }

    // Triggers the teleport event, moving the player to a new map and position.
    public void teleport(int map, int col, int row) {
        gp.gameState = gp.transitionState; // Change the game state to a transition state.
        tempMap = map; // Store the target map for the teleportation.
        tempCol = col; // Store the target column for the teleportation.
        tempRow = row; // Store the target row for the teleportation.
        canTouchEvent = false; // Prevent further events until the player moves away.
        gp.playSE(13); // Play the sound effect for teleportation.
    }

    // Triggers the damage pit event, reducing player life by one.
    public void damagePit(int gameState) {
        gp.gameState = gameState; // Change game state to display dialogue.
        gp.playSE(6); // Play sound effect indicating damage.
        eventMaster.startDialogue(eventMaster, 0); // Start dialogue for damage pit.
        gp.player.life--; // Decrease player life by one.
        // Temporarily disables further event triggering until player moves away.
        canTouchEvent = false;
    }

    // Triggers the healing pool event, restoring player life to the maximum and saving progress.
    public void healingPool(int gameState) {
        // Check if player has pressed the enter key to activate healing.
        if (gp.keyH.enterPressed) {
            gp.playSE(2); // Play sound effect indicating healing.
            gp.player.attackCancel = true; // Cancel any ongoing attack.
            gp.gameState = gameState; // Change the game state to display dialogue.

            // Display dialogue indicating the healing process and save confirmation.
            eventMaster.startDialogue(eventMaster, 1); // Start dialogue for damage pit.

            gp.player.life = gp.player.maxLife; // Restore the player's health to its maximum value.
            gp.player.mana = gp.player.maxMana; // Restore the player's mana to its maximum value.

            gp.aSetter.setMonster(); // Respawn monsters for a refreshed challenge.

            // Save the game's progress, including the player's current stats and state.
            gp.saveLoad.save();
        }
    }

    // Initiates a dialogue with the specified NPC entity.
    // This is triggered when the player presses the "enter" key while near the NPC.
    public void speak(Entity entity) {
        if (gp.keyH.enterPressed) { // Check if the "enter" key is pressed.
            gp.gameState = gp.dialogueState; // Change the game state to dialogue mode.
            gp.player.attackCancel = true; // Cancel any ongoing attack animations.
            entity.speak(); // Trigger the NPC's specific dialogue logic.
        }
    }
}
