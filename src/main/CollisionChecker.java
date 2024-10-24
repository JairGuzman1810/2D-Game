package main;

import entity.Entity;

// The CollisionChecker class is responsible for detecting if an entity has collided with a solid object (such as a wall or obstacle).
public class CollisionChecker {

    // Reference to the GamePanel, which provides the game environment (including the map and entities).
    GamePanel gp;

    // Constructor that initializes the CollisionChecker with the GamePanel.
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // Method to check if an entity has collided with a tile on the map.
    // Takes an entity as a parameter and updates its collision status if necessary.
    public void checkTile(Entity entity) {
        // Calculate the X coordinates of the entity's solid area's left and right edges.
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;

        // Calculate the Y coordinates of the entity's solid area's top and bottom edges.
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Convert the world coordinates to column and row numbers on the map grid.
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        // Variables to hold the tile numbers for collision checks.
        int tileNum1, tileNum2;

        // Check the entity's current direction to determine which tiles to check for collision.
        switch (entity.direction) {
            case "up" -> {
                // Check the tiles above the entity by adjusting the top Y-coordinate.
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                // If either of the tiles has a collision flag, set the entity's collision status to true.
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case "down" -> {
                // Check the tiles below the entity by adjusting the bottom Y-coordinate.
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                // If either of the tiles has a collision flag, set the entity's collision status to true.
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case "left" -> {
                // Check the tiles to the left of the entity by adjusting the left X-coordinate.
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                // If either of the tiles has a collision flag, set the entity's collision status to true.
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            case "right" -> {
                // Check the tiles to the right of the entity by adjusting the right X-coordinate.
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                // If either of the tiles has a collision flag, set the entity's collision status to true.
                if (gp.tileM.tiles[tileNum1].collision || gp.tileM.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
        }
    }
}
