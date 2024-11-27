package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Key defines the key object in the game, which can be collected by the player.
public class OBJ_Key extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the key's name and loads its image.
    public OBJ_Key(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // Set the type of the object to "consumable," as it can be used and removed.
        type = type_consumable;

        // Set the name of the key object.
        name = "Key";

        // Load the key image from resources.
        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);

        // Provides a short description of the item, shown in the inventory.
        description = "[" + name + "]\nIt opens a door.";

        // Sets the price in the in-game currency, determining its value for purchasing.
        price = 100;

        // Indicates if the item is stackable (multiple instances of the same item can occupy one inventory slot).
        stackable = true;
    }

    // Handles the logic for using the key.
    @Override
    public boolean use(Entity entity) {
        // Switch the game to dialogue state when the key is used.
        gp.gameState = gp.dialogueState;

        // Check if the player is near a door that can be opened.
        int objIndex = getDetected(entity, gp.obj, "Door");

        if (objIndex != 999) {
            // If a door is detected, open it and display a success message.
            gp.ui.currentDialogue = "You use the " + name + " and open the door.";
            gp.playSE(3); // Play the sound effect for using the key.
            gp.obj[gp.currentMap][objIndex] = null; // Remove the door from the map.
            return true;
        } else {
            // If no door is detected, display a failure message.
            gp.ui.currentDialogue = "Nothing happened.";
            return false;
        }
    }
}
