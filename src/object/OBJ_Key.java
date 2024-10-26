package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// OBJ_Key defines the key object in the game, which can be collected by the player.
public class OBJ_Key extends SuperObject {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Key.class.getName());

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the key's name and loads its image.
    public OBJ_Key(GamePanel gp) {
        this.gp = gp; // Store the reference to the GamePanel for accessing game settings.

        name = "Key";

        try {
            // Load the key image from resources.
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/key.png")));
            // Scale the loaded image to match the tile size defined in GamePanel.
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); // Scale the image for proper display size.

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load key image", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found for key!", e);
        }
    }
}
