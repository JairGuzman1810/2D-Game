package environment;

import main.GamePanel;

import java.awt.*;

// The EnvironmentManager class handles environmental effects in the game,
// such as lighting, and ensures they are rendered properly during gameplay
public class EnvironmentManager {

    GamePanel gp; // Reference to the game panel.
    public Lighting lighting; // Lighting effect instance.

    // Constructor initializes the environment manager with the game panel.
    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    // Updates the environments effect, ensuring it reflects any changes, such as when the player switches.
    public void update() {
        lighting.update();
    }

    // Setup method initializes the environments.
    public void setup() {
        lighting = new Lighting(gp);
    }

    // Draw method renders the environment effect.
    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }
}