package object;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

// SuperObject is the base class for all objects in the game (e.g., keys, doors, chests).
public class SuperObject {

    // The image representing the object (e.g., key, door, etc.).
    public BufferedImage image, image2, image3;

    // The name of the object.
    public String name;

    // Indicates whether the object can trigger collisions.
    public boolean collision = false;

    // The object's position in the game world.
    public int worldX, worldY;

    // Rectangle representing the solid area for collision detection.
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    // Default position for the solid area (X coordinate).
    public int solidAreaDefaultX = 0;

    // Default position for the solid area (Y coordinate).
    public int solidAreaDefaultY = 0;

    // UtilityTool instance for image manipulation and scaling operations.
    UtilityTool uTool = new UtilityTool();

    // Draws the object on the screen relative to the player's position.
    public void draw(Graphics2D g2, GamePanel gp) {
        // Calculate the object's position on the screen based on the player's position.
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Check if the object is within the player's visible area.
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            // Draw the object's image on the screen at the calculated position.
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
