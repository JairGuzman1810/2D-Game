package tile;

import main.GamePanel;
import main.UtilityTool;

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
    public int[][][] mapTileNum;

    // Constructor that initializes the TileManager with a reference to GamePanel.
    // It also prepares the tile images and loads the map layout.
    public TileManager(GamePanel gp) {
        this.gp = gp; // Assign the GamePanel reference to this TileManager instance.
        tiles = new Tile[50]; // Initialize the tile array with a fixed size (50 tiles).
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow]; // Set up the map array based on the world dimensions.
        getTileImage(); // Load the tile images from resources.
        loadMap("/maps/worldV3.txt", 0); // Load the map layout from a specified text file.
        loadMap("/maps/interior01.txt", 1); // Load the hut map.
    }

    // The getTileImage method loads the images for each type of tile from the resources.
    public void getTileImage() {
        // Initialize each tile by calling the setup method, which loads the corresponding image
        // and defines its collision properties.

        // Placeholder tiles - these are temporary until actual images are defined.
        setup(0, "grass00", false);
        setup(1, "grass00", false);
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4, "grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);

        // Grass tiles with no collision properties.
        setup(10, "grass00", false);
        setup(11, "grass01", false);

        // Water tiles with collision properties.
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);

        // Road tiles with no collision properties.
        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);

        // Other tiles with specified properties.
        setup(39, "earth", false); // Earth tile with no collision properties.
        setup(40, "wall", true);   // Wall tile with collision properties.
        setup(41, "tree", true);   // Tree tile with collision properties.

        setup(42, "hut", false);   // Hut tile with no collision properties.
        setup(43, "floor01", false);   // Floor tile with no collision properties.
        setup(44, "table01", true);   // Table tile with collision properties.


    }


    // The setup method initializes a tile at a specific index and loads its image.
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool(); // Create an instance of UtilityTool for image scaling.

        try {
            // Initialize the tile object at the specified index.
            tiles[index] = new Tile();
            // Load the image from the resources and scale it to the desired size.
            tiles[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/" + imageName + ".png")));
            tiles[index].image = uTool.scaleImage(tiles[index].image, gp.tileSize, gp.tileSize);
            // Set the collision property for this tile.
            tiles[index].collision = collision;

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
    public void loadMap(String filePath, int map) {
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

                    mapTileNum[map][col][row] = num; // Store the tile index in the map array.
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
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow]; // Get the tile index for the current position.

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
                g2.drawImage(tiles[tileNum].image, screenX, screenY, null);
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
