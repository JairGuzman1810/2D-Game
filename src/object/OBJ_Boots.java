package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// OBJ_Boots defines the boots object in the game, which can increase the player's speed.
public class OBJ_Boots extends SuperObject {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Boots.class.getName());

    // Constructor that sets the boot's name and loads its image.
    public OBJ_Boots() {
        name = "Boots"; // Set the name of the boots object.

        try {
            // Load the boots image from the resources folder.
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/boots.png")));
        } catch (IOException e) {
            // Log an error if the image fails to load.
            logger.log(Level.SEVERE, "Failed to load boots image", e);
        } catch (NullPointerException e) {
            // Log a warning if the image resource is not found.
            logger.log(Level.WARNING, "Image not found for boots!", e);
        }
    }
}
