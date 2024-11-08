package object;

import entity.Entity;
import main.GamePanel;

// OBJ_ManaCrystal represents a mana crystal object in the game that is used for visualizing the player's
// current and maximum mana and use as pick up item for recovering mana.
public class OBJ_ManaCrystal extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the mana's name and loads its image.
    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // Defines the mana value that this crystal provides.
        value = 1;

        // Define the type of this entity as a pickup only item.
        type = type_pickupOnly;

        // Set the name of the mana object.
        name = "Mana";

        // Load the mana image from resources.
        down1 = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image = setup("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    }

    @Override
    // Overrides the `use` method to action of restore mana.
    // Restores the entity's mana.
    public void use(Entity entity) {
        super.use(entity);

        // Play sound effect associated with pickup the crystal.
        gp.playSE(2);

        // Display the amount of mana restored
        gp.ui.addMessage("Mana +" + value);

        // Increase the entity's mana by the crystal's value.
        entity.mana += value;
    }
}
