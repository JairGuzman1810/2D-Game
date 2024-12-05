package environment;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

// The Lighting class is responsible for creating and managing dynamic lighting effects,
// including a darkness filter with a radial gradient centered on the player
public class Lighting {

    GamePanel gp;                 // Reference to the game panel.
    BufferedImage darknessFilter; // Image overlay for the darkness effect.
    public int dayCounter;        // Counter to track the progression of time during the day-night cycle.
    public float filterAlpha = 0; // Alpha value controlling the opacity of the darkness filter.

    // Constants representing the different states of the day-night cycle.
    public final int day = 0;
    public final int dusk = 1;
    public final int night = 2;
    public final int dawn = 3;

    public int dayState = day;    // Current state of the day-night cycle.

    // Constructor creates the darkness filter with a radial gradient.
    public Lighting(GamePanel gp) {
        this.gp = gp;
        setLightSource(); // Initialize the light source.
    }

    // The setLightSource method creates a buffered image with a radial gradient
    // representing the current light source or darkness effect.
    public void setLightSource() {
        // Create a buffered image to serve as the darkness overlay.
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        if (gp.player.currentLight == null) {
            // If no light source, apply a uniform darkness overlay.
            g2.setColor(new Color(0, 0, 0.1f, 0.90f));
        } else {
            // Calculate the center of the light circle.
            int centerX = gp.player.screenX + (gp.tileSize) / 2;
            int centerY = gp.player.screenY + (gp.tileSize) / 2;

            // Define the color and transparency levels for the gradient.
            Color[] color = new Color[12];
            float[] fraction = new float[12];

            // Gradation colors from transparent to opaque.
            color[0] = new Color(0, 0, 0.1f, 0.1f); // Lightest edge.
            color[1] = new Color(0, 0, 0.1f, 0.42f);
            color[2] = new Color(0, 0, 0.1f, 0.52f);
            color[3] = new Color(0, 0, 0.1f, 0.61f);
            color[4] = new Color(0, 0, 0.1f, 0.69f);
            color[5] = new Color(0, 0, 0.1f, 0.76f);
            color[6] = new Color(0, 0, 0.1f, 0.82f);
            color[7] = new Color(0, 0, 0.1f, 0.87f);
            color[8] = new Color(0, 0, 0.1f, 0.91f);
            color[9] = new Color(0, 0, 0.1f, 0.94f);
            color[10] = new Color(0, 0, 0.1f, 0.96f);
            color[11] = new Color(0, 0, 0.1f, 0.98f); // Darkest center.

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

    // Resets the day-night cycle to its initial state.
    public void resetDay() {
        dayState = day;          // Set the current day-night cycle phase to "day."
        dayCounter = 0;          // Reset the day counter to the start of the cycle.
        filterAlpha = 0f;        // Clear any darkness effect by setting opacity to 0.
    }

    // The update method checks if the player's light source has changed
    // and updates the lighting effect accordingly.
    public void update() {
        if (gp.player.lightUpdated) {
            setLightSource();            // Refresh the lighting effect.
            gp.player.lightUpdated = false;
        }

        // Handle transitions between different day states.
        if (dayState == day) {
            dayCounter++;

            if (dayCounter > 600) {      // Transition to dusk after 600 ticks.
                dayState = dusk;
                dayCounter = 0;
            }
        }

        if (dayState == dusk) {
            filterAlpha += 0.001f;       // Gradually darken the screen during dusk.

            if (filterAlpha > 1f) {      // Once fully dark, transition to night.
                filterAlpha = 1f;
                dayState = night;
            }
        }

        if (dayState == night) {
            dayCounter++;
            if (dayCounter > 9000) {     // After a prolonged night, transition to dawn.
                dayState = dawn;
                dayCounter = 0;
            }
        }

        if (dayState == dawn) {
            filterAlpha -= 0.001f;       // Gradually brighten the screen during dawn.

            if (filterAlpha < 0f) {      // Once fully bright, transition to day.
                filterAlpha = 0f;
                dayState = day;
            }
        }
    }

    // Draw the darkness filter on the screen.
    public void draw(Graphics2D g2) {
        // Set the alpha composite for the darkness overlay.
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        g2.drawImage(darknessFilter, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Reset composite.

        // Display the current day state (Day, Dusk, Night, or Dawn) on the screen.
        String situation = "";

        switch (dayState) {
            case day -> situation = "Day";
            case dusk -> situation = "Dusk";
            case night -> situation = "Night";
            case dawn -> situation = "Dawn";
        }

        g2.setColor(Color.white);        // Set text color.
        g2.setFont(g2.getFont().deriveFont(50f)); // Set font size.
        g2.drawString(situation, 800, 500); // Draw the current state on the screen.
    }
}
