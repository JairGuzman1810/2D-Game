package object;

import entity.Entity;
import main.GamePanel;

// OBJ_ManaCrystal represents a mana crystal object in the game that is used for visualizing the player's
// current and maximum mana.
public class OBJ_ManaCrystal extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the mana's name and loads its image.
    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);
        this.gp = gp;
        // Set the name of the mana object.
        name = "Mana";
        // Load the mana image from resources.
        image = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    }
}
