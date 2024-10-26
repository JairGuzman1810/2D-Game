package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// OBJ_Boots defines the boots object in the game, which can increase the player's speed.
public class OBJ_Boots extends SuperObject {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Boots.class.getName());

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the boot's name and loads its image.
    public OBJ_Boots(GamePanel gp) {
        this.gp = gp; // Store the reference to the GamePanel for accessing game settings.

        name = "Boots"; // Set the name of the boots object.

        try {
            // Load the boots image from the resources folder.
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/boots.png")));
            // Scale the loaded image to match the tile size defined in GamePanel.
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize); // Scale the image for proper display size.

        } catch (IOException e) {
            // Log an error if the image fails to load.
            logger.log(Level.SEVERE, "Failed to load boots image", e);
        } catch (NullPointerException e) {
            // Log a warning if the image resource is not found.
            logger.log(Level.WARNING, "Image not found for boots!", e);
        }
    }
}
