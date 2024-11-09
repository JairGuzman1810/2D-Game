package tile_interactive;

import main.GamePanel;

// IT_Trunk represents the trunk left behind after a dry tree is destroyed,
// serving as a non-collidable decoration in the game world.
public class IT_Trunk extends InteractiveTile {

    // Reference to the GamePanel, which manages game settings and properties.
    GamePanel gp;

    // Constructor to create a trunk at a specific grid position.
    public IT_Trunk(GamePanel gp, int col, int row) {
        super(gp, col, row); // Initialize as an InteractiveTile.
        this.gp = gp;

        // Set the trunk's world position based on tile size and grid coordinates.
        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        // Load the trunk's image.
        down1 = setup("/tiles_interactive/trunk", gp.tileSize, gp.tileSize);

        // Remove collision by setting solid area dimensions to zero.
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x; // Store default X position.
        solidAreaDefaultY = solidArea.y; // Store default Y position.
    }
}
