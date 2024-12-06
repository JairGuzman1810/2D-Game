package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

// OBJ_Chest represents a chest object in the game that contains loot.
// Players can interact with it to obtain items or rewards.
public class OBJ_Chest extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the chest's name and loads its image.
    public OBJ_Chest(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // Set the name of the chest.
        name = "Chest";
        // Load the chest image from resources.
        image = setup("/objects/chest", gp.tileSize, gp.tileSize);

        // Load the opened chest image.
        image2 = setup("/objects/chest_opened", gp.tileSize, gp.tileSize);

        // Default image shown for the chest when facing down.
        down1 = image;

        // Enable collision for the chest.
        collision = true;

        // Define the solid area for the chest, which will be used for collision detection.
        solidArea = new Rectangle();
        solidArea.x = 4;  // Offset of the solid area within the chest's sprite.
        solidArea.y = 16; // Offset within the sprite.
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 40;  // Width of the chest collision area.
        solidArea.height = 32; // Height of the chest collision area.

        // Set the type of this entity to an obstacle.
        type = type_obstacle;
    }

    // Assigns the loot that the chest will contain when opened by the player.
    // The loot can be any type of Entity, such as a weapon, potion, or coin.
    public void setLoot(Entity loot) {
        this.loot = loot; // Stores the given entity as the chest's loot.
    }

    // Handles interaction with the chest when the player interacts with it.
    @Override
    public void interact() {

        // Change the game state to dialogue mode.
        gp.gameState = gp.dialogueState;

        if (!isOpen) {
            // Play the chest opening sound effect.
            gp.playSE(3);

            // Build the dialogue for opening the chest.
            StringBuilder sb = new StringBuilder();

            sb.append("You open the chest and find a ").append(loot.name).append("!");

            // Check if the player's inventory is full.
            if (!gp.player.canObtainItem(loot)) {
                sb.append("\n... But you cannot carry any more!");
            } else {
                sb.append("\nYou obtain the ").append(loot.name).append("!");
                // Change the chest's appearance to the opened state.
                down1 = image2;
                isOpen = true;
            }

            // Display the dialogue in the UI.
            gp.ui.currentDialogue = sb.toString();

        } else {
            // If the chest is already open, display that it is empty.
            gp.ui.currentDialogue = "It's empty.";
        }
    }
}
