package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Boots defines the boots object in the game, which can increase the player's speed.
public class OBJ_Boots extends Entity {

    // String identifier for the class, representing the specific name of this object type.
    public static final String objName = "Boots";
    
    // Constructor that sets the boot's name and loads its image.
    public OBJ_Boots(GamePanel gp) {
        super(gp);

        // Set the name of the boots object.
        name = objName;
        // Load the boots image from resources.
        down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);

    }
}
