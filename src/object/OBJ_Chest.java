package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Chest defines the chest object in the game, which acts as the game’s treasure.
// When interacted with, it triggers the end game sequence.
public class OBJ_Chest extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the chest's name and loads its image.
    public OBJ_Chest(GamePanel gp) {
        super(gp);
        // Set the name of the chest.
        name = "Chest";
        // Load the chest image from resources.
        down1 = setup("/objects/chest");
    }
}
