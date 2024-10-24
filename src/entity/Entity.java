package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

// The Entity class serves as a base class for any game entity (like a player or enemy).
// It contains the common attributes of an entity, such as position (x, y), speed, and collision settings.
public class Entity {

    // The entity's position in the game world (worldX, worldY).
    public int worldX, worldY;

    // The speed of the entity, determining how fast it moves per frame.
    public int speed;

    // BufferedImages representing different frames of the player's movement in each direction.
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    // Stores the current direction of the player's movement (up, down, left, right).
    public String direction;

    // spriteCounter keeps track of the number of frames elapsed for animating the sprite.
    public int spriteCounter = 0;

    // spriteNum indicates which sprite image (1 or 2) to display for animation.
    public int spriteNum = 1;

    // A Rectangle representing the entity's solid area used for collision detection.
    // This defines the area around the entity that is checked for collisions.
    public Rectangle solidArea;

    // A flag that indicates whether a collision has occurred (true) or not (false).
    public boolean collisionOn = false;
}
