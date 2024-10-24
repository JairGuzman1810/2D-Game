package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// OBJ_Key defines the key object in the game, which can be collected by the player.
public class OBJ_Key extends SuperObject {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Key.class.getName());

    // Constructor that sets the key's name and loads its image.
    public OBJ_Key() {
        name = "Key";

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/key.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load key image", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found for key!", e);
        }
    }
}
