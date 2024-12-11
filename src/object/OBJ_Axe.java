package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Axe defines an axe object in the game, providing moderate attack power and the ability to cut trees.
// When equipped by an entity, it increases the entity's attack value.
public class OBJ_Axe extends Entity {

    // String identifier for the class, representing the specific name of this object type.
    public static final String objName = "Woodcutter's Axe";

    // Constructor that sets up the axe's name, image, and attack value.
    public OBJ_Axe(GamePanel gp) {
        super(gp);

        // Define the type of this entity as an axe.
        type = type_axe;

        // Set the name of the axe.
        name = objName;

        // Load the axe image from resources.
        down1 = setup("/objects/axe", gp.tileSize, gp.tileSize);

        // Attack value added when this axe is equipped.
        attackValue = 2;

        // Define the width of the axe's attack collision area.
        attackArea.width = 30;

        // Define the height of the axe's attack collision area.
        attackArea.height = 30;

        // Provides a short description of the item, displayed in the inventory.
        description = "[" + name + "]\nA bit rusty but still \ncan cut some trees.";

        // Sets the price in the in-game currency, determining its value for purchasing.
        price = 75;

        // Sets the strength of the knockback effect when hitting an enemy, pushing them away.
        knockBackPower = 10;

        // Sets the duration of the first attack motion phase (e.g., wind-up animation).
        motion1_duration = 20;

        // Sets the duration of the second attack motion phase (e.g., wind-up animation).
        motion2_duration = 40;
    }
}