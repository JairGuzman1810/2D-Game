package entity;

import main.GamePanel;
import object.*;

import java.awt.*;

// NPC_Merchant represents an NPC character that acts as a merchant in the game.
// This class inherits from the Entity class, which provides fundamental properties
// and behaviors for all entities. The NPC_Merchant class extends these by adding
// specific functionality such as trade interactions, dialogue, and a predefined inventory.
public class NPC_Merchant extends Entity {

    // Initializes the NPC_Merchant with a reference to the GamePanel.
    // Sets the initial direction and speed, and loads its images.
    public NPC_Merchant(GamePanel gp) {
        super(gp);

        direction = "down"; // Sets the initial direction for the NPC to "down".
        speed = 1;          // Sets the movement speed of the NPC.

        // Define the solid area for the merchant, which will be used for collision detection.
        solidArea = new Rectangle();
        solidArea.x = 8;  // Offset of the solid area within the merchant's sprite.
        solidArea.y = 16; // Offset within the sprite.
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;  // Width of the merchant collision area.
        solidArea.height = 32; // Height of the merchant collision area.

        getImage();    // Loads images for the NPC's directional movement animations.

        setDialogue(); // Sets the dialogue that will be displayed when speaking with the NPC.

        setItems();    // Populates the merchant's inventory with items available for trading.

    }

    // Loads the images to represent
    // the merchant using the setup method to load and scale each image.
    public void getImage() {

        up1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
    }

    // Sets the series of dialogues for this NPC, which will display sequentially when spoken to.
    public void setDialogue() {
        dialogues[0] = "He he, so you found me.\nI have some good stuff.\nDo you want to trade?";
    }

    @Override
    // Triggers the speak method inherited from Entity, which will manage dialogue display
    // and align the NPC’s direction towards the player during interaction.
    public void speak() {
        super.speak();
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }

    // Initializes the merchant's inventory with a predefined set of items for sale.
    // These items are available to the player during trade interactions.
    public void setItems() {

        // Adds a red potion to the inventory.
        inventory.add(new OBJ_Potion_Red(gp));

        // Adds a key to the inventory.
        inventory.add(new OBJ_Key(gp));

        // Adds a standard sword to the inventory.
        inventory.add(new OBJ_Sword_Normal(gp));

        // Adds an axe to the inventory.
        inventory.add(new OBJ_Axe(gp));

        // Adds a wooden shield to the inventory.
        inventory.add(new OBJ_Shield_Wood(gp));

        // Adds a blue shield to the inventory.
        inventory.add(new OBJ_Shield_Blue(gp));
    }
}