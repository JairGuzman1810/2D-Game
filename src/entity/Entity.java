package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
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
    public final int type_player = 0; // Identifies the player character.
    public final int type_npc = 1; // Identifies a non-player character (NPC).
    public final int type_monster = 2; // Identifies a monster or enemy.
    public final int type_sword = 3; // Identifies a sword item.
    public final int type_axe = 4; // Identifies an axe item.
    public final int type_shield = 5; // Identifies a shield item.
    public final int type_consumable = 6; // Identifies a consumable item, like potions.
    public final int type_pickupOnly = 7; // Identifies an item as pickup only, like coins.


    // Position and Movement
    public int worldX, worldY; // Entity's position in the game world.
    public int speed; // Movement speed of the entity.
    public String direction = "down"; // Current movement direction (up, down, left, right).

    // Animation
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // Movement animation frames.
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2; // Attack animation frames.
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
    // Equipment
    public Entity currentWeapon;        // The entity's currently equipped weapon, affecting attack stats.
    public Entity currentShield;        // The entity's currently equipped shield, affecting defense stats.
    public Projectile projectile;       // The entity's currently equipped projectile,
    public boolean invincible = false;  // Flag for invincibility to prevent repeated damage.
    public boolean attacking = false;   // Determines if the entity is attacking, triggering attack animations.
    public boolean alive = true;        // Flag indicating if the entity is alive.
    public boolean dying = false;       // Flag indicating if the entity is in the process of dying.
    boolean hpBarOn = false;            // Flag to display the health bar when true.

    // Item Attributes
    public int attackValue;             // Attack value provided by the current weapon or item.
    public int defenseValue;            // Defense value provided by the current shield or item.
    public String description = "";     // A brief description of the item, which can be displayed in the inventory.
    public int useCost;                 // The resource cost for using this item (mana for projectiles).
    public int value;                   // Value of the item (like in healing or money)

    // Dialogue
    String[] dialogues = new String[20]; // Array to store dialogue text, allowing multiple phrases.
    int dialogueIndex = 0; // Current dialogue index for displaying text.

    // Object Properties
    public BufferedImage image, image2, image3; // Images representing the object (e.g., key, door).
    public boolean collision = false; // Indicates if the object can trigger collisions.

    // Constructor initializes the Game Panel
    public Entity(GamePanel gp) {
        this.gp = gp;
    }


    // Sets the action for the entity, such as determining its direction or behavior.
    // This method can be overridden by subclasses to customize entity behavior.
    public void setAction() {

    }

    // Defines the reaction of the entity upon receiving damage.
    // Can be overridden in subclasses to provide specific behaviors.
    public void damageReaction() {

    }

    // Method to use a consumable item, like a potion. Can be overridden in subclasses to implement specific effects.
    // The `entity` parameter represents the entity that is being used.
    public void use(Entity entity) {

    }

    // Method to check if an item should be dropped by the entity.
// Can be overridden in subclasses to implement specific drop logic.
    public void checkDrop() {

    }

    // Method to drop an item in the game world.
    // Finds the first empty slot in the object array and assigns the dropped item to it.
    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = droppedItem;       // Place the item in the first available slot.
                gp.obj[i].worldX = worldX;     // Set item's drop position to entity's worldX.
                gp.obj[i].worldY = worldY;     // Set item's drop position to entity's worldY.
                break;
            }
        }
    }

    // Handles the entity speaking by displaying dialogue text to the player
    // and adjusting the entity's direction to face the player.
    public void speak() {
        // Reset dialogue index if no more dialogue is available
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        // Display the current dialogue
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++; // Move to the next dialogue for future interactions

        // Face the entity towards the player’s direction
        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";
        }
    }


    // Updates the entity's state each frame, handling movement, collision detection,
    // and animation updates. This ensures smooth movement and interaction with the game world.
    public void update() {
        setAction();
        // Check for tile collision.
        collisionOn = false;                                          // Reset collision state.
        gp.cChecker.checkTile(this);                            // Check if the entity is colliding with any tiles.
        gp.cChecker.checkObject(this, false);             // Check if the entity is colliding with any object.
        gp.cChecker.checkEntity(this, gp.npc);                  // Check if the entity is colliding with any other NPC.
        gp.cChecker.checkEntity(this, gp.monster);              // Check if the entity is colliding with any other monster.
        gp.cChecker.checkEntity(this, gp.iTile);                // Check for collision with interactive tiles.
        boolean contactPlayer = gp.cChecker.checkPlayer(this);  // Check if the entity is colliding with the player, if yes true, no false

        // Checks if the entity is a monster and has contacted the player.
        // If so, reduces player's life and sets them to invincible to avoid consecutive damage.
        if (this.type == type_monster && contactPlayer) {
            damagePlayer(attack); // Initiates damage process with the monster's attack value.
        }

        // If no collision detected, move the entity in the current direction.
        if (!collisionOn) {
            switch (direction) {
                case "up" -> worldY -= speed;  // Move up in the world (Y-axis).
                case "down" -> worldY += speed; // Move down.
                case "left" -> worldX -= speed; // Move left on the X-axis.
                case "right" -> worldX += speed; // Move right.
            }
        }

        // Increment spriteCounter to control the animation frame rate.
        spriteCounter++;

        // Toggle between two sprites every 12 frames to create walking animation.
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        // If entity is invincible, increment the invincibility counter.
        if (invincible) {
            invincibleCounter++; // Track invincibility duration.

            // Disable invincibility after 40 frames and reset the counter.
            if (invincibleCounter > 40) {
                invincible = false; // End invincibility.
                invincibleCounter = 0; // Reset counter for next use.
            }
        }

        // Increment the shot availability counter if it's below cooldown limit.
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++; // Increment counter.
        }
    }

    // Method to handle player damage when hit by a monster or projectile.
    // Calculates the damage based on the monster's attack and player's defense.
    public void damagePlayer(int attack) {
        // Check if the player is not currently invincible to allow damage.
        if (!gp.player.invincible) {
            gp.playSE(6); // Play sound effect when player receives damage.

            // Calculate damage as monster's attack minus player's defense, ensuring a minimum of zero damage.
            int damage = Math.max(attack - gp.player.defense, 0);
            gp.player.life -= damage; // Reduce player's life by the calculated damage amount.
            gp.player.invincible = true; // Set player to invincible to prevent consecutive hits.
        }
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

            image = switch (direction) {
                case "up" -> (spriteNum == 1) ? up1 : up2;
                case "down" -> (spriteNum == 1) ? down1 : down2;
                case "left" -> (spriteNum == 1) ? left1 : left2;
                case "right" -> (spriteNum == 1) ? right1 : right2;
                default -> down1;
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
            g2.drawImage(image, screenX, screenY, null);

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
}
