package environment;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

// The Lighting class is responsible for creating and managing dynamic lighting effects,
// including a darkness filter with a radial gradient centered on the player
public class Lighting {

    GamePanel gp;                 // Reference to the game panel.
    BufferedImage darknessFilter; // Image overlay for the darkness effect.

    // Constructor creates the darkness filter with a radial gradient.
    public Lighting(GamePanel gp) {
        this.gp = gp;
        setLightSource();

    }

    // The setLightSource method creates a buffered image with a radial gradient
    // representing the current light source or darkness effect.
    public void setLightSource() {
        // Create a buffered image to serve as the darkness overlay.
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        if (gp.player.currentLight == null) {
            // If no light source, apply a uniform darkness overlay.
            g2.setColor(new Color(0, 0, 0, 0.90f));
        } else {
            // Calculate the center of the light circle.
            int centerX = gp.player.screenX + (gp.tileSize) / 2;
            int centerY = gp.player.screenY + (gp.tileSize) / 2;

            // Define the color and transparency levels for the gradient.
            Color[] color = new Color[12];
            float[] fraction = new float[12];

            // Gradation colors from transparent to opaque.
            color[0] = new Color(0, 0, 0, 0.1f); // Lightest edge.
            color[1] = new Color(0, 0, 0, 0.42f);
            color[2] = new Color(0, 0, 0, 0.52f);
            color[3] = new Color(0, 0, 0, 0.61f);
            color[4] = new Color(0, 0, 0, 0.69f);
            color[5] = new Color(0, 0, 0, 0.76f);
            color[6] = new Color(0, 0, 0, 0.82f);
            color[7] = new Color(0, 0, 0, 0.87f);
            color[8] = new Color(0, 0, 0, 0.91f);
            color[9] = new Color(0, 0, 0, 0.94f);
            color[10] = new Color(0, 0, 0, 0.96f);
            color[11] = new Color(0, 0, 0, 0.98f); // Darkest center.

            // Gradation stops for the radial gradient.
            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.5f;
            fraction[3] = 0.6f;
            fraction[4] = 0.65f;
            fraction[5] = 0.7f;
            fraction[6] = 0.75f;
            fraction[7] = 0.8f;
            fraction[8] = 0.85f;
            fraction[9] = 0.9f;
            fraction[10] = 0.95f;
            fraction[11] = 1f;

            // Create a radial gradient paint with the defined settings.
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, gp.player.currentLight.lightRadius, fraction, color);

            // Apply the gradient to the image.
            g2.setPaint(gPaint);
        }

        // Fill the screen with the gradient.
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.dispose(); // Release resources.
    }

    // The update method checks if the player's light source has changed
    // and updates the lighting effect accordingly.
    public void update() {
        if (gp.player.lightUpdated) {
            setLightSource();
            gp.player.lightUpdated = false;
        }
    }

    // Draw the darkness filter on the screen.
    public void draw(Graphics2D g2) {
        g2.drawImage(darknessFilter, 0, 0, null);
    }
}
