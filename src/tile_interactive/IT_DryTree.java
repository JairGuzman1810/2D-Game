package tile_interactive;

import entity.Entity;
import main.GamePanel;

// IT_DryTree represents a dry tree in the game that can be destroyed by the player
// and may drop an item or change its appearance when destroyed.
public class IT_DryTree extends InteractiveTile {

    // Reference to the GamePanel, which manages game settings and properties.
    GamePanel gp;

    // Constructor to create a dry tree at a specific grid position.
    public IT_DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row); // Initialize as an InteractiveTile.
        this.gp = gp;

        // Set the tree's world position based on tile size and grid coordinates.
        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        // Load the tree's image and set destructibility and initial life.
        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
        destructible = true;
        life = 3;
    }

    @Override
    // Checks if the correct item (an axe) is being used to interact with the tree.
    public boolean isCorrectItem(Entity entity) {
        return entity.currentWeapon.type == type_axe;
    }

    @Override
    // Plays a sound effect when the tree is struck by an entity.
    public void playSE() {
        gp.playSE(11); // Play a specific sound effect for the dry tree.
    }

    @Override
    // Returns a variant of the tree that represents its destroyed state, an IT_Trunk.
    public InteractiveTile getDestroyedVariant() {
        return new IT_Trunk(gp, worldX / gp.tileSize, worldY / gp.tileSize);
    }
}
