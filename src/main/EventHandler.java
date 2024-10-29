package main;

import java.awt.*;

// Handles game events such as damage pits, healing pools, and teleport tiles.
// The EventHandler class manages these events by checking if the player collides with specific event locations.
public class EventHandler {

    // Reference to the GamePanel, which provides the game environment's state and properties.
    GamePanel gp;
    // Rectangle representing the event area that will detect player collision.
    Rectangle eventRect;
    // Default X and Y positions for resetting the event rectangle.
    int eventRectDefaultX, eventRectDefaultY;

    // Constructor initializes the EventHandler with the game environment and configures the event rectangle.
    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new Rectangle();
        eventRect.x = 23; // Starting X position for the event rectangle.
        eventRect.y = 23; // Starting Y position for the event rectangle.
        eventRect.width = 2; // Width of the event rectangle.
        eventRect.height = 2; // Height of the event rectangle.
        eventRectDefaultX = eventRect.x; // Store default X position for reset.
        eventRectDefaultY = eventRect.y; // Store default Y position for reset.
    }

    // Checks for player collisions with any events (damage pits, healing pools, or teleport tiles).
    public void checkEvent() {
        // If the player is in the designated area and facing the required direction, trigger the event.
        if (hit(27, 16, "right")) damagePit(gp.dialogueState); // Damage pit event.
        if (hit(23, 12, "up")) healingPool(gp.dialogueState);   // Healing pool event.
        if (hit(23, 7, "any")) teleport(gp.dialogueState);      // Teleport event.
    }

    // Checks if the player collides with an event area based on tile coordinates and required direction.
    public boolean hit(int eventCol, int eventRow, String reqDirection) {
        boolean hit = false;

        // Update player collision area to current world position.
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // Set event rectangle position based on tile coordinates.
        eventRect.x = eventCol * gp.tileSize + eventRect.x;
        eventRect.y = eventRow * gp.tileSize + eventRect.y;

        // Check if player collides with the event rectangle and meets direction requirement.
        if (gp.player.solidArea.intersects(eventRect)) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true; // Collision detected with required direction.
            }
        }

        // Reset collision areas to their default values.
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit; // Return true if event was triggered.
    }

    // Triggers the teleport event, moving the player to a new location.
    public void teleport(int gameState) {
        gp.gameState = gameState; // Change game state to display dialogue.
        gp.ui.currentDialogue = "Teleport!"; // Set dialogue for teleport event.
        gp.player.worldX = gp.tileSize * 37; // New X position for player.
        gp.player.worldY = gp.tileSize * 10; // New Y position for player.
    }

    // Triggers the damage pit event, reducing player life by one.
    public void damagePit(int gameState) {
        gp.gameState = gameState; // Change game state to display dialogue.
        gp.ui.currentDialogue = "You fall into a pit!"; // Set dialogue for damage pit.
        gp.player.life--; // Decrease player life by one.
    }

    // Triggers the healing pool event, restoring player life to the maximum.
    public void healingPool(int gameState) {
        if (gp.keyH.enterPressed) { // Check if player has pressed enter key.
            gp.gameState = gameState; // Change game state to display dialogue.
            gp.ui.currentDialogue = "You drink the water. \n Your HP has been recovered."; // Healing dialogue.
            gp.player.life = gp.player.maxLife; // Restore player's life to maximum.
        }
    }
}
