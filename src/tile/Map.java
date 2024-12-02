package tile;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

// The Map class is responsible for managing and rendering the full game map and minimap.
public class Map extends TileManager {

    // Reference to the GamePanel, providing access to game-related configurations and data.
    GamePanel gp;

    // Stores the map image for each level in the game.
    BufferedImage[] worldMap;

    // Determines whether the minimap is currently visible.
    public boolean miniMapOn = false;

    // Constructor initializes the Map object and generates the world map images.
    public Map(GamePanel gp) {
        super(gp);
        this.gp = gp;
        createWordMap();
    }

    // Creates a full world map for each level, rendering the tiles into an image.
    public void createWordMap() {
        worldMap = new BufferedImage[gp.maxMap];
        int wordMapWidth = gp.tileSize * gp.maxWorldCol;
        int wordMapHeight = gp.tileSize * gp.maxWorldRow;

        for (int i = 0; i < gp.maxMap; i++) {
            // Create a blank image for the map and get its graphics context.
            worldMap[i] = new BufferedImage(wordMapWidth, wordMapHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = worldMap[i].createGraphics();

            int col = 0;
            int row = 0;

            // Loop through all tiles in the map and draw them onto the image.
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                int tileNum = mapTileNum[i][col][row];
                int x = gp.tileSize * col;
                int y = gp.tileSize * row;
                g2.drawImage(tiles[tileNum].image, x, y, null);
                col++;

                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            // Release resources.
            g2.dispose();
        }
    }

    // Draws the full map on the screen in a central position, including the player's location.
    public void drawFullMapScreen(Graphics2D g2) {
        // Fill the background with black.
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Draw the current map level in the center of the screen.
        int width = 500;
        int height = 500;
        int x = gp.screenWidth / 2 - width / 2;
        int y = gp.screenHeight / 2 - height / 2;
        g2.drawImage(worldMap[gp.currentMap], x, y, width, height, null);

        // Calculate and draw the player's position on the map.
        double scale = (double) (gp.tileSize * gp.maxWorldCol) / width;
        int playerX = (int) (x + gp.player.worldX / scale);
        int playerY = (int) (y + gp.player.worldY / scale);
        int playerSize = (int) (gp.tileSize / scale);

        BufferedImage player;

        // Determine which image to use based on the player's direction.
        switch (gp.player.direction) {
            case "down" -> player = gp.player.down1;
            case "up" -> player = gp.player.up1;
            case "left" -> player = gp.player.left1;
            default -> player = gp.player.right1;
        }

        g2.drawImage(player, playerX, playerY, playerSize, playerSize, null);

        // Display a hint message for closing the map.
        g2.setFont(gp.ui.pixelOperator.deriveFont(32f));
        g2.setColor(Color.white);
        g2.drawString("Press M to close", 750, 550);
    }

    // Draws a minimap in the corner of the screen, showing the player's location.
    public void drawMiniMap(Graphics2D g2) {
        if (miniMapOn) {
            // Set the minimap dimensions and position.
            int width = 200;
            int height = 200;
            int x = gp.screenWidth - width - 50;
            int y = 50;

            // Make the minimap slightly transparent.
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.drawImage(worldMap[gp.currentMap], x, y, width, height, null);

            // Calculate and draw the player's position on the minimap.
            double scale = (double) (gp.tileSize * gp.maxWorldCol) / width;
            int playerX = (int) (x + gp.player.worldX / scale);
            int playerY = (int) (y + gp.player.worldY / scale);
            int playerSize = gp.tileSize / 4;

            BufferedImage player;

            // Determine which image to use based on the player's direction.
            switch (gp.player.direction) {
                case "down" -> player = gp.player.down1;
                case "up" -> player = gp.player.up1;
                case "left" -> player = gp.player.left1;
                default -> player = gp.player.right1;
            }

            // Draw the player's icon slightly offset to align better with the map.
            g2.drawImage(player, playerX - 6, playerY - 6, playerSize, playerSize, null);

            // Reset the transparency.
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }
}