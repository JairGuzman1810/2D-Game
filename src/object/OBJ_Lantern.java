package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Lantern defines a lantern object that provides a light source in the game.
// When used, it illuminates the area around the player by increasing the light radius.
public class OBJ_Lantern extends Entity {

    // Constructor initializes the lantern's attributes such as name, image, description, price, and light radius.
    public OBJ_Lantern(GamePanel gp) {
        super(gp);

        // Sets the type of the item as a light source.
        type = type_light;

        // Assigns a name to the lantern, which is used in the inventory and interactions.
        name = "Lantern";

        // Loads the image for the lantern from the resources folder.
        down1 = setup("/objects/lantern", gp.tileSize, gp.tileSize);

        // Provides a description of the lantern, displayed in the player's inventory.
        description = "[" + name + "]\nIlluminates your \nsurroundings.";

        // Sets the lantern's price in the in-game currency.
        // This value determines how much the lantern costs to purchase or its resale value.
        price = 200;

        // Specifies the radius of the light emitted by the lantern.
        // This value is used to determine how far the light reaches around the player.
        lightRadius = 250;
    }
}