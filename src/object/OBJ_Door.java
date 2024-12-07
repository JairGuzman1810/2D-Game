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

        this.gp = gp;

        // Set the name of the door object.
        name = "Door";
        // Load the door image from resources.
        down1 = setup("/objects/door", gp.tileSize, gp.tileSize);
        // Set collision property to true, indicating that the door can block the player's path.
        collision = true;
        // Set the type of the object to "obstacle," as doors obstruct the player's movement.
        type = type_obstacle;

        // Sets the dialogue that will be displayed when interacting with the door.
        setDialogue();
    }

    // Sets the series of dialogues for this door, which will display sequentially when interact to.
    public void setDialogue() {
        dialogues[0][0] = "You need a key to open this";
    }

    // Handles the interaction logic for the door object.
    @Override
    public void interact() {
        startDialogue(this, 0);
    }
}
