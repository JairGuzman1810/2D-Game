package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OBJ_Heart extends SuperObject {

    // Logger for logging errors during image loading.
    private static final Logger logger = Logger.getLogger(OBJ_Heart.class.getName());

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the heart's name and loads its image.
    public OBJ_Heart(GamePanel gp) {
        this.gp = gp; // Store the reference to the GamePanel for accessing game settings.

        name = "Heart";

        try {
            // Load the hearts image from resources.
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_full.png")));
            image2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_half.png")));
            image3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/heart_blank.png")));
            // Scale the loaded images to match the tile size defined in GamePanel.
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize); // Scale the image for proper display size.
            image2 = uTool.scaleImage(image2, gp.tileSize, gp.tileSize); // Scale the image2 for proper display size.
            image3 = uTool.scaleImage(image3, gp.tileSize, gp.tileSize); // Scale the image3 for proper display size.


        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load heart image", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found for heart!", e);
        }
    }
}
