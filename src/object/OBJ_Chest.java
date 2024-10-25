package object;

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

    // Constructor that sets the chest's name and loads its image.
    public OBJ_Chest() {
        name = "Chest";

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/chest.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load chest image", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found for chest!", e);
        }
    }
}
