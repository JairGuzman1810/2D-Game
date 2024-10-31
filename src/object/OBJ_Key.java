package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Key defines the key object in the game, which can be collected by the player.
public class OBJ_Key extends Entity {


    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the key's name and loads its image.
    public OBJ_Key(GamePanel gp) {
        super(gp);
        // Set the name of the key object.
        name = "Key";
        // Load the key image from resources.
        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
    }
}
