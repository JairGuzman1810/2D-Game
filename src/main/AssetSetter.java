package main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.OBJ_Axe;
import tile_interactive.IT_DryTree;

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

    // Places various game objects at predefined positions in the game world.
    public void setObject() {

        // Place an axe at tile coordinates (33, 7)
        gp.obj[3] = new OBJ_Axe(gp);
        gp.obj[3].worldX = gp.tileSize * 33;
        gp.obj[3].worldY = gp.tileSize * 7;

    }

    // The setNPC method places various game NPC
    // at specific world coordinates using the GamePanel's tile size.
    public void setNPC() {
        // Set a Old man at coordinates (21, 21) in the world.
        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }

    // The setMonster method places various game monsters
    // at specific world coordinates using the GamePanel's tile size.
    public void setMonster() {
        // Set a Green Slime at coordinates (21, 21) in the world.
        gp.monster[0] = new MON_GreenSlime(gp);
        gp.monster[0].worldX = gp.tileSize * 21;
        gp.monster[0].worldY = gp.tileSize * 36;

        gp.monster[1] = new MON_GreenSlime(gp);
        gp.monster[1].worldX = gp.tileSize * 23;
        gp.monster[1].worldY = gp.tileSize * 37;
    }

    public void setInteractiveTile() {

        // Set a Dry Tree at coordinates (27, 12) in the world.
        gp.iTile[0] = new IT_DryTree(gp, 27, 12);

        // Set a Dry Tree at coordinates (28, 12) in the world.
        gp.iTile[1] = new IT_DryTree(gp, 28, 12);

        // Set a Dry Tree at coordinates (29, 12) in the world.
        gp.iTile[2] = new IT_DryTree(gp, 29, 12);

        // Set a Dry Tree at coordinates (30, 12) in the world.
        gp.iTile[3] = new IT_DryTree(gp, 30, 12);

        // Set a Dry Tree at coordinates (31, 12) in the world.
        gp.iTile[4] = new IT_DryTree(gp, 31, 12);

        // Set a Dry Tree at coordinates (32, 12) in the world.
        gp.iTile[5] = new IT_DryTree(gp, 32, 12);

        // Set a Dry Tree at coordinates (33, 12) in the world.
        gp.iTile[6] = new IT_DryTree(gp, 33, 12);


        // Set a Dry Tree at coordinates (30, 21) in the world.
        gp.iTile[8] = new IT_DryTree(gp, 30, 21);
    }
}
