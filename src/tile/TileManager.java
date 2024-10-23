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
    private static final Logger logger = Logger.getLogger(TileManager.class.getName());

    // Reference to the GamePanel, which holds the game state and properties.
    GamePanel gp;

    // Array of tiles that can be used in the game.
    Tile[] tiles;

    // 2D array representing the map layout, where each element corresponds to a tile type.
    int[][] mapTileNum;

    // Constructor that initializes the TileManager with a reference to GamePanel.
    // It also prepares the tile images and loads the map layout.
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10]; // Initialize the tile array with a fixed size.
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow]; // Set up the map array based on the screen dimensions.
        getTileImage(); // Load the tile images.
        loadMap("/maps/map01.txt"); // Load the map layout from a file.
    }

    // The getTileImage method loads the images for each type of tile from the resources.
    public void getTileImage() {
        try {
            // Initialize the tiles and load their images.
            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass.png")));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/wall.png")));

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/water.png")));
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
            assert is != null; // Ensure the input stream is not null.
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0; // Current column in the map.
            int row = 0; // Current row in the map.

            // Read the map layout until the entire array is populated.
            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                String line = br.readLine(); // Read a line from the map file.

                while (col < gp.maxScreenCol) {
                    String[] numbers = line.split(" "); // Split the line into individual tile indices.

                    int num = Integer.parseInt(numbers[col]); // Parse the tile index as an integer.

                    mapTileNum[col][row] = num; // Store the tile index in the map array.
                    col++; // Move to the next column.
                }

                // Move to the next row if the current row is fully populated.
                if (col == gp.maxScreenCol) {
                    col = 0; // Reset column index.
                    row++; // Move to the next row.
                }
            }

            br.close(); // Close the BufferedReader.

        } catch (Exception e) {
            // Log a warning if the map file cannot be found or read.
            logger.log(Level.WARNING, "Map not found!", e);
        }
    }

    // The draw method renders the tiles onto the screen based on the map layout.
    public void draw(Graphics2D g2) {
        int col = 0; // Current column in the map.
        int row = 0; // Current row in the map.
        int x = 0; // X-coordinate for drawing the tile.
        int y = 0; // Y-coordinate for drawing the tile.

        // Loop through the mapTileNum array and draw each tile.
        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            int tileNum = mapTileNum[col][row]; // Get the tile index for the current position.

            g2.drawImage(tiles[tileNum].image, x, y, gp.tileSize, gp.tileSize, null); // Draw the tile image at the calculated position.
            col++; // Move to the next column.
            x += gp.tileSize; // Increment X position by the tile size.

            // If the current row is fully populated, move to the next row.
            if (col == gp.maxScreenCol) {
                col = 0; // Reset column index.
                x = 0; // Reset X position for the new row.
                row++; // Move to the next row.
                y += gp.tileSize; // Increment Y position by the tile size.
            }
        }
    }
}