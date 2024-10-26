package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// OBJ_Chest defines the chest object in the game, which acts as the game’s treasure.
// When interacted with, it triggers the end game sequence.
public class OBJ_Chest extends SuperObject {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Chest.class.getName());

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the chest's name and loads its image.
    public OBJ_Chest(GamePanel gp) {
        this.gp = gp; // Store the reference to the GamePanel for accessing game settings.

        name = "Chest";

        try {
            // Load the chest image from resources.
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/chest.png")));
            // Scale the loaded image to match the tile size defined in GamePanel.
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize); // Scale the image for proper display size.


        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load chest image", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found for chest!", e);
        }
    }
}
