package main;

import entity.NPC_OldMan;

// AssetSetter is responsible for placing objects (like keys, doors, chests)
// at predefined positions in the game world.
public class AssetSetter {

    // Reference to the GamePanel, which holds the game state and properties.
    GamePanel gp;

    // Constructor that initializes the AssetSetter with a reference to GamePanel.
    // It allows for object placement in the game world.
    public AssetSetter(GamePanel gp) {
        this.gp = gp; // Store the reference to the GamePanel for object placement.
    }

    // The setObject method places various game objects
    // at specific world coordinates using the GamePanel's tile size.
    public void setObject() {

    }

    // The setNPC method places various game NPC
    // at specific world coordinates using the GamePanel's tile size.
    public void setNPC() {
        // Set a Old man at coordinates (21, 21) in the world.
        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }
}
