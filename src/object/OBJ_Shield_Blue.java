package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Shield_Blue defines a wooden shield object in the game, providing moderate defense.
// When equipped by an entity, it increases the entity's defense value.
public class OBJ_Shield_Blue extends Entity {

    // String identifier for the class, representing the specific name of this object type.
    public static final String objName = "Blue Shield";

    // Constructor that sets up the shield's name, image, and defense value.
    public OBJ_Shield_Blue(GamePanel gp) {
        super(gp);

        // Define the type of this entity as a shield.
        type = type_shield;

        // Set the name of the shield.
        name = objName;

        // Load the wooden shield image from resources.
        down1 = setup("/objects/shield_blue", gp.tileSize, gp.tileSize);

        // Set the defense value that this shield provides.
        defenseValue = 2;

        // Provides a short description of the item, shown in the inventory.
        description = "[" + name + "]\nA shiny blue shield.";

        // Sets the price in the in-game currency, determining its value for purchasing.
        price = 250;
    }
}
