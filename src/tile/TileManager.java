package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    // List of file names for the tile images.
    ArrayList<String> fileNames = new ArrayList<>();
    // List of collision statuses corresponding to the tiles.
    ArrayList<String> collisionStatus = new ArrayList<>();

    // Constructor that initializes the TileManager with a reference to GamePanel.
    // It also prepares the tile images and loads the map layout.
    public TileManager(GamePanel gp) {
        this.gp = gp; // Assign the GamePanel reference to this TileManager instance.

        // Load tile data file containing tile names and collision statuses.
        InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line; // Temporary variable for reading file lines.

        try {
            // Read tile names and collision statuses into their respective lists.
            while ((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e); // Handle errors by throwing a runtime exception.
        }

        // Initialize the tile array based on the number of file names.
        tiles = new Tile[fileNames.size()];
        getTileImage(); // Load images for all tiles.

        // Load world map dimensions from a text file.
        is = getClass().getResourceAsStream("/maps/worldmap.txt");
        assert is != null;
        br = new BufferedReader(new InputStreamReader(is));

        try {
            String lineMap = br.readLine(); // Read the first line containing tile dimensions.
            String[] maxTile = lineMap.split(" ");

            // Set maximum columns and rows for the game world.
            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;

            // Initialize the map array with the dimensions and map layers.
            mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e); // Handle errors during map dimension loading.
        }

        // Load different maps into the map array.
        loadMap("/maps/worldmap.txt", 0); // Load the main map layout.
        loadMap("/maps/indoor01.txt", 1); // Load an additional map layout.
    }

    // Loads tile images and sets their collision properties based on data.
    public void getTileImage() {
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i); // Get tile image file name.
            boolean collision = collisionStatus.get(i).equals("true"); // Determine collision status.

            setup(i, fileName, collision); // Initialize the tile with its properties.
        }
    }


    // The setup method initializes a tile at a specific index and loads its image.
    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool(); // Create an instance of UtilityTool for image scaling.

        try {
            // Initialize the tile object at the specified index.
            tiles[index] = new Tile();
            // Load the image from the resources and scale it to the desired size.
            tiles[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/" + imageName)));
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
