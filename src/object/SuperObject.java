package object;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

// SuperObject is the base class for all objects in the game (e.g., keys, doors, chests).
public class SuperObject {

    // The image representing the object (e.g., key, door, etc.).
    public BufferedImage image;

    // The name of the object.
    public String name;

    // Indicates whether the object can trigger collisions.
    public boolean collision = false;

    // The object's position in the game world.
    public int worldX, worldY;

    // Draws the object on the screen relative to the player's position.
    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Check if the object is within the player's visible area.
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
