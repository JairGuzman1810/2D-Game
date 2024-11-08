package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Potion_Red defines a red potion item in the game, which restores health to the player when used.
// When consumed by an entity, it increases the entity's life points by a fixed amount.
public class OBJ_Potion_Red extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets up the potion's name and loads its image.
    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        // Store the reference to the GamePanel for further access.
        this.gp = gp;

        // Defines the healing value that this potion provides.
        value = 5;

        // Define the type of this entity as a consumable item.
        type = type_consumable;

        // Set the name of the potion.
        name = "Red Potion";

        // Load the potion image from resources.
        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);

        // Provides a short description of the item, displayed in the inventory.
        description = "[" + name + "]\nHeals your life by " + value + ".";
    }

    @Override
    // Overrides the `use` method to apply the red potion effect.
    // Restores the entity's health, displays a message, and plays a sound.
    public void use(Entity entity) {
        super.use(entity);

        // Set the game state to dialogue state to display potion usage message.
        gp.gameState = gp.dialogueState;

        // Display message showing the effect of drinking the potion.
        gp.ui.currentDialogue = "You drink the " + name + "!\nYour life has been recovered by " + value + ".";

        // Increase the entity's life by the potion's value.
        entity.life += value;

        // Play sound effect associated with consuming the potion.
        gp.playSE(2);
    }
}
