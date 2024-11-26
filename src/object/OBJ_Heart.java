package object;

import entity.Entity;
import main.GamePanel;

// OBJ_Heart represents a heart object in the game that is used for visualizing the player's
// current and maximum life and use as pick up item for recovering life.
public class OBJ_Heart extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the heart's name and loads its image.
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // Defines the healing value that this heart provides.
        value = 2;

        // Define the type of this entity as a pickup only item.
        type = type_pickupOnly;

        // Set the name of the heart object.
        name = "Heart";
        // Load the hearts image from resources.
        down1 = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
        image = setup("/objects/heart_full", gp.tileSize, gp.tileSize);
        image2 = setup("/objects/heart_half", gp.tileSize, gp.tileSize);
        image3 = setup("/objects/heart_blank", gp.tileSize, gp.tileSize);
    }

    @Override
    // Overrides the `use` method to action of restore health.
    // Restores the entity's health.
    public boolean use(Entity entity) {
        super.use(entity);

        // Play sound effect associated with pickup the heart.
        gp.playSE(2);

        // Display the amount of life restored.
        gp.ui.addMessage("Life +" + value);

        // Increase the entity's life by the heart's value.
        entity.life += value;

        return true;
    }
}
