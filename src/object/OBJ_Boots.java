package object;

import entity.Entity;
import main.GamePanel;

import java.util.logging.Logger;

// OBJ_Boots defines the boots object in the game, which can increase the player's speed.
public class OBJ_Boots extends Entity {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Boots.class.getName());

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the boot's name and loads its image.
    public OBJ_Boots(GamePanel gp) {
        super(gp);

        // Set the name of the boots object.
        name = "Boots";
        // Load the boots image from resources.
        down1 = setup("/objects/boots");

    }
}
