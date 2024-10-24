package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

// The TileManager class is responsible for managing and rendering the tiles in the game.
// It loads tile images and the map layout from a specified file.
public class TileManager {
    // Logger for logging error messages related to tile management.
    private static final Logger logger = Logger.getLogger(TileManager.class.getName());

    // Reference to the GamePanel, which holds the game state and properties.
    GamePanel gp;

    // Array of tiles that can be used in the game.
    public Tile[] tiles;

    // 2D array representing the map layout, where each element corresponds to a tile type.
    public int[][] mapTileNum;

    // Constructor that initializes the TileManager with a reference to GamePanel.
    // It also prepares the tile images and loads the map layout.
    public TileManager(GamePanel gp) {
        this.gp = gp; // Assign the GamePanel reference to this TileManager instance.
        tiles = new Tile[10]; // Initialize the tile array with a fixed size (10 tiles).
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; // Set up the map array based on the world dimensions.
        getTileImage(); // Load the tile images from resources.
        loadMap("/maps/world01.txt"); // Load the map layout from a specified text file.
    }

    // The getTileImage method loads the images for each type of tile from the resources.
    public void getTileImage() {
        try {
            // Initialize each tile and load its corresponding image.
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass.png")));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/wall.png")));
            tiles[1].collision = true;

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/water.png")));
            tiles[2].collision = true;


            tiles[3] = new Tile();
            tiles[3].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/earth.png")));

            tiles[4] = new Tile();
            tiles[4].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/tree.png")));
            tiles[4].collision = true;


            tiles[5] = new Tile();
            tiles[5].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/sand.png")));

        } catch (IOException e) {
            // Log an error if image loading fails.
            logger.log(Level.SEVERE, "Failed to load tile image", e);
        } catch (NullPointerException e) {
            // Log a warning if an image file is missing or not found.
            logger.log(Level.WARNING, "Image not found for tile!", e);
        }
    }

    // The loadMap method reads a map layout from a specified text file.
    // It populates the mapTileNum array with the tile indices.
    public void loadMap(String filePath) {
        try {
            // Open the map file and read it line by line.
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null; // Ensure the input stream is not null, indicating the file exists.
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0; // Current column in the map.
            int row = 0; // Current row in the map.

            // Read the map layout until the entire array is populated.
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine(); // Read a line from the map file.

                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" "); // Split the line into individual tile indices.

                    int num = Integer.parseInt(numbers[col]); // Parse the tile index as an integer.

                    mapTileNum[col][row] = num; // Store the tile index in the map array.
                    col++; // Move to the next column.
                }

                // Move to the next row if the current row is fully populated.
                if (col == gp.maxWorldCol) {
                    col = 0; // Reset column index.
                    row++; // Move to the next row.
                }
            }

            br.close(); // Close the BufferedReader to release resources.

        } catch (Exception e) {
            // Log a warning if the map file cannot be found or read.
            logger.log(Level.WARNING, "Map not found!", e);
        }
    }

    // The draw method renders the tiles onto the screen based on the map layout.
    public void draw(Graphics2D g2) {
        int worldCol = 0; // Current column in the map.
        int worldRow = 0; // Current row in the map.

        // Loop through the mapTileNum array and draw each tile.
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow]; // Get the tile index for the current position.

            // Calculate the world and screen coordinates for drawing the tile.
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Check if the tile is within the player's visible area before drawing.
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                // Draw the tile image at the calculated position.
                g2.drawImage(tiles[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++; // Move to the next column.

            // If the current row is fully populated, move to the next row.
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0; // Reset column index.
                worldRow++; // Move to the next row.
            }
        }
    }
}
