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

        int mapNum = 0;

        // Place an axe at tile coordinates (33, 7)
        gp.obj[mapNum][3] = new OBJ_Axe(gp);
        gp.obj[mapNum][3].worldX = gp.tileSize * 33;
        gp.obj[mapNum][3].worldY = gp.tileSize * 7;

    }

    // The setNPC method places various game NPC
    // at specific world coordinates using the GamePanel's tile size.
    public void setNPC() {
        int mapNum = 0;

        // Set a Old man at coordinates (21, 21) in the world.
        gp.npc[mapNum][0] = new NPC_OldMan(gp);
        gp.npc[mapNum][0].worldX = gp.tileSize * 21;
        gp.npc[mapNum][0].worldY = gp.tileSize * 21;
    }

    // The setMonster method places various game monsters
    // at specific world coordinates using the GamePanel's tile size.
    public void setMonster() {
        int mapNum = 0;

        // Set a Green Slime at coordinates (21, 21) in the world.
        gp.monster[mapNum][0] = new MON_GreenSlime(gp);
        gp.monster[mapNum][0].worldX = gp.tileSize * 21;
        gp.monster[mapNum][0].worldY = gp.tileSize * 36;

        gp.monster[mapNum][1] = new MON_GreenSlime(gp);
        gp.monster[mapNum][1].worldX = gp.tileSize * 23;
        gp.monster[mapNum][1].worldY = gp.tileSize * 37;
    }

    public void setInteractiveTile() {

        int mapNum = 0;


        // Set a Dry Tree at coordinates (27, 12) in the world.
        gp.iTile[mapNum][0] = new IT_DryTree(gp, 27, 12);

        // Set a Dry Tree at coordinates (28, 12) in the world.
        gp.iTile[mapNum][1] = new IT_DryTree(gp, 28, 12);

        // Set a Dry Tree at coordinates (29, 12) in the world.
        gp.iTile[mapNum][2] = new IT_DryTree(gp, 29, 12);

        // Set a Dry Tree at coordinates (30, 12) in the world.
        gp.iTile[mapNum][3] = new IT_DryTree(gp, 30, 12);

        // Set a Dry Tree at coordinates (31, 12) in the world.
        gp.iTile[mapNum][4] = new IT_DryTree(gp, 31, 12);

        // Set a Dry Tree at coordinates (32, 12) in the world.
        gp.iTile[mapNum][5] = new IT_DryTree(gp, 32, 12);

        // Set a Dry Tree at coordinates (33, 12) in the world.
        gp.iTile[mapNum][6] = new IT_DryTree(gp, 33, 12);

        // Set a Dry Tree at coordinates (30, 21) in the world.
        gp.iTile[mapNum][8] = new IT_DryTree(gp, 30, 21);

        // Set a Dry Tree at coordinates (18, 40)) in the world.
        gp.iTile[mapNum][9] = new IT_DryTree(gp, 18, 40);

        // Set a Dry Tree at coordinates (17, 40s) in the world.
        gp.iTile[mapNum][10] = new IT_DryTree(gp, 17, 40);

        // Set a Dry Tree at coordinates (16, 40) in the world.
        gp.iTile[mapNum][11] = new IT_DryTree(gp, 16, 40);

        // Set a Dry Tree at coordinates (15, 40) in the world.
        gp.iTile[mapNum][12] = new IT_DryTree(gp, 15, 40);

        // Set a Dry Tree at coordinates (14, 40) in the world.
        gp.iTile[mapNum][13] = new IT_DryTree(gp, 14, 40);

        // Set a Dry Tree at coordinates (13, 40) in the world.
        gp.iTile[mapNum][14] = new IT_DryTree(gp, 13, 40);

        // Set a Dry Tree at coordinates (13, 41) in the world.
        gp.iTile[mapNum][15] = new IT_DryTree(gp, 13, 41);

        // Set a Dry Tree at coordinates (12, 41) in the world.
        gp.iTile[mapNum][16] = new IT_DryTree(gp, 12, 41);

        // Set a Dry Tree at coordinates (11, 41) in the world.
        gp.iTile[mapNum][17] = new IT_DryTree(gp, 11, 41);

        // Set a Dry Tree at coordinates (10, 41) in the world.
        gp.iTile[mapNum][18] = new IT_DryTree(gp, 10, 41);


    }
}
