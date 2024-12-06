package data;

import java.io.Serializable;
import java.util.ArrayList;


// Stores the player's stats and inventory data and implements Serializable
// to allow saving and loading the data into a file.
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
}
