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
            entity.collisionOn = true; // Mark entity as collided if any of the relevant tiles are collidable.
        }
    }

    // Helper method to check if a tile is collidable based on its column and row position in the tile map.
    // Returns true if the tile has collision properties, meaning it's a solid object.
    private boolean isTileCollidable(int col, int row) {
        try {
            int tileNum = gp.tileM.mapTileNum[col][row]; // Get the tile number from the tile map
            return gp.tileM.tiles[tileNum].collision; // Return whether the tile has collision properties
        } catch (ArrayIndexOutOfBoundsException e) {
            return true; // Return true if out of bounds
        }
    }


    // Method to check if an entity has collided with any in-game object.
    // It returns the index of the object the entity collides with, or 999 if no collision occurred.
    public int checkObject(Entity entity, boolean player) {
        int index = 999; // Initialize index to 999, indicating no collision by default.

        // Loop through all objects in the game to check for potential collisions.
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                // Get the object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                // Check collision based on the entity's movement direction.
                switch (entity.direction) {
                    case "up" -> {
                        entity.solidArea.y -= entity.speed; // Move entity's solid area up based on speed.
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) { // Check for collision.
                            if (gp.obj[i].collision) { // If the object has collision properties, mark entity as collided.
                                entity.collisionOn = true;
                            }
                            if (player) { // If the entity is the player, return the object's index.
                                index = i;
                            }
                        }
                    }
                    case "down" -> {
                        entity.solidArea.y += entity.speed; // Move entity's solid area down based on speed.
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) { // Check for collision.
                            if (gp.obj[i].collision) { // If the object has collision properties, mark entity as collided.
                                entity.collisionOn = true;
                            }
                            if (player) { // If the entity is the player, return the object's index.
                                index = i;
                            }
                        }
                    }
                    case "left" -> {
                        entity.solidArea.x -= entity.speed; // Move entity's solid area left based on speed.
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) { // Check for collision.
                            if (gp.obj[i].collision) { // If the object has collision properties, mark entity as collided.
                                entity.collisionOn = true;
                            }
                            if (player) { // If the entity is the player, return the object's index.
                                index = i;
                            }
                        }
                    }
                    case "right" -> {
                        entity.solidArea.x += entity.speed; // Move entity's solid area right based on speed.
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) { // Check for collision.
                            if (gp.obj[i].collision) { // If the object has collision properties, mark entity as collided.
                                entity.collisionOn = true;
                            }
                            if (player) { // If the entity is the player, return the object's index.
                                index = i;
                            }
                        }
                    }
                }

                // Reset the solid areas back to their default positions after checking for collisions.
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index; // Return the index of the object collided with, or 999 if no collision.
    }
}
