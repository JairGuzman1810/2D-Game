package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// OBJ_Door defines the door object in the game, which can block the player's path.
public class OBJ_Door extends SuperObject {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Door.class.getName());

    // Constructor that sets the door's name and loads its image.
    public OBJ_Door() {
        // Set the name of the door object.
        name = "Door";

        try {
            // Load the door image from the resources folder.
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/door.png")));
        } catch (IOException e) {
            // Log an error if the image fails to load.
            logger.log(Level.SEVERE, "Failed to load door image", e);
        } catch (NullPointerException e) {
            // Log a warning if the image resource is not found.
            logger.log(Level.WARNING, "Image not found for door!", e);
        }

        // Set collision property to true, indicating that the door can block the player's path.
        collision = true;
    }
}
