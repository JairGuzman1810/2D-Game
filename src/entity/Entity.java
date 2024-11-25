package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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
    public int defaultSpeed; // Stores the original speed of the entity.
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
    int knockBackCounter = 0; // Counter to track the duration of the knockback effect.

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
    // Equipment
    public Entity currentWeapon;        // The entity's currently equipped weapon, affecting attack stats.
    public Entity currentShield;        // The entity's currently equipped shield, affecting defense stats.
    public Projectile projectile;       // The entity's currently equipped projectile,
    // State
    public boolean invincible = false;  // Flag for invincibility to prevent repeated damage.
    public boolean attacking = false;   // Determines if the entity is attacking, triggering attack animations.
    public boolean alive = true;        // Flag indicating if the entity is alive.
    public boolean dying = false;       // Flag indicating if the entity is in the process of dying.
    boolean hpBarOn = false;            // Flag to display the health bar when true.
    public boolean onPath = false;      // Flag indicating if the entity needs to move to a specific location or follow the player.
    public boolean knockBack = false;   // Flag to determine if the entity is currently in a knockback state.

    // Item Attributes
    public int attackValue;             // Attack value provided by the current weapon or item.
    public int defenseValue;            // Defense value provided by the current shield or item.
    public String description = "";     // A brief description of the item, which can be displayed in the inventory.
    public int useCost;                 // The resource cost for using this item (mana for projectiles).
    public int value;                   // Value of the item (like in healing or money)
    public int price;                   // The price of the item, used in the trading system to determine its cost for purchasing.
    public int knockBackPower;          // Determines the intensity of the knockback effect.

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
    // and effects like knockback and invincibility.
    public void update() {

        // Handles knockback state, where the entity is pushed back upon certain events (e.g., being hit).
        if (knockBack) {

            checkCollision(); // Check if the entity collides with something during knockback.

            if (collisionOn) {
                // Stop knockback if a collision is detected, resetting speed and state.
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            } else {
                // Apply movement based on knockback direction.
                switch (direction) {
                    case "up" -> worldY -= speed;  // Push upward.
                    case "down" -> worldY += speed; // Push downward.
                    case "left" -> worldX -= speed; // Push to the left.
                    case "right" -> worldX += speed; // Push to the right.
                }
            }

            knockBackCounter++; // Increment knockback duration.

            // End knockback effect after a specific time frame (e.g., 10 frames).
            if (knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed; // Reset speed to default.
            }

        } else {
            // Regular entity behavior when not in knockback state.

            setAction(); // Determine the entity's action (e.g., AI, movement logic).
            checkCollision(); // Check for collisions before moving.

            // If no collision is detected, move in the current direction.
            if (!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;  // Move up.
                    case "down" -> worldY += speed; // Move down.
                    case "left" -> worldX -= speed; // Move left.
                    case "right" -> worldX += speed; // Move right.
                }
            }
        }

        // Handle animation by toggling between sprites to simulate movement.
        spriteCounter++; // Increment the sprite frame counter.

        // Change to the next sprite frame every 24 frames.
        if (spriteCounter > 24) {
            spriteNum = (spriteNum == 1) ? 2 : 1; // Toggle between sprite 1 and 2.
            spriteCounter = 0; // Reset the sprite counter.
        }

        // Manage invincibility duration if the entity is in an invincible state.
        if (invincible) {
            invincibleCounter++; // Count frames of invincibility.

            // End invincibility after 40 frames and reset the counter.
            if (invincibleCounter > 40) {
                invincible = false; // Disable invincibility.
                invincibleCounter = 0; // Reset the counter for reuse.
            }
        }

        // Handle projectile cooldown by incrementing the shot availability counter up to its limit.
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++; // Increment the counter to track shot cooldown.
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
}
