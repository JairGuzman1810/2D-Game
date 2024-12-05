package data;

import java.io.Serializable;


// Stores the player's stats and implements Serializable
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
}
