package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Heart represents a heart object in the game that is used for visualizing the player's
// current and maximum life.
public class OBJ_Heart extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the heart's name and loads its image.
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        this.gp = gp;
        // Set the name of the heart object.
        name = "Heart";
        // Load the hearts image from resources.
        image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
        image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);
    }
}
