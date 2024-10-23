package tile;

import java.awt.image.BufferedImage;

// The Tile class represents a single tile in the game world.
// It contains the tile's image and a boolean indicating whether the tile causes collision.
public class Tile {
    // BufferedImage representing the visual appearance of the tile.
    public BufferedImage image;

    // Indicates whether this tile causes collision with entities (e.g., walls).
    public boolean collision = false;
}
