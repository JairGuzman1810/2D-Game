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
            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;

            // Player inventory
            for (int i = 0; i < gp.player.inventory.size(); i++) {
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }

            // Write the DataStorage object to the file.
            oos.writeObject(ds);
            oos.close(); // Close the stream after writing to release resources.
        } catch (IOException e) {
            // Handle exceptions that may occur during saving.
            System.out.println("Save exception! " + e);
        }
    }

    //Loads the player's progress from the "save.dat" file.
    public void load() {
        try {
            // Create an ObjectInputStream to read the object from the file.
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"));

            // Read the DataStorage object from the file.
            DataStorage ds = (DataStorage) ois.readObject();
            ois.close(); // Close the stream after reading to release resources.

            // Restore the player's stats from the DataStorage object.
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;

            // Player inventory
            gp.player.inventory.clear();
            for (int i = 0; i < ds.itemNames.size(); i++) {

                gp.player.inventory.add(getObject(ds.itemNames.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);

            }

        } catch (Exception e) {
            // Handle exceptions that may occur during loading.
            System.out.println("Load exception! " + e);
        }
    }
}