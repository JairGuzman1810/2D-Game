package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

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
        down1 = setup("/tiles_interactive/drytree", gp.tileSize, gp.tileSize); // Load the image for the dry tree.
        destructible = true; // Mark the tree as destructible.
        life = 3; // The tree has 3 life points before being destroyed.
    }

    @Override
    // Checks if the correct item (an axe) is being used to interact with the tree.
    public boolean isCorrectItem(Entity entity) {
        return entity.currentWeapon.type == type_axe; // Return true if the entity is using an axe.
    }

    @Override
    // Plays a sound effect when the tree is struck by an entity.
    public void playSE() {
        gp.playSE(11); // Play a specific sound effect for the dry tree (ID 11).
    }

    @Override
    // Returns a variant of the tree that represents its destroyed state, an IT_Trunk.
    public InteractiveTile getDestroyedVariant() {
        return new IT_Trunk(gp, worldX / gp.tileSize, worldY / gp.tileSize); // Return the destroyed version of the tree (IT_Trunk).
    }

    // Return the particle color when the tree is interacted with (e.g., chopped).
    public Color getParticleColor() {
        return new Color(65, 50, 30); // A brown color for the dry tree's particles.
    }

    // Return the particle size when the tree is interacted with.
    public int getParticleSize() {
        return 6; // The size of the particles in pixels.
    }

    // Return the particle speed when the tree is interacted with.
    public int getParticleSpeed() {
        return 1; // The speed at which the particles move.
    }

    // Return the maximum lifetime of the particles after the tree is interacted with.
    public int getParticleMaxLife() {
        return 20; // Particles will live for 20 frames before disappearing.
    }
}
