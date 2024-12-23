package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Sword_Normal defines a basic sword object in the game, providing standard attack power.
// When equipped by an entity, it increases the entity's attack value.
public class OBJ_Sword_Normal extends Entity {

    // String identifier for the class, representing the specific name of this object type.
    public static final String objName = "Normal Sword";

    // Constructor that sets up the sword's name, image, and attack value.
    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        // Define the type of this entity as a sword.
        type = type_sword;

        // Set the name of the sword.
        name = objName;

        // Load the normal sword image from resources.
        down1 = setup("/objects/sword_normal", gp.tileSize, gp.tileSize);

        // Set the attack value that this sword provides.
        attackValue = 1;

        // Width of the sword attack collision area.
        attackArea.width = 36;

        // Height of the sword attack collision area.
        attackArea.height = 36;

        // Provides a short description of the item, shown in the inventory.
        description = "[" + name + "]\n An old sword.";

        // Sets the price in the in-game currency, determining its value for purchasing.
        price = 20;

        // Sets the strength of the knockback effect when hitting an enemy, pushing them away.
        knockBackPower = 2;

        // Sets the duration of the first attack motion phase (e.g., wind-up animation).
        motion1_duration = 5;

        // Sets the duration of the second attack motion phase (e.g., wind-up animation).
        motion2_duration = 25;
    }
}
