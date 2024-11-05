package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Sword_Normal defines a basic sword object in the game, providing standard attack power.
// When equipped by an entity, it increases the entity's attack value.
public class OBJ_Sword_Normal extends Entity {

    // Constructor that sets up the sword's name, image, and attack value.
    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        // Set the name of the sword.
        name = "Normal Sword";

        // Load the normal sword image from resources.
        down1 = setup("/objects/sword_normal", gp.tileSize, gp.tileSize);

        // Set the attack value that this sword provides.
        attackValue = 1;

        // Provides a short description of the item, shown in the inventory.
        description = "[" + name + "]\n An old sword.";
    }
}
