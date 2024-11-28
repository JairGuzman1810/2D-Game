package environment;

import main.GamePanel;

import java.awt.*;

// The EnvironmentManager class handles environmental effects in the game,
// such as lighting, and ensures they are rendered properly during gameplay
public class EnvironmentManager {

    GamePanel gp; // Reference to the game panel.
    Lighting lighting; // Lighting effect instance.

    // Constructor initializes the environment manager with the game panel.
    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    // Setup method initializes the lighting with a specific radius.
    public void setup() {
        lighting = new Lighting(gp, 350);
    }

    // Draw method renders the lighting effect.
    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }
}