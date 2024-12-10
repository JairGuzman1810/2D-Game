package data;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.io.*;


// Handles saving and loading game progress to/from a .dat file.
public class SaveLoad {

    GamePanel gp; // Reference to the GamePanel to access player data.

    // Constructor to initialize the SaveLoad class with the current game panel.
    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }


    public Entity getObject(String itemName) {

        switch (itemName) {
            case "Woodcutter's Axe" -> {
                return new OBJ_Axe(gp);
            }
            case "Boots" -> {
                return new OBJ_Boots(gp);
            }
            case "Key" -> {
                return new OBJ_Key(gp);
            }
            case "Lantern" -> {
                return new OBJ_Lantern(gp);
            }
            case "Red Potion" -> {
                return new OBJ_Potion_Red(gp);
            }
            case "Blue Shield" -> {
                return new OBJ_Shield_Blue(gp);
            }
            case "Wood Shield" -> {
                return new OBJ_Shield_Wood(gp);
            }
            case "Tent" -> {
                return new OBJ_Tent(gp);
            }
            case "Door" -> {
                return new OBJ_Door(gp);
            }
            case "Chest" -> {
                return new OBJ_Chest(gp);
            }
            default -> {
                return new OBJ_Sword_Normal(gp);
            }
        }
    }

    // Saves the player's progress to a "save.dat" file.
    public void save() {
        try {
            // Create an ObjectOutputStream to write the object to a file.
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"));

            // Create a new DataStorage object to store the player's stats.
            DataStorage ds = new DataStorage();

            // Copy the player's stats into the DataStorage object.
            ds.level = gp.player.level;             // Player's current level.
            ds.maxLife = gp.player.maxLife;         // Maximum health points of the player.
            ds.life = gp.player.life;               // Current health points of the player.
            ds.maxMana = gp.player.maxMana;         // Maximum mana points of the player.
            ds.mana = gp.player.mana;               // Current mana points of the player.
            ds.strength = gp.player.strength;       // Player's strength, used for attack calculations.
            ds.dexterity = gp.player.dexterity;     // Player's dexterity, used for defense calculations.
            ds.exp = gp.player.exp;                 // Player's current experience points.
            ds.nextLevelExp = gp.player.nextLevelExp; // Experience points required for the next level.
            ds.coin = gp.player.coin;               // Player's current coin count.

            // Player inventory: iterate through each item in the player's inventory.
            for (int i = 0; i < gp.player.inventory.size(); i++) {
                ds.itemNames.add(gp.player.inventory.get(i).name);    // Add item name to the save data.
                ds.itemAmounts.add(gp.player.inventory.get(i).amount); // Add item amount to the save data.
            }

            // Player equipment: save the current weapon and shield slots.
            ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();   // Slot of the currently equipped weapon.
            ds.currentShieldSlot = gp.player.getCurrentShieldSlot();   // Slot of the currently equipped shield.

            // Objects on map: Initialize arrays to track objects' positions, loot, and state.
            ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];  // Object names on each map.
            ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];    // X coordinates of map objects.
            ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];    // Y coordinates of map objects.
            ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length]; // Loot contained in map objects.
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];  // Whether map objects like chests are opened.

            // Iterate through all maps to store map object details.
            for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for (int i = 0; i < gp.obj[1].length; i++) {
                    // If the object is null, mark it as "NA" (Not Available) in the data storage.
                    if (gp.obj[mapNum][i] == null) {
                        ds.mapObjectNames[mapNum][i] = "NA";
                    } else {
                        // Store object details (name, position, loot, and opened status).
                        ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
                        ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;

                        if (gp.obj[mapNum][i].loot != null) {
                            // Store loot name if it exists.
                            ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
                            // Store whether the object has been opened (like a chest).
                            ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].isOpen;
                        }
                    }
                }
            }

            // Write the DataStorage object to the file.
            oos.writeObject(ds); // Serialize and write the data to the file.
        } catch (IOException e) {
            // Handle exceptions that may occur during the saving process.
            System.out.println("Save exception! " + e); // Print an error message if an exception occurs.
        }
    }

    // Loads the player's progress from the "save.dat" file.
    public void load() {
        try {
            // Create an ObjectInputStream to read the object from the file.
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));

            // Read the DataStorage object from the file.
            DataStorage ds = (DataStorage) ois.readObject(); // Deserialize the saved data.

            // Restore the player's stats from the DataStorage object.
            gp.player.level = ds.level;                   // Restore player's current level.
            gp.player.maxLife = ds.maxLife;               // Restore maximum health points.
            gp.player.life = ds.life;                     // Restore current health points.
            gp.player.maxMana = ds.maxMana;               // Restore maximum mana points.
            gp.player.mana = ds.mana;                     // Restore current mana points.
            gp.player.strength = ds.strength;             // Restore player's strength.
            gp.player.dexterity = ds.dexterity;           // Restore player's dexterity.
            gp.player.exp = ds.exp;                       // Restore current experience points.
            gp.player.nextLevelExp = ds.nextLevelExp;     // Restore experience points required for the next level.
            gp.player.coin = ds.coin;                     // Restore current coin count.

            // Player inventory: clear existing inventory and load saved items.
            gp.player.inventory.clear();
            for (int i = 0; i < ds.itemNames.size(); i++) {
                gp.player.inventory.add(getObject(ds.itemNames.get(i)));  // Retrieve and add the saved item by name.
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i); // Set the correct item amount.
            }

            // Player equipment: restore the currently equipped weapon and shield.
            gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);   // Restore the equipped weapon.
            gp.player.currentShield = gp.player.inventory.get(ds.currentShieldSlot);   // Restore the equipped shield.

            // Update stats based on restored equipment and inventory.
            gp.player.getAttack();  // Recalculate and update attack stats.
            gp.player.getDefense(); // Recalculate and update defense stats.

            // Update player attack sprites based on the restored weapon.
            gp.player.getAttackImage();

            // Restore objects on the map and their states.
            for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for (int i = 0; i < gp.obj[1].length; i++) {

                    if (ds.mapObjectNames[mapNum][i].equals("NA")) {
                        gp.obj[mapNum][i] = null; // If the object is marked as "NA", set it to null.
                    } else {
                        // Retrieve and restore map object details.
                        gp.obj[mapNum][i] = getObject(ds.mapObjectNames[mapNum][i]);
                        gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
                        gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];

                        // Restore the loot associated with the object, if any.
                        if (ds.mapObjectLootNames[mapNum][i] != null) {
                            gp.obj[mapNum][i].loot = getObject(ds.mapObjectLootNames[mapNum][i]);
                        }

                        // Restore whether the map object (like a chest) is opened.
                        gp.obj[mapNum][i].isOpen = ds.mapObjectOpened[mapNum][i];
                        if (gp.obj[mapNum][i].isOpen) {
                            // Change chest to open sprite if it's marked as opened.
                            gp.obj[mapNum][i].down1 = gp.obj[mapNum][i].image2;
                        }
                    }
                }
            }

        } catch (Exception e) {
            // Handle exceptions that may occur during loading.
            System.out.println("Load exception! " + e); // Print an error message if an exception occurs.
        }
    }
}