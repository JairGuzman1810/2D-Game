package main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.*;

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

        // Place a key at tile coordinates (25, 23)
        gp.obj[0] = new OBJ_Key(gp);
        gp.obj[0].worldX = gp.tileSize * 25;
        gp.obj[0].worldY = gp.tileSize * 23;

        // Place another key at tile coordinates (21, 19)
        gp.obj[1] = new OBJ_CoinBronze(gp);
        gp.obj[1].worldX = gp.tileSize * 21;
        gp.obj[1].worldY = gp.tileSize * 19;

        // Place a third key at tile coordinates (26, 21)
        gp.obj[2] = new OBJ_CoinBronze(gp);
        gp.obj[2].worldX = gp.tileSize * 26;
        gp.obj[2].worldY = gp.tileSize * 21;

        // Place an axe at tile coordinates (33, 21)
        gp.obj[3] = new OBJ_Axe(gp);
        gp.obj[3].worldX = gp.tileSize * 33;
        gp.obj[3].worldY = gp.tileSize * 21;

        // Place a blue shield at tile coordinates (35, 21)
        gp.obj[4] = new OBJ_Shield_Blue(gp);
        gp.obj[4].worldX = gp.tileSize * 35;
        gp.obj[4].worldY = gp.tileSize * 21;

        // Place a red potion at tile coordinates (22, 27)
        gp.obj[5] = new OBJ_Potion_Red(gp);
        gp.obj[5].worldX = gp.tileSize * 22;
        gp.obj[5].worldY = gp.tileSize * 27;

        // Place a heart at tile coordinates (22, 29)
        gp.obj[6] = new OBJ_Heart(gp);
        gp.obj[6].worldX = gp.tileSize * 22;
        gp.obj[6].worldY = gp.tileSize * 29;

        // Place a mana crysta at tile coordinates (22, 31)
        gp.obj[7] = new OBJ_ManaCrystal(gp);
        gp.obj[7].worldX = gp.tileSize * 22;
        gp.obj[7].worldY = gp.tileSize * 31;

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
}
