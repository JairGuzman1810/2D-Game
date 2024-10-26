package main;

import java.awt.*;
import java.awt.image.BufferedImage;

// The UtilityTool class provides helper methods for image manipulation,
// including scaling images to specified dimensions for better rendering performance in the game.
public class UtilityTool {

    // Scales a given BufferedImage to the specified width and height
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        // Create a new BufferedImage to hold the scaled image
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics(); // Create a Graphics2D object to draw on the new image
        g2.drawImage(original, 0, 0, width, height, null); // Draw the original image scaled to the new dimensions
        g2.dispose(); // Dispose of the graphics context to free up resources
        return scaledImage; // Return the scaled image
    }
}
