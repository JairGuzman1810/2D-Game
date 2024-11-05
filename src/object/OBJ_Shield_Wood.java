package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Shield_Wood defines a wooden shield object in the game, providing basic defense.
// When equipped by an entity, it increases the entity's defense value.
public class OBJ_Shield_Wood extends Entity {

    // Constructor that sets up the shield's name, image, and defense value.
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        // Set the name of the shield.
        name = "Wood Shield";

        // Load the wooden shield image from resources.
        down1 = setup("/objects/shield_wood", gp.tileSize, gp.tileSize);

        // Set the defense value that this shield provides.
        defenseValue = 1;

        // Provides a short description of the item, shown in the inventory.
        description = "[" + name + "]\nMade with wood.";
    }
}
