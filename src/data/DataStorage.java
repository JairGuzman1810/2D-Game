package data;

import java.io.Serializable;
import java.util.ArrayList;

// The DataStorage class stores the player's stats and inventory data, implementing Serializable
// to allow saving and loading of the data into a file for persistent gameplay.
// It holds important information such as the player's level, health, mana, strength, dexterity, experience,
// coin count, and the player's inventory (items and their quantities). Additionally, it tracks the
// currently equipped weapon and shield slots by storing their indices.
public class DataStorage implements Serializable {
    // Player stats
    int level;          // Player's current level.
    int maxLife;        // Maximum health points of the player.
    int life;           // Current health points of the player.
    int maxMana;        // Maximum mana points of the player.
    int mana;           // Current mana points of the player.
    int strength;       // Player's strength, used for attack calculations.
    int dexterity;      // Player's dexterity, used for defense calculations.
    int exp;            // Player's current experience points.
    int nextLevelExp;   // Experience points required for the next level.
    int coin;           // Player's current coin count.

    // Player inventory
    ArrayList<String> itemNames = new ArrayList<>();  // Names of items in the inventory.
    ArrayList<Integer> itemAmounts = new ArrayList<>(); // Quantities of the respective items.
    int currentWeaponSlot; // Index of the current weapon in the inventory.
    int currentShieldSlot; // Index of the current shield in the inventory.

    // Objects on map
    String[][] mapObjectNames;        // Names of objects placed in the map.
    int[][] mapObjectWorldX;          // X coordinates of map objects.
    int[][] mapObjectWorldY;          // Y coordinates of map objects.
    String[][] mapObjectLootNames;    // Names of loot contained in map objects (e.g., chests).
    boolean[][] mapObjectOpened;      // Whether a map object (like a chest) has been opened or not.
}
