package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_CoinBronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

// MON_Orc represents an orc monster entity within the game, inheriting
// properties and behaviors from the Entity class. This monster type
// has unique attributes such as attack power, defense, and animations.
public class MON_Orc extends Entity {

    // Reference to the GamePanel, which holds game-related settings and properties.
    GamePanel gp;

    // Constructs the orc with specific attributes and initializes
    // its image and solid area for collision detection.
    public MON_Orc(GamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_monster;                    // Sets the type to 'monster' for identification.
        name = "Orc";                           // Assigns the name of this monster type.
        defaultSpeed = 1;                       // Default movement speed of the orc.
        speed = defaultSpeed;                   // Current speed, initially set to defaultSpeed.
        maxLife = 10;                           // Maximum life points of the orc.
        life = maxLife;                         // Initial life points set to maximum.
        attack = 8;                             // Attack power of the orc.
        defense = 2;                            // Defense level of the orc.
        exp = 10;                               // Experience points granted when defeated.
        projectile = new OBJ_Rock(gp);          // Projectile the orc uses for ranged attacks.

        // Defines the solid area for collision detection.
        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = 40;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Attack area dimensions for melee attacks.
        attackArea.width = 48;
        attackArea.height = 48;

        // Initializes images for movement and attack animations.
        getImage();
        getAttackImage();
    }

    // Loads the orc's movement images for animation.
    public void getImage() {
        up1 = setup("/monster/orc_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/orc_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/orc_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/orc_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/orc_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/orc_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/orc_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/orc_right_2", gp.tileSize, gp.tileSize);
    }

    // Loads the orc's attack images for different directions.
    public void getAttackImage() {
        attackUp1 = setup("/monster/orc_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("/monster/orc_attack_up_2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("/monster/orc_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("/monster/orc_attack_down_2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("/monster/orc_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/monster/orc_attack_left_2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("/monster/orc_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/monster/orc_attack_right_2", gp.tileSize * 2, gp.tileSize);
    }

    @Override
    // Sets the orc's behavior, alternating between chasing the player
    // or moving randomly depending on its current state.
    public void setAction() {
        if (onPath) {
            // Stops chasing if the player is more than 15 tiles away.
            checkIsStopChasing(gp.player, 15, 100);

            // Finds and follows a path to the player's position.
            searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
        } else {
            // Starts chasing if the player is within 5 tiles.
            checkIsStartChasing(gp.player, 5, 100);

            // Moves in a random direction when not chasing.
            getRandomDirection();
        }
    }

    @Override
    // Handles the orc's reaction to taking damage. Resets its action lock counter
    // and sets it to aggro mode to immediately pursue the player.
    public void damageReaction() {
        super.damageReaction();

        actionLockCounter = 0; // Resets counter for immediate action.
        onPath = true;         // Switches to aggro mode.
    }

    @Override
    // Determines the item dropped by the orc when defeated. The drop is based
    // on a random chance between a bronze coin, a heart, or a mana crystal.
    public void checkDrop() {
        super.checkDrop();

        int i = new Random().nextInt(100) + 1; // Random number between 1 and 100.

        if (i <= 50) {
            dropItem(new OBJ_CoinBronze(gp)); // 50% chance for a bronze coin.
        } else if (i <= 75) {
            dropItem(new OBJ_Heart(gp)); // 25% chance for a heart.
        } else {
            dropItem(new OBJ_ManaCrystal(gp)); // 25% chance for a mana crystal.
        }
    }
}