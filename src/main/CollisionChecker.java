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
    public void checkTile(Entity entity) {
        // Calculate the entity's solid area's bounds.
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Determine the entity's current tile position.
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        // Adjust the row or column based on the entity's direction and speed.
        switch (entity.direction) {
            case "up" -> entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
            case "down" -> entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
            case "left" -> entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
            case "right" -> entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
        }

        // Check for collision in the updated tile positions.
        if (isTileCollidable(entityLeftCol, entityTopRow) || isTileCollidable(entityRightCol, entityTopRow)
                || isTileCollidable(entityLeftCol, entityBottomRow) || isTileCollidable(entityRightCol, entityBottomRow)) {
            entity.collisionOn = true;
        }
    }

    // Helper method to check if a tile is collidable.
    private boolean isTileCollidable(int col, int row) {
        int tileNum = gp.tileM.mapTileNum[col][row];
        return gp.tileM.tiles[tileNum].collision;
    }

}
