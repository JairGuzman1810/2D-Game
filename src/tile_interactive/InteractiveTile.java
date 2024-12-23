package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

// InteractiveTile represents a tile in the game world that can be interacted with
// and may have specific properties, like being destructible.
public class InteractiveTile extends Entity {

    // Reference to the GamePanel, which contains game settings and properties.
    GamePanel gp;

    // Indicates if this tile can be destroyed by the player or other entities.
    public boolean destructible = false;

    // Constructor to create an InteractiveTile object at a specified position.
    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp); // Call the Entity constructor to initialize base properties.
        this.gp = gp; // Store the GamePanel reference.
    }

    // Checks if the interacting entity has the correct item required for interaction.
    // Returns false by default; subclasses can override this to specify required items.
    public boolean isCorrectItem(Entity entity) {
        return false;
    }

    // Plays a sound effect when interacting with this tile.
    // Default implementation does nothing; subclasses can override to add sound effects.
    public void playSE() {
    }

    // Returns a variant of this tile that represents its destroyed state.
    // Default implementation returns null; subclasses can override to specify a variant.
    public InteractiveTile getDestroyedVariant() {
        return null;
    }

    // Updates the tile’s state, including handling temporary invincibility.
    @Override
    public void update() {
        // Check if the tile is in an invincible state.
        if (invincible) {
            invincibleCounter++; // Track invincibility duration.

            // Disable invincibility after 20 frames and reset the counter.
            if (invincibleCounter > 20) {
                invincible = false; // End invincibility state.
                invincibleCounter = 0; // Reset the counter for next use.
            }
        }
    }

    @Override
    // Draws the entity on the screen relative to the player's position.
    public void draw(Graphics2D g2) {
        // Calculate the object's position on the screen based on the player's position.
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;


        // Check if the entity is within the player's visible area.
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            // Draw the entity's image on the screen at the calculated position.
            g2.drawImage(down1, screenX, screenY, null);

        }
    }
}