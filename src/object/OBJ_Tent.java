package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Tent defines the tent object in the game, which allows the player to skip to the next morning by sleeping.
public class OBJ_Tent extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor initializes the tent's properties, including its name, image, description, and functionality.
    public OBJ_Tent(GamePanel gp) {
        super(gp);

        this.gp = gp;

        // Set the name of the tent object.
        name = "Tent";

        // Load the tent image from resources with the appropriate tile size.
        down1 = setup("/objects/tent", gp.tileSize, gp.tileSize);

        // Provides a description of the item, shown in the inventory when selected.
        description = "[" + name + "]\nYou can sleep until\nnext morning.";

        // Sets the price of the tent in the game's currency for trading purposes.
        price = 300;

        // Specifies the item type as "consumable."
        type = type_consumable;

        // Enables stacking, allowing multiple tents to occupy a single inventory slot.
        stackable = true;
    }

    @Override
    // The `use` method handles the tent's functionality, allowing the player to rest and recover.
    public boolean use(Entity entity) {

        // Change the game state to sleep state to initiate the sleeping animation.
        gp.gameState = gp.sleepState;

        // Play the sleep sound effect.
        gp.playSE(14);

        // Restore the player's health and mana to their maximum values.
        gp.player.life = gp.player.maxLife;
        gp.player.mana = gp.player.maxMana;

        // Set the player's sleeping image to the tent sprite.
        gp.player.getSleepingImage(down1);

        return true; // Indicate that the item was successfully used.
    }
}