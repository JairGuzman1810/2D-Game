package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Door defines the door object in the game, which can block the player's path.
public class OBJ_Door extends Entity {


    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;


    // Constructor that sets the door's name and loads its image.
    public OBJ_Door(GamePanel gp) {
        super(gp);
        // Set the name of the door object.
        name = "Door";
        // Load the door image from resources.
        down1 = setup("/objects/door", gp.tileSize, gp.tileSize);
        // Set collision property to true, indicating that the door can block the player's path.
        collision = true;
    }
}
