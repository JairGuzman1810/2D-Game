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
    }

    // Handles the interaction logic for the door object.
    @Override
    public void interact() {
        // Switch the game to dialogue state when interacting with the door.
        gp.gameState = gp.dialogueState;

        // Display a message indicating that a key is required to open the door.
        gp.ui.currentDialogue = "You need a key to open this";
    }
}
