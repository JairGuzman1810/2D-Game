package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Shield_Wood defines a wooden shield object in the game, providing basic defense.
// When equipped by an entity, it increases the entity's defense value.
public class OBJ_Shield_Wood extends Entity {

    // String identifier for the class, representing the specific name of this object type.
    public static final String objName = "Wood Shield";

    // Constructor that sets up the shield's name, image, and defense value.
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        // Define the type of this entity as a shield.
        type = type_shield;

        // Set the name of the shield.
        name = objName;

        // Load the wooden shield image from resources.
        down1 = setup("/objects/shield_wood", gp.tileSize, gp.tileSize);

        // Set the defense value that this shield provides.
        defenseValue = 1;

        // Provides a short description of the item, shown in the inventory.
        description = "[" + name + "]\nMade with wood.";

        // Sets the price in the in-game currency, determining its value for purchasing.
        price = 35;
    }
}
