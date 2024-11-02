package main;

// Handles game events such as damage pits, healing pools, and teleport tiles.
// The EventHandler class manages these events by checking if the player collides with specific event locations.
public class EventHandler {

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;
    // 2D array to store event rectangles for each tile in the game world,
    // representing areas where events can be triggered.
    EventRect[][] eventRect;

    // Stores the last position where an event was triggered.
    int previousEventX, previousEventY;
    // Indicates if the player can trigger a new event, reset when moving away from last event.
    boolean canTouchEvent = true;

    // Constructor initializes the EventHandler with the game environment and configures the event rectangle.
    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        // Initialize each EventRect in the grid, setting default size and position.
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23; // Starting X position for the event rectangle.
            eventRect[col][row].y = 23; // Starting Y position for the event rectangle.
            eventRect[col][row].height = 2; // Width of the event rectangle.
            eventRect[col][row].width = 2; // Height of the event rectangle.
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x; // Store default X position for reset.
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y; // Store default Y position for reset.

            col++;

            // Move to the next row if end of column is reached.
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
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
            if (hit(27, 16, "right")) damagePit(27, 16, gp.dialogueState); // Damage pit event.
            if (hit(23, 19, "any")) damagePit(27, 16, gp.dialogueState); // Another damage pit event.
            if (hit(23, 12, "up")) healingPool(23, 12, gp.dialogueState); // Healing pool event.
            if (hit(23, 7, "any")) teleport(gp.dialogueState); // Teleport event.
        }
    }

    // Checks if the player has collided with an event at a specific location and direction.
    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false; // Indicates if an event collision has occurred.

        // Sets the player's collision area based on their current world position.
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // Sets the event's collision area based on its position in the game world.
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        // Checks if the player's collision area intersects with the event's area and if the event hasn't been done.
        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
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
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit; // Returns true if an event was triggered, false otherwise.
    }

    // Triggers the teleport event, moving the player to a new location.
    public void teleport(int gameState) {
        gp.gameState = gameState; // Change game state to display dialogue.
        gp.ui.currentDialogue = "Teleport!"; // Set dialogue for teleport event.
        gp.player.worldX = gp.tileSize * 37; // New X position for player.
        gp.player.worldY = gp.tileSize * 10; // New Y position for player.
    }

    // Triggers the damage pit event, reducing player life by one.
    public void damagePit(int col, int row, int gameState) {
        gp.gameState = gameState; // Change game state to display dialogue.
        gp.playSE(6); // Play sound effect indicating damage.
        gp.ui.currentDialogue = "You fall into a pit!"; // Set dialogue for damage pit.
        gp.player.life--; // Decrease player life by one.
        // Temporarily disables further event triggering until player moves away.
        canTouchEvent = false;
    }

    // Triggers the healing pool event, restoring player life to the maximum.
    public void healingPool(int col, int row, int gameState) {
        // Check if player has pressed enter key to activate healing.
        if (gp.keyH.enterPressed) {
            gp.playSE(2); // Play sound effect indicating healing.
            gp.player.attackCancel = true;
            gp.gameState = gameState; // Change game state to display dialogue.
            gp.ui.currentDialogue = "You drink the water. \n Your HP has been recovered."; // Healing dialogue.
            gp.player.life = gp.player.maxLife; // Restore player's life to maximum.
        }
    }
}
