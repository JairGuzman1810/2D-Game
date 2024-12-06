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

            // Write the DataStorage object to the file.
            oos.writeObject(ds); // Serialize and write the data to the file.
            oos.close();         // Close the stream after writing to release resources.
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
            ois.close(); // Close the stream after reading to release resources.

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

        } catch (Exception e) {
            // Handle exceptions that may occur during loading.
            System.out.println("Load exception! " + e); // Print an error message if an exception occurs.
        }
    }
}