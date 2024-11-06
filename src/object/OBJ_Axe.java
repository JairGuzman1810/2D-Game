package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Axe defines an axe object in the game, providing moderate attack power and the ability to cut trees.
// When equipped by an entity, it increases the entity's attack value.
public class OBJ_Axe extends Entity {

    // Constructor that sets up the axe's name, image, and attack value.
    public OBJ_Axe(GamePanel gp) {
        super(gp);

        // Define the type of this entity as an axe.
        type = type_axe;

        // Set the name of the axe.
        name = "Woodcutter's Axe";

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
    }
}