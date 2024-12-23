package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

// The Entity class serves as a base class for any game entity (like a player, enemy or object).
// It contains the common attributes of an entity, such as position (x, y), speed, and collision settings.
public class Entity {

    // General
    private static final Logger logger = Logger.getLogger(Entity.class.getName()); // Logger for debugging or error messages.
    GamePanel gp; // Reference to the game panel, provides game state and properties.

    // Entity Identification
    public String name; // The name of the entity.
    public int type; // Type identifier used to classify the entity for gameplay interactions.

    // Constants representing different types of entities or items in the game.
    // These help the game identify the entity and apply specific behavior based on its type.
    public final int type_player = 0;       // Identifies the player character.
    public final int type_npc = 1;          // Identifies a non-player character (NPC).
    public final int type_monster = 2;      // Identifies a monster or enemy.
    public final int type_sword = 3;        // Identifies a sword item.
    public final int type_axe = 4;          // Identifies an axe item.
    public final int type_shield = 5;       // Identifies a shield item.
    public final int type_consumable = 6;   // Identifies a consumable item, like potions.
    public final int type_pickupOnly = 7;   // Identifies an item as pickup only, like coins.
    public final int type_obstacle = 8;     // Identifies an obstacle, like a door or chest.
    public final int type_light = 9;        // Identifies a light source, such as a lantern or torch, that can illuminate surroundings.

    // Position and Movement
    public int worldX, worldY;          // Entity's position in the game world.
    public int speed;                   // Movement speed of the entity.
    public int defaultSpeed;            // Stores the original speed of the entity.
    public String direction = "down";   // Current movement direction (up, down, left, right).

    // Animation
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // Movement animation frames.
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2; // Attack animation frames.
    public BufferedImage guardUp, guardDown, guardLeft, guardRight; // Guarding sprites for the player or entity, displayed when in a guarding state (facing up, down, left, or right).
    public int spriteCounter = 0; // Counts frames elapsed for animation.
    public int spriteNum = 1; // Current sprite frame number (1 or 2) for animation.

    // Collision and Solid Area
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // Area around the entity checked for collisions.
    public int solidAreaDefaultX, solidAreaDefaultY; // Default x, y coordinates of the solid area within the entity.
    public boolean collisionOn = false; // Flag to indicate if a collision has occurred.
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);

    // Action Control
    public int actionLockCounter = 0; // Counter to lock entity's action temporarily (e.g., idle/move control).
    public int invincibleCounter = 0; // Tracks duration of invincibility effect.
    public int shotAvailableCounter = 0; // Tracks when player can shoot a projectile again.
    int dyingCounter = 0; // Tracks duration of dying animation.
    int hpBarCounter = 0; // Tracks visibility duration of the HP bar.
    int knockBackCounter = 0; // Counter to track the duration of the knockback effect.
    public int guardCounter = 0; // Tracks the duration the player's shield is active, enabling guarding or parrying.
    int offBalanceCounter = 0; // Tracks the duration the entity remains in a vulnerable state, receiving increased damage.

    // Character Status
    public int maxLife;          // Max life points the entity can have.
    public int life;             // Current life points of the entity.
    public int maxMana;          // Maximum mana points the entity can reach (full mana for projectiles).
    public int mana;             // Current mana points; used for shot projectiles.
    public int ammo;             // Monster current ammo points; used for shot projectiles.
    // Character Level and Stats
    public int level;            // The entity's current level, affecting stats and abilities.
    public int strength;         // Strength attribute, influencing the entity's attack power.
    public int dexterity;        // Dexterity attribute, affecting attack speed or precision.
    public int attack;           // Current attack power of the entity, considering weapon and strength.
    public int defense;          // Defense stat, reducing incoming damage.
    public int exp;              // Experience points accumulated by the entity.
    public int nextLevelExp;     // Experience required to reach the next level.
    public int coin;             // Number of coins the entity currently has (for purchasing items, etc.).
    public ArrayList<Entity> inventory = new ArrayList<>();         // Inventory list that holds items collected by the entity, such as weapons, shields, etc.
    public final int maxInventorySize = 20;                         // Maximum number of items that the entity can carry in the inventory.
    public int motion1_duration; // Duration for the first attack animation frame.
    public int motion2_duration; // Duration for the second attack animation frame.
    // Equipment
    public Entity currentWeapon;        // The entity's currently equipped weapon, affecting attack stats.
    public Entity currentShield;        // The entity's currently equipped shield, affecting defense stats.
    public Entity currentLight;         // The entity's currently equipped light source for the entity, dynamically affecting the lighting system.
    public Projectile projectile;       // The entity's currently equipped projectile,
    // State
    public boolean invincible = false;  // Flag for invincibility to prevent repeated damage.
    public boolean attacking = false;   // Determines if the entity is attacking, triggering attack animations.
    public boolean alive = true;        // Flag indicating if the entity is alive.
    public boolean dying = false;       // Flag indicating if the entity is in the process of dying.
    boolean hpBarOn = false;            // Flag to display the health bar when true.
    public boolean onPath = false;      // Flag indicating if the entity needs to move to a specific location or follow the player.
    public boolean knockBack = false;   // Flag to determine if the entity is currently in a knockback state.
    public String knockBackDirection;   // Direction in which the entity will be knocked back.
    public boolean guarding = false;    // Flag indicating if the entity is in a guarding state.
    public boolean transparent = false; // Flag that handles visual transparency when taking damage.
    public boolean offBalance = false;  // Flag indicating if the entity is in an vulnerable state.
    public boolean isOpen = false;             // Flag that indicates whether the chest is open.

    // Chest
    public Entity loot;                        // The loot contained inside the chest.

    // Item Attributes
    public int attackValue;             // Attack value provided by the current weapon or item.
    public int defenseValue;            // Defense value provided by the current shield or item.
    public String description = "";     // A brief description of the item, which can be displayed in the inventory.
    public int useCost;                 // The resource cost for using this item (mana for projectiles).
    public int value;                   // Value of the item (like in healing or money)
    public int price;                   // The price of the item, used in the trading system to determine its cost for purchasing.
    public int knockBackPower;          // Determines the intensity of the knockback effect.
    public boolean stackable = false;   // Determines if the item can stack in inventory.
    public int amount = 1;              // Quantity of the item in inventory (if stackable).
    public int lightRadius;

    public Entity attacker;             // The entity causing the knockback effect.

    // Dialogue
    public String[][] dialogues = new String[20][20];   // Array to store dialogue text, allowing multiple phrases.
    public int dialogueIndex = 0;                       // Current dialogue index for displaying text.
    public int dialogueSet = 0;                         // Specifies the current set of dialogue for the entity.

    // Object Properties
    public BufferedImage image, image2, image3; // Images representing the object (e.g., key, door).
    public boolean collision = false; // Indicates if the object can trigger collisions.

    // Constructor initializes the Game Panel
    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    // Gets the leftmost X coordinate of the entity's collision area.
    public int getLeftX() {
        return worldX + solidArea.x; // Adds the solid area's offset to the entity's world position.
    }

    // Gets the rightmost X coordinate of the entity's collision area.
    public int getRightX() {
        return getLeftX() + solidArea.height; // Adds the height of the solid area to its left X position.
    }

    // Gets the topmost Y coordinate of the entity's collision area.
    public int getTopY() {
        return worldY + solidArea.y; // Adds the solid area's offset to the entity's world position.
    }

    // Gets the bottommost Y coordinate of the entity's collision area.
    public int getBottomY() {
        return getTopY() + solidArea.width; // Adds the width of the solid area to its top Y position.
    }

    // Calculates the entity's column position on the game grid.
    public int getCol() {
        return (worldX + solidArea.x) / gp.tileSize; // Converts the X position to a grid column.
    }

    // Calculates the entity's row position on the game grid.
    public int getRow() {
        return (worldY + solidArea.y) / gp.tileSize; // Converts the Y position to a grid row.
    }

    // Calculates the absolute horizontal distance (in pixels) between this entity and the target entity.
    public int getXDistance(Entity target) {
        return Math.abs(worldX - target.worldX);
    }

    // Calculates the absolute vertical distance (in pixels) between this entity and the target entity.
    public int getYDistance(Entity target) {
        return Math.abs(worldY - target.worldY);
    }

    // Calculates the tile distance between this entity and the target entity using Manhattan distance.
    // Divides the sum of X and Y distances by 2 to get the distance in terms of tiles.
    public int getTileDistance(Entity target) {
        return (getXDistance(target) + getYDistance(target)) / gp.tileSize;
    }

    // Determines the target's column index in the tile grid by converting its world position.
    // Adds the target's solidArea.x offset to account for its precise location.
    public int getGoalCol(Entity target) {
        return (target.worldX + target.solidArea.x) / gp.tileSize;
    }

    // Determines the target's row index in the tile grid by converting its world position.
    // Adds the target's solidArea.y offset to account for its precise location.
    public int getGoalRow(Entity target) {
        return (target.worldY + target.solidArea.y) / gp.tileSize;
    }

    // Resets all counters used to track the player's or entities' temporary states.
    // This method ensures all timing-related mechanics are initialized to their default state.
    public void resetCounter() {
        actionLockCounter = 0; // Resets the action lock timer.
        invincibleCounter = 0; // Resets the invincibility timer.
        shotAvailableCounter = 0; // Resets the projectile cooldown timer.
        dyingCounter = 0; // Resets the dying animation timer.
        hpBarCounter = 0; // Resets the HP bar visibility timer.
        knockBackCounter = 0; // Resets the knockback effect timer.
        guardCounter = 0; // Resets the guard/parry timer.
        offBalanceCounter = 0; // Resets the off-balance state timer.
    }

    // Defines a method to assign loot to an entity.
    // This method can be overridden by subclasses to customize loot.
    public void setLoot(Entity loot) {

    }

    // Sets the action for the entity, such as determining its direction or behavior.
    // This method can be overridden by subclasses to customize entity behavior.
    public void setAction() {

    }

    // Checks if the player is in range based on direction and distance.
    // Initiates an attack with a chance based on the given rate.
    public void checkIsAttacking(int rate, int straight, int horizontal) {

        // Flag to check if the target is within attack range.
        boolean targetInRange = false;

        // Calculate the horizontal and vertical distances to the player.
        int xDis = getXDistance(gp.player);
        int yDis = getYDistance(gp.player);

        // Determine if the player is within range based on direction.
        switch (direction) {
            case "up" -> {
                // Checks if the player is above and within the straight and horizontal range.
                if (gp.player.worldY < worldY && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
            }
            case "down" -> {
                // Checks if the player is below and within the straight and horizontal range.
                if (gp.player.worldY > worldY && yDis < straight && xDis < horizontal) {
                    targetInRange = true;
                }
            }
            case "left" -> {
                // Checks if the player is to the left and within the straight and horizontal range.
                if (gp.player.worldX < worldX && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
            }
            case "right" -> {
                // Checks if the player is to the right and within the straight and horizontal range.
                if (gp.player.worldX > worldX && xDis < straight && yDis < horizontal) {
                    targetInRange = true;
                }
            }
        }

        // If the player is within range, initiate a chance to attack.
        if (targetInRange) {
            int i = new Random().nextInt(rate); // Random chance based on the attack rate.
            if (i == 0) { // Initiate attack if the random value matches.
                attacking = true;           // Set attacking state to true.
                spriteNum = 1;              // Start attack animation from the first frame.
                spriteCounter = 0;          // Reset sprite counter for animation timing.
                shotAvailableCounter = 0;  // Reset shot availability for ranged attacks.
            }
        }
    }


    // Determines if the entity should fire a projectile based on random chance, firing rate, and interval.
    // Ensures the entity can only fire when the projectile is not already active and the cooldown has expired.
    public void checkIsShooting(int rate, int interval) {
        // Generate a random number to decide if the entity fires a shot
        int i = new Random().nextInt(rate);

        // If conditions are met, initialize the projectile and add it to the game
        if (i > 0 && !projectile.alive && shotAvailableCounter == interval) {
            // Sets the projectile's initial position, direction, and active state
            projectile.set(worldX, worldY, direction, true, this);

            // Add the projectile to the active list for the current map
            for (int j = 0; j < gp.projectile[gp.currentMap].length; j++) {
                if (gp.projectile[gp.currentMap][j] == null) {
                    gp.projectile[gp.currentMap][j] = projectile;
                    break;
                }
            }

            // Reset the shot counter to introduce a delay before the next shot
            shotAvailableCounter = 0;
        }
    }

    // Checks if the entity should start chasing the target based on proximity and random chance.
// Activates the pathfinding behavior when within the specified distance.
    public void checkIsStartChasing(Entity target, int distance, int rate) {
        // If the target is within the specified tile distance
        if (getTileDistance(target) < distance) {
            // Random chance to start chasing
            int i = new Random().nextInt(rate);
            if (i == 0) {
                onPath = true; // Enable pathfinding
            }
        }
    }

    // Checks if the entity should stop chasing the target based on proximity and random chance.
// Deactivates the pathfinding behavior when outside the specified distance.
    public void checkIsStopChasing(Entity target, int distance, int rate) {
        // If the target is beyond the specified tile distance
        if (getTileDistance(target) > distance) {
            // Random chance to stop chasing
            int i = new Random().nextInt(rate);
            if (i == 0) {
                onPath = false; // Disable pathfinding
            }
        }
    }

    // Determines a random direction for the entity's movement at regular intervals.
    public void getRandomDirection() {

        // Increment the counter that tracks how long the entity has been in its current state.
        actionLockCounter++;

        // Every 120 frames, decide on a new direction.
        if (actionLockCounter == 120) {
            // Generate a random number between 1 and 100 to select a direction.
            Random random = new Random();
            int i = random.nextInt(100) + 1; // Values range from 1 to 100.

            // Assign a direction based on random chance:
            // 25% chance for each possible direction: "up", "down", "left", or "right".
            if (i <= 25) {
                direction = "up"; // Move upwards.
            } else if (i <= 50) {
                direction = "down"; // Move downwards.
            } else if (i <= 75) {
                direction = "left"; // Move left.
            } else {
                direction = "right"; // Move right.
            }

            // Reset the counter to repeat this process after another 120 frames.
            actionLockCounter = 0;
        }
    }

    // Defines the reaction of the entity upon receiving damage.
    // Can be overridden in subclasses to provide specific behaviors.
    public void damageReaction() {

    }

    // Method to use a consumable item, like a potion. Can be overridden in subclasses to implement specific effects.
    // The `entity` parameter represents the entity that is being used.
    public boolean use(Entity entity) {

        return false;
    }

    // Method to check if an item should be dropped by the entity.
    // Can be overridden in subclasses to implement specific drop logic.
    public void checkDrop() {

    }

    // Method to drop an item in the game world.
    // Finds the first empty slot in the object array and assigns the dropped item to it.
    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;       // Place the item in the first available slot.
                gp.obj[gp.currentMap][i].worldX = worldX;     // Set item's drop position to entity's worldX.
                gp.obj[gp.currentMap][i].worldY = worldY;     // Set item's drop position to entity's worldY.
                break;
            }
        }
    }

    // Handles the entity speaking by displaying dialogue text to the player
    // and adjusting the entity's direction to face the player.
    public void speak() {

    }


    // Adjusts the entity's direction to face the player during dialogue interactions.
    public void facePlayer() {
        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";
        }
    }

    // Starts a dialogue interaction with the specified entity, switching to dialogue mode.
    // Assigns the NPC to the UI and selects the dialogue set to display.
    public void startDialogue(Entity entity, int setNum) {
        gp.gameState = gp.dialogueState; // Change the game state to dialogue mode.
        gp.ui.npc = entity; // Assign the NPC to the UI for rendering dialogue.
        dialogueSet = setNum; // Set the current dialogue group for the interaction.
    }
    
    // Handles player interactions with objects like doors or chests; override in subclasses.
    public void interact() {
        // Default: No interaction behavior. Subclasses define specific logic.
    }

    // Returns the color of the particles. This method can be overridden in subclasses to specify a custom color for different entities' particles.
    public Color getParticleColor() {
        return null; // Default implementation returns null, indicating no specific color is set.
    }

    // Returns the size of the particles. This method can be overridden in subclasses to specify a custom size for different entities' particles.
    public int getParticleSize() {
        return 0; // Default implementation returns 0, indicating no specific size is set.
    }

    // Returns the speed of the particles. This method can be overridden in subclasses to specify a custom speed for different entities' particles.
    public int getParticleSpeed() {
        return 0; // Default implementation returns 0, indicating no specific speed is set.
    }

    // Returns the maximum lifetime of the particles. This method can be overridden in subclasses to specify a custom lifespan for different entities' particles.
    public int getParticleMaxLife() {
        return 0; // Default implementation returns 0, indicating no specific maximum life is set.
    }

    // Generates particles when an entity interacts with another (e.g., cutting a tree or hitting a monster).
    // This method creates multiple particles with properties determined by the generator entity.
    public void generateParticle(Entity generator, Entity target) {
        // Get the particle properties from the generator entity.
        Color color = generator.getParticleColor(); // The color of the particle.
        int size = generator.getParticleSize();     // The size of the particle.
        int speed = generator.getParticleSpeed();   // The speed of the particle.
        int maxLife = generator.getParticleMaxLife(); // The maximum lifetime of the particle.

        // Create multiple particles with slight variations in their movement direction.
        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1); // Particle with a negative x and y direction.
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);  // Particle with a positive x and negative y direction.
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);  // Particle with a negative x and positive y direction.
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);   // Particle with a positive x and positive y direction.

        // Add the generated particles to the particle list for future updates and rendering.
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }

    // Determines if the entity is colliding with tiles, objects, NPCs, monsters, or the player.
    // It resets the collision state, performs collision checks, and applies effects like damaging the player if applicable.
    public void checkCollision() {

        // Check for tile collision.
        collisionOn = false;                                          // Reset collision state.
        gp.cChecker.checkTile(this);                            // Check if the entity is colliding with any tiles.
        gp.cChecker.checkObject(this, false);             // Check if the entity is colliding with any object.
        gp.cChecker.checkEntity(this, gp.npc);                  // Check if the entity is colliding with any other NPC.
        gp.cChecker.checkEntity(this, gp.monster);              // Check if the entity is colliding with any other monster.
        gp.cChecker.checkEntity(this, gp.iTile);                // Check for collision with interactive tiles.
        boolean contactPlayer = gp.cChecker.checkPlayer(this);  // Check if the entity is colliding with the player, if yes true, no false

        // Checks if the entity is a monster and has contacted the player.
        // If so, reduces player's life and sets them invincible to avoid consecutive damage.
        if (this.type == type_monster && contactPlayer) {
            damagePlayer(attack); // Initiates damage process with the monster's attack value.
        }
    }

    // Updates the entity's state each frame, managing movement, collision detection, animation,
// and effects like knockback, guarding, and invincibility.
    public void update() {

        // Handles knockback state, where the entity is pushed back after being hit.
        if (knockBack) {
            checkCollision(); // Check for collision during knockback movement.

            if (collisionOn) {
                // Stop knockback if the entity collides with an obstacle.
                knockBackCounter = 0; // Reset the knockback duration counter.
                knockBack = false; // End the knockback state.
                speed = defaultSpeed; // Reset the speed to its default value.
            } else {
                // Apply movement in the knockback direction.
                switch (knockBackDirection) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            knockBackCounter++; // Increment knockback duration counter.

            // End knockback effect after 10 frames.
            if (knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed; // Reset speed to normal.
            }

        } else if (attacking) {
            attacking(); // Execute the attack behavior.
        } else {
            // Regular behavior when the entity is not in a special state (e.g., knockback or attack).
            setAction(); // Determine the entity's next action (e.g., AI logic).
            checkCollision(); // Check for collisions before moving.

            // If no collision is detected, move the entity in the current direction.
            if (!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            // Update animation frames for movement.
            spriteCounter++; // Increment the frame counter.

            // Change the animation sprite every 24 frames for smooth movement.
            if (spriteCounter > 24) {
                spriteNum = (spriteNum == 1) ? 2 : 1; // Toggle between sprite 1 and 2.
                spriteCounter = 0; // Reset the frame counter.
            }
        }

        // Handle invincibility state, making the entity immune to damage temporarily.
        if (invincible) {
            invincibleCounter++; // Count frames of invincibility.

            // End invincibility after 40 frames.
            if (invincibleCounter > 40) {
                invincible = false; // Disable invincibility.
                invincibleCounter = 0; // Reset the counter.
            }
        }

        // Manage projectile cooldown to prevent rapid shooting.
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++; // Increment the cooldown counter.
        }

        // Handle vulnerability due to an off-balance state (e.g., after a failed parry).
        if (offBalance) {
            offBalanceCounter++; // Count frames of vulnerability.

            // End the off-balance state after 60 frames.
            if (offBalanceCounter > 60) {
                offBalance = false; // Disable off-balance.
                offBalanceCounter = 0; // Reset the counter.
            }
        }
    }

    // Method to determine the opposite direction of a given direction.
// This is used to check if the player is guarding from the correct direction of an incoming attack.
    public String getOppositeDirection(String direction) {
        switch (direction) {
            case "down" -> {
                return "up"; // If the direction is "down", the opposite is "up".
            }
            case "left" -> {
                return "right"; // If the direction is "left", the opposite is "right".
            }
            case "right" -> {
                return "left"; // If the direction is "right", the opposite is "left".
            }
            default -> {
                return "down"; // Default to "down" if no recognized direction is provided (e.g., "up").
            }
        }
    }

    // Method to control the attack animation and handle collision checks during the attack.
    public void attacking() {
        spriteCounter++; // Increment counter to control the animation frame.

        // Determine the attack phase based on spriteCounter.
        if (spriteCounter <= motion1_duration) {
            spriteNum = 1; // First phase of the attack animation.
        } else if (spriteCounter <= motion2_duration) {
            spriteNum = 2; // Second phase of the attack animation, which includes attack mechanics.

            // Save the current position and solid area dimensions to restore them after attack calculations.
            int currentWorldX = worldX;
            int currentWorldY = worldY;

            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust the entity's position based on the direction of the attack, simulating attack reach.
            switch (direction) {
                case "up" -> worldY -= attackArea.height;
                case "down" -> worldY += attackArea.height;
                case "left" -> worldX -= attackArea.width;
                case "right" -> worldX += attackArea.width;
            }

            // Temporarily change the solid area to match the attack area for collision detection.
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            if (type == type_monster) {
                // If the entity is a monster, check if it collides with the player.
                if (gp.cChecker.checkPlayer(this)) {
                    // Inflict damage to the player if collision is detected.
                    damagePlayer(attack);
                }
            } else { // If the entity is the player.

                // Check for collisions with monsters and apply damage if detected.
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                gp.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

                // Check for collisions with interactive tiles and handle their effects.
                int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
                gp.player.damageInteractiveTile(iTileIndex);

                // Check for collisions with projectiles and handle their effects.
                int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
                gp.player.damageProjectile(projectileIndex);
            }

            // After all collision checks, restore the original position and solid area dimensions.
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        } else {
            // Reset animation and attack state after completing the attack phases.
            spriteNum = 1; // Return to the initial sprite.
            spriteCounter = 0; // Reset the animation counter.
            attacking = false; // Mark the end of the attack.
        }
    }


    // Method to handle damage dealt to the player by a monster or projectile.
    public void damagePlayer(int attack) {

        // Only apply damage if the player is not invincible.
        if (!gp.player.invincible) {
            // Calculate damage based on the monster's attack and the player's defense.
            int damage = attack - gp.player.defense;

            // Determine the direction opposite to the attack for guarding checks.
            String canGuardDirection = getOppositeDirection(direction);

            // Check if the player is guarding and facing the correct direction.
            if (gp.player.guarding && gp.player.direction.equals(canGuardDirection)) {
                // Handle parry: if guard is triggered early (e.g., within 10 frames).
                if (gp.player.guardCounter < 10) {
                    damage = 0; // Negate damage completely.
                    gp.playSE(16); // Play parry sound effect.
                    setKnockBack(this, gp.player, knockBackPower); // Push the monster back.
                    offBalance = true; // Set the monster to an off-balance state.
                    spriteCounter = -60; // Trigger a special animation for the parry.
                } else {
                    // Handle regular guard: reduce damage by 1/3.
                    damage /= 3;
                    gp.playSE(15); // Play guard sound effect.
                }
            } else {
                // Handle unguarded or improperly guarded hits.
                gp.playSE(6); // Play damage sound effect.
                damage = Math.max(damage, 1); // Ensure a minimum of 1 damage is dealt.
            }

            // If damage is dealt, apply visual and knockback effects.
            if (damage != 0) {
                gp.player.transparent = true; // Make the player visually transparent as feedback.
                setKnockBack(gp.player, this, knockBackPower); // Push the player back.
            }

            // Subtract the damage from the player's life.
            gp.player.life -= damage;

            // Make the player invincible to prevent consecutive hits.
            gp.player.invincible = true;
        }
    }

    // Applies a knockback effect to the target entity, pushing it in the direction of the attacker.
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {
        this.attacker = attacker;                 // Set the attacker entity.
        target.knockBackDirection = attacker.direction; // Set knockback direction based on attacker.
        target.speed += knockBackPower;          // Increase target's speed temporarily.
        target.knockBack = true;                 // Activate knockback effect on target.
    }

    // Draws the entity on the screen relative to the player's position.
    public void draw(Graphics2D g2) {

        BufferedImage image;
        // Calculate the object's position on the screen based on the player's position.
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;


        // Check if the entity is within the player's visible area.
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            // Adjust tempScreenX and tempScreenY based on direction to position the attack animation properly.
            int tempScreenX = screenX;
            int tempScreenY = screenY;

            // Adjust Y-coordinate when direction is up or down
            if (attacking) {
                switch (direction) {
                    case "up" -> tempScreenY = screenY - gp.tileSize;
                    case "left" -> tempScreenX = screenX - gp.tileSize;
                }
            }

            // Choose the correct animation frame based on direction and attack status.
            image = switch (direction) {
                case "up" -> attacking ? (spriteNum == 1 ? attackUp1 : attackUp2) : (spriteNum == 1 ? up1 : up2);
                case "down" ->
                        attacking ? (spriteNum == 1 ? attackDown1 : attackDown2) : (spriteNum == 1 ? down1 : down2);
                case "left" ->
                        attacking ? (spriteNum == 1 ? attackLeft1 : attackLeft2) : (spriteNum == 1 ? left1 : left2);
                case "right" ->
                        attacking ? (spriteNum == 1 ? attackRight1 : attackRight2) : (spriteNum == 1 ? right1 : right2);
                default -> down1; // Default to down1 if direction is unrecognized.
            };

            // Monster HP bar
            if (type == 2 && hpBarOn) { // Check if the entity is a monster and the HP bar should be displayed.
                // Calculate the width of one life point in pixels based on the monster's max life.
                double oneScale = (double) gp.tileSize / maxLife;
                // Calculate the current HP bar width according to the monster's current life.
                double hpBarValue = oneScale * life;

                // Draw the HP bar background slightly larger than the bar itself for visual clarity.
                g2.setColor(new Color(35, 35, 35)); // Set background color to dark gray.
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12); // Draw background rectangle.

                // Draw the HP bar itself, scaled to the monster's current life.
                g2.setColor(new Color(255, 0, 30)); // Set HP bar color to red.
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10); // Draw HP bar rectangle.

                // Increment the HP bar counter to track how long the HP bar has been visible.
                hpBarCounter++;

                // Hide the HP bar after a certain period (600 frames).
                if (hpBarCounter > 600) {
                    hpBarCounter = 0; // Reset the counter for future visibility.
                    hpBarOn = false;  // Disable the HP bar until it’s needed again.
                }
            }

            // Apply invincibility blinking effect
            if (invincible) {
                // Turn on the HP bar to indicate invincibility state visually
                hpBarOn = true;

                // Reduce the alpha (transparency) of the graphics to 0.4f for a blinking effect
                // This makes the entity semi-transparent to signify invincibility
                changeAlpha(g2, 0.4f);
            }

            // Check if the entity is in a dying state
            if (dying) {
                // Call the dying animation to handle visual effects during the death sequence
                dyingAnimation(g2);
            }

            // Draw the entity's image on the screen at the calculated position.
            g2.drawImage(image, tempScreenX, tempScreenY, null);

            // Reset alpha to 1f
            changeAlpha(g2, 1f);
        }
    }

    // Sets the transparency level for the Graphics2D object.
    private static void changeAlpha(Graphics2D g2, float alpha) {
        // Set the composite for the Graphics2D object to control transparency.
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    // Controls the dying animation, increasing blink speed as the entity approaches "death."
    public void dyingAnimation(Graphics2D g2) {

        // Increment the counter to track the duration of the dying animation.
        dyingCounter++;

        // Calculate the blink interval, decreasing as dyingCounter approaches 40.
        // Blink interval starts at 10 frames and decreases by 1 frame per 4 dyingCounter units.
        int blinkInterval = Math.max(2, 10 - (dyingCounter / 4));

        // Set alpha to 0.4 or 1.0, toggling every 'blinkInterval' frames for a blinking effect.
        float alpha = (dyingCounter / blinkInterval % 2 == 0) ? 0.4f : 1.0f;

        // Apply the calculated alpha transparency to the graphics object.
        changeAlpha(g2, alpha);

        // End the animation after 40 frames, resetting relevant flags and counter.
        if (dyingCounter > 40) {
            alive = false; // Set alive status to false, indicating the entity is "dead."
            dyingCounter = 0; // Reset dyingCounter for future animations if needed.
        }
    }


    // Helper method to load an image by name, scale it, and return the BufferedImage
    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool(); // Create an instance of UtilityTool for image scaling
        BufferedImage image = null; // Initialize the BufferedImage variable

        try {
            // Load the image from resources and scale it to the size of the game tiles
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, width, height); // Scale the loaded image
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load image", e); // Log error if image loading fails
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Image not found!", e); // Log warning if image resource is not found
        }

        return image; // Return the scaled image
    }

    // The searchPath method calculates the path to a specified goal location using a pathfinding algorithm.
    // It sets the next movement direction for the entity based on its position and collision checks.
    // If the entity reaches the goal, it stops following the path.
    public void searchPath(int goalCol, int goalRow) {

        // Calculate the entity's starting column and row based on its world position and solid area.
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        // Set up the start and goal nodes for the pathfinding algorithm.
        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        // If a path is found, determine the next step towards the goal.
        if (gp.pFinder.search()) {
            // Get the next target position in the path.
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            // Calculate the entity's current solid area position.
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            // Determine the direction based on the next position in the path.
            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                // Move left or right based on horizontal position.
                if (enLeftX > nextX) {
                    direction = "left";
                }
                if (enLeftX < nextX) {
                    direction = "right";
                }
            } else if (enTopY > nextY && enLeftX > nextX) {
                // Move up or left, with collision checks.
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY > nextY && enLeftX < nextX) {
                // Move up or right, with collision checks.
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            } else if (enTopY < nextY && enLeftX > nextX) {
                // Move down or left, with collision checks.
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY < nextY && enLeftX < nextX) {
                // Move down or right, with collision checks.
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            // Check if the entity has reached the goal.
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;
            if (nextCol == goalCol && nextRow == goalRow) {
                onPath = false; // Stop following the path.
            }
        }
    }

    // Detects nearby objects matching a target name in the direction the user is facing.
    public int getDetected(Entity user, Entity[][] target, String targetName) {
        int index = 999; // Default value indicating no target found.

        // Determine the position of the next tile in the user's movement direction.
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();
        switch (user.direction) {
            case "up" -> nextWorldY -= user.speed;
            case "down" -> nextWorldY = user.getBottomY() + user.speed;
            case "left" -> nextWorldX -= user.speed;
            case "right" -> nextWorldX = user.getRightX() + user.speed;
        }

        // Calculate the grid coordinates of the tile being checked.
        int col = nextWorldX / gp.tileSize;
        int row = nextWorldY / gp.tileSize;

        // Iterate through the target entities in the current map to find a match.
        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null) {
                // Check if the target's position and name match the specified conditions.
                if (target[gp.currentMap][i].getCol() == col &&
                        target[gp.currentMap][i].getRow() == row &&
                        target[gp.currentMap][i].name.equals(targetName)) {
                    index = i; // Set the index of the detected target.
                    break;
                }
            }
        }
        return index; // Return the index or 999 if no entity is detected.
    }
}
