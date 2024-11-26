package object;

import entity.Entity;
import main.GamePanel;

// OBJ_CoinBronze represents a coin bronze object in the game that is used for increasing
// current player coins
public class OBJ_CoinBronze extends Entity {


    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructor that sets the mana's name and loads its image.
    public OBJ_CoinBronze(GamePanel gp) {
        super(gp);
        this.gp = gp;

        // Defines the monetary value that this coin provides.
        value = 1;

        // Define the type of this entity as a pickup only item.
        type = type_pickupOnly;

        // Set the name of the coin object.
        name = "Coin";

        // Load the coin image from resources.
        down1 = setup("/objects/coin_bronze", gp.tileSize, gp.tileSize);
    }

    @Override
    // Overrides the `use` method to apply the red potion effect.
    // Increase the entity's coin counter.
    public boolean use(Entity entity) {

        // Play sound effect associated with pickup the coin.
        gp.playSE(1);

        // Display the amount of coin gained
        gp.ui.addMessage("Coin +" + value);

        // Increase the entity coins counter.
        gp.player.coin += value;

        return true;
    }
}
