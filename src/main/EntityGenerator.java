package main;

import entity.Entity;
import object.*;

// EntityGenerator is a utility class responsible for creating instances of objects in the game based on their unique identifiers (names).
// It uses the provided GamePanel instance to initialize the objects with required game context.
public class EntityGenerator {

    GamePanel gp;

    // Constructor that accepts a GamePanel instance, enabling the generator to create objects with game-related dependencies.
    public EntityGenerator(GamePanel gp) {
        this.gp = gp;
    }

    // Returns an object instance corresponding to the given item name.
    // If no matching item name is found, it defaults to creating a normal sword object.
    public Entity getObject(String itemName) {
        switch (itemName) {
            case OBJ_Axe.objName -> {
                return new OBJ_Axe(gp); // Creates a Woodcutter's Axe object.
            }
            case OBJ_Boots.objName -> {
                return new OBJ_Boots(gp); // Creates a Boots object.
            }
            case OBJ_Chest.objName -> {
                return new OBJ_Chest(gp); // Creates a Chest object.
            }
            case OBJ_CoinBronze.objName -> {
                return new OBJ_CoinBronze(gp); // Creates a Bronze Coin object.
            }
            case OBJ_Door.objName -> {
                return new OBJ_Door(gp); // Creates a Door object.
            }
            case OBJ_Fireball.objName -> {
                return new OBJ_Fireball(gp); // Creates a Fireball object.
            }
            case OBJ_Heart.objName -> {
                return new OBJ_Heart(gp); // Creates a Heart object.
            }
            case OBJ_Key.objName -> {
                return new OBJ_Key(gp); // Creates a Key object.
            }
            case OBJ_Lantern.objName -> {
                return new OBJ_Lantern(gp); // Creates a Lantern object.
            }
            case OBJ_ManaCrystal.objName -> {
                return new OBJ_ManaCrystal(gp); // Creates a Mana Crystal object.
            }
            case OBJ_Potion_Red.objName -> {
                return new OBJ_Potion_Red(gp); // Creates a Red Potion object.
            }
            case OBJ_Rock.objName -> {
                return new OBJ_Rock(gp); // Creates a Rock object.
            }
            case OBJ_Shield_Blue.objName -> {
                return new OBJ_Shield_Blue(gp); // Creates a Blue Shield object.
            }
            case OBJ_Shield_Wood.objName -> {
                return new OBJ_Shield_Wood(gp); // Creates a Wooden Shield object.
            }
            case OBJ_Tent.objName -> {
                return new OBJ_Tent(gp); // Creates a Tent object.
            }
            default -> {
                return new OBJ_Sword_Normal(gp); // Defaults to creating a Normal Sword object.
            }
        }
    }
}
