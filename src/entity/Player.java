package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Axe;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// The Player class extends the Entity class, inheriting common attributes like position and speed.
// It adds specific logic for handling player movement, drawing, and now, collision detection and object interaction.
public class Player extends Entity {

    // Reference to the KeyHandler, which captures the player's key inputs for movement.
    KeyHandler keyH;

    // screenX and screenY represent the player's position on the screen, always centered.
    public final int screenX;
    public final int screenY;

    // Tracks idle frames to set player to standstill position after a delay.
    int standCounter = 0;

    // Indicates whether an attack can be canceled, such as when interacting with an NPC.
    public boolean attackCancel = false;

    // Inventory list that holds items collected by the player, such as weapons, shields, etc.
    public ArrayList<Entity> inventory = new ArrayList<>();

    // Maximum number of items that the player can carry in the inventory.
    public final int maxInventorySize = 20;

    // Constructor initializes the Player with references to the game environment and key handler.
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;

        // Calculate and set the player's position at the center of the screen.
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Define the solid area for the player, which will be used for collision detection.
        solidArea = new Rectangle();
        solidArea.x = 8;  // Offset of the solid area within the player's sprite.
        solidArea.y = 16; // Offset within the sprite.
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;  // Width of the player collision area.
        solidArea.height = 32; // Height of the player collision area.


        // Set the player's initial position and speed.
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    // Sets the default values for the player's position and speed.
    public void setDefaultValues() {
        // Player's starting position in the world (worldX, worldY) in tile units.
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        // Set the player's movement speed.
        speed = 4;
        // Default movement direction.
        direction = "down";

        // Player stats
        maxLife = 6; // Player's maximum health represented by heart images on the screen.
        life = maxLife; // Sets the player's initial health equal to the maximum, displayed as full hearts.
        maxMana = 4; // Player's maximum mana represented by crystal images on the screen.
        mana = maxMana; // Sets the player's initial mana equal to the maximum, displayed as full crystals.
        level = 1; // Player's starting level, which increases as they gain experience.
        strength = 1; // The more strength the player has, the more damage they deal.
        dexterity = 1; // The more dexterity the player has, the less damage they receive.
        exp = 0; // Player's experience points, earned by defeating enemies and completing objectives.
        nextLevelExp = 5; // The amount of experience required to reach the next level.
        coin = 0; // The player's current number of coins, used for in-game purchases.
        // Sets the player's current weapon to a normal sword.
        currentWeapon = new OBJ_Axe(gp);
        // Sets the player's current shield to a wooden shield.
        currentShield = new OBJ_Shield_Wood(gp);
        // Sets the player's current projectile to a fire ball.
        projectile = new OBJ_Fireball(gp);
        // Calculates total attack power based on strength and the equipped weapon.
        attack = getAttack();
        // Calculates total defense power based on dexterity and the equipped shield.
        defense = getDefense();
    }

    // Sets the initial items for the player's inventory.
    public void setItems() {
        // Adds the current weapon to the inventory.
        inventory.add(currentWeapon);

        // Adds the current shield to the inventory.
        inventory.add(currentShield);

        // Adds a new key object to the inventory, allowing the player to open doors.
        inventory.add(new OBJ_Key(gp));
    }


    // Calculates the player's total attack power based on strength and equipped weapon.
    public int getAttack() {
        // Update attack area based on the current weapon's attributes.
        attackArea = currentWeapon.attackArea;

        // Return total attack power by multiplying strength with weapon's attack value.
        return strength * currentWeapon.attackValue;
    }

    // Calculates the player's total defense based on dexterity and shield.
    public int getDefense() {
        return dexterity * currentShield.defenseValue;
    }

    // Load the images for the player's movement in all four directions.
    public void getPlayerImage() {
        // Use the setup method to load and scale player images for different movements
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    // Loads the images for the player's attack animations in all four directions based on the equipped weapon.
    public void getPlayerAttackImage() {
        // Use the setup method to load and scale player images for different attacks
        if (currentWeapon.type == type_sword) {
            // Load images for sword attack in all four directions
            attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        } else if (currentWeapon.type == type_axe) {
            // Load images for axe attack in all four directions
            attackUp1 = setup("/player/boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        }
    }


    // Update method, called every frame, processes key inputs, moves the player, and handles collisions and object interaction.
    public void update() {
        // Check if player its attacking
        if (attacking) {
            // Call method of attacking
            attacking();
        }
        // Check if no movement keys are pressed to keep the player idle
        else if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed && !keyH.enterPressed) {
            // Increment the stand counter each frame when idle
            standCounter++;

            // If the player has been idle for 20 frames, switch to the standstill sprite
            if (standCounter == 20) {
                spriteNum = 1; // Set sprite to represent the standing still position
                standCounter = 0; // Reset the stand counter for future idle checks
            }

        } else {

            // Check and update the player's direction based on key inputs.
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // Check for tile collision.
            collisionOn = false; // Reset collision state.
            gp.cChecker.checkTile(this); // Check if the player is colliding with any tiles.

            // Check for collisions with objects (like keys or doors).
            // objIndex will hold the index of the object the player collides with.
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex); // Call the method to handle object interaction.

            // Check for collisions with NPC
            // npcIndex will hold the index of the NPC the player collides with.
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Check for collisions with monster.
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // Check for collision with interactive tiles.
            gp.cChecker.checkEntity(this, gp.iTile);

            // Check for event
            gp.eHandler.checkEvent();

            // If no collision detected, move the player in the current direction.
            if (!collisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "up" -> worldY -= speed;  // Move up in the world (Y-axis).
                    case "down" -> worldY += speed; // Move down.
                    case "left" -> worldX -= speed; // Move left on the X-axis.
                    case "right" -> worldX += speed; // Move right.
                }
            }

            // When the enter key is pressed, initiate the player's attack sequence if it hasn't been canceled.
            if (keyH.enterPressed && !attackCancel) {
                gp.playSE(7); // Play attack sound effect.
                attacking = true; // Set player to attacking state.
                spriteCounter = 0; // Reset the sprite animation counter for the attack animation.
            }

            // Reset attackCancel after each attack cycle.
            attackCancel = false;

            // Reset the enter key state to avoid repeated interactions in the same frame.
            gp.keyH.enterPressed = false;

            // Increment spriteCounter to control the animation frame rate.
            spriteCounter++;

            // Toggle between two sprites every 12 frames to create walking animation.
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }

        }

        // If shot key is pressed and projectile is ready, launch a projectile.
        // The projectile cannot be launched again until cooldown period (30 frames) has elapsed.
        if (gp.keyH.shotKeyPressed && !projectile.alive && shotAvailableCounter == 30 && projectile.haveResource(this)) {
            // Set the initial coordinates, direction, and entity that cast the projectile.
            projectile.set(worldX, worldY, direction, true, this);

            // Add the projectile to the list for rendering and collision logic.
            gp.projectileList.add(projectile);

            // Reset the shot availability counter after firing the projectile.
            shotAvailableCounter = 0;

            // Play sound effect for shooting the projectile.
            gp.playSE(10);

            // Spend mana
            projectile.subtractResource(this);
        }

        // If player is invincible, increment the invincibility counter.
        if (invincible) {
            invincibleCounter++; // Track invincibility duration.

            // Disable invincibility after 60 frames and reset the counter.
            if (invincibleCounter > 60) {
                invincible = false; // End invincibility.
                invincibleCounter = 0; // Reset counter for next use.
            }
        }

        // Increment the shot availability counter if it's below cooldown limit.
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++; // Increment counter.
        }

        // Ensure that the player's life does not exceed the maximum limit (When using a potion or pick up a heart).
        life = Math.min(life, maxLife);

        // Ensure that the player's mana does not exceed the maximum limit (When using a potion or pick up a crystal).
        mana = Math.min(mana, maxMana);

    }

    // Controls the attack animation by toggling between two frames.
    private void attacking() {
        spriteCounter++; // Increment counter for frame control.


        // Determine the attack phase based on spriteCounter.
        if (spriteCounter <= 5) {
            spriteNum = 1; // Initial attack phase.
        } else if (spriteCounter <= 25) {
            spriteNum = 2; // Second phase during attack.

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;

            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y for the attackArea
            switch (direction) {
                case "up" -> worldY -= attackArea.height;
                case "down" -> worldY += attackArea.height;
                case "left" -> worldX -= attackArea.width;
                case "right" -> worldX += attackArea.width;
            }

            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision with the updated worldX, worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack);

            // Check interactive tile collision with the updated worldX, worldY and solidArea
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;


        } else {
            spriteNum = 1; // Reset phase after completing the attack.
            spriteCounter = 0; // Reset sprite counter.
            attacking = false; // End attacking animation.
        }
    }


    // Handles object interaction when the player collides with an object (e.g., key, door, chest).
    // This method checks if an object is available at the collision point and processes the pickup.
    public void pickUpObject(int i) {
        // Verify that a valid object is present at the collision point (999 indicates no object).
        if (i != 999) {
            String text;

            // If the object is of type "pickup only," it cannot be added to the inventory and is used immediately.
            if (gp.obj[i].type == type_pickupOnly) {
                gp.obj[i].use(this); // Trigger immediate use of the object (e.g., health or mana pickup).

            } else {
                // Inventory items can be stored if there's space available.
                if (inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[i]);  // Add the object to the inventory list.
                    gp.playSE(1);              // Play a pickup sound effect to confirm the action.
                    text = "Got a " + gp.obj[i].name + "!";  // Show a message with the item's name to the player.
                } else {
                    // Notify the player when inventory is full and can't carry more items.
                    text = "You cannot carry more items!";
                }

                // Display the pickup or inventory message in the UI.
                gp.ui.addMessage(text);
            }
            // Remove the object from the world since it has been picked up.
            gp.obj[i] = null;
        }
    }

    // Manages interaction with non-playable characters (NPCs).
    // This method checks for collisions with NPCs and initiates dialogue if the player interacts with them.
    public void interactNPC(int i) {
        // If there's an NPC at the collision point and the enter key is pressed, start dialogue.
        if (i != 999 && gp.keyH.enterPressed) {
            attackCancel = true; // Prevent the player from attacking during the dialogue.
            gp.gameState = gp.dialogueState; // Set the game state to allow dialogue interaction.
            gp.npc[i].speak(); // Call the speak method of the colliding NPC to display its dialogue.
        }
    }

    // This method handles damaging a monster by decreasing its life when the player attacks.
    // It checks if the monster is invincible to prevent damage during the invincibility period.
    public void damageMonster(int i, int attack) {
        // Ensure the monster index is valid (not 999, which indicates no monster).
        if (i != 999) {
            // Check if the monster is not currently invincible.
            if (!gp.monster[i].invincible) {
                gp.playSE(5); // Play sound effect indicating a hit on the monster

                // Calculates the damage dealt, ensuring it is at least zero (if defense is higher).
                int damage = Math.max(attack - gp.monster[i].defense, 0);
                gp.ui.addMessage(damage + " damage!"); // Display the amount of damage dealt to the monster
                gp.monster[i].life -= damage; // Reduce the monster's life by the damage amount
                gp.monster[i].invincible = true; // Set invincibility to prevent further hits
                gp.monster[i].damageReaction(); // Trigger the monster's reaction to damage

                // Mark the monster as dying if its life reaches zero or below
                if (gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[i].name + "!"); // Display a message indicating the monster was killed
                    gp.ui.addMessage("Exp +" + gp.monster[i].exp); // Display experience gained
                    exp += gp.monster[i].exp; // Add monster's experience points to player
                    checkLevelUp(); // Check if player has leveled up
                }
            }
        }
    }

    // Handles damaging an interactive tile, such as a destructible object in the game world.
    public void damageInteractiveTile(int i) {

        // Check if there is a valid interactive tile at index i that is destructible,
        // can be damaged by the player's current item, and is not currently invincible.
        if (i != 999 && gp.iTile[i].destructible && gp.iTile[i].isCorrectItem(this) && !gp.iTile[i].invincible) {

            // Play the tile's sound effect for taking damage.
            gp.iTile[i].playSE();

            // Decrease the tile's life by 1.
            gp.iTile[i].life--;

            // Set the tile to invincible to prevent immediate consecutive hits.
            gp.iTile[i].invincible = true;

            // If the tile's life reaches zero, replace it with its destroyed variant.
            if (gp.iTile[i].life == 0) {
                gp.iTile[i] = gp.iTile[i].getDestroyedVariant();
            }
        }
    }

    // Method to handle leveling up the player based on experience gained.
    public void checkLevelUp() {
        // Check if player experience meets or exceeds the required for next level.
        if (exp >= nextLevelExp) {
            level++; // Increase player level
            nextLevelExp *= 2; // Set experience threshold for next level
            maxLife += 2; // Increase maximum life points
            strength++; // Increase player strength
            dexterity++; // Increase player dexterity
            attack = getAttack(); // Recalculate attack with updated strength
            defense = getDefense(); // Recalculate defense with updated dexterity
            gp.playSE(8); // Play sound effect for leveling up
            gp.gameState = gp.dialogueState; // Trigger dialogue state to show level up message
            gp.ui.currentDialogue = "You are level " + level + " now!\n You feel stronger!";
        }
    }

    // Checks if a monster is encountered at a specific collision point and, if so,
    // reduces the player's life by 1 unless the player is currently invincible.
    public void contactMonster(int i) {
        // Verify if a monster is found at the collision point (999 indicates no monster present).
        if (i != 999) {
            // If the player is not invincible, reduce life and activate invincibility.
            if (!invincible && !gp.monster[i].dying) {
                gp.playSE(6); // Play sound effect receive damage
                int damage = Math.max(gp.monster[i].attack - defense, 0);
                life -= damage;
                invincible = true; // Set invincibility to prevent immediate further damage.
            }
        }
    }

    // Selects an item from the inventory and handles its use or equipping.
// This method checks the type of the selected item and applies the appropriate effect, such as equipping it or using it.
    public void selectItem() {
        // Get the index of the item selected in the inventory slot from the UI
        int itemIndex = gp.ui.getItemIndexOnSlot();

        // Check if the selected index is valid (within bounds of the inventory)
        if (itemIndex < inventory.size()) {
            // Retrieve the item from the inventory at the selected index
            Entity selectedItem = inventory.get(itemIndex);

            // If the selected item is a weapon (either a sword or an axe),
            // equip it, update the player's attack power, and load the corresponding attack images.
            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentWeapon = selectedItem;  // Equip the weapon
                attack = getAttack();          // Update the player's attack power based on the equipped weapon
                getPlayerAttackImage();        // Load the attack animations based on the weapon type
            }

            // If the selected item is a shield, equip it, and update the player's defense stats.
            if (selectedItem.type == type_shield) {
                currentShield = selectedItem;  // Equip the shield
                defense = getDefense();        // Update the player's defense value based on the shield's defense power
            }

            // If the selected item is consumable (e.g., a potion),
            // use it and remove it from the inventory.
            if (selectedItem.type == type_consumable) {
                selectedItem.use(this);           // Use the consumable item (e.g., heal or apply effect)
                inventory.remove(itemIndex);     // Remove the used item from the inventory
            }
        }
    }


    // Draw the player sprite at the center of the screen, using screenX and screenY.
    public void draw(Graphics2D g2) {
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
        BufferedImage image = switch (direction) {
            case "up" -> attacking ? (spriteNum == 1 ? attackUp1 : attackUp2) : (spriteNum == 1 ? up1 : up2);
            case "down" -> attacking ? (spriteNum == 1 ? attackDown1 : attackDown2) : (spriteNum == 1 ? down1 : down2);
            case "left" -> attacking ? (spriteNum == 1 ? attackLeft1 : attackLeft2) : (spriteNum == 1 ? left1 : left2);
            case "right" ->
                    attacking ? (spriteNum == 1 ? attackRight1 : attackRight2) : (spriteNum == 1 ? right1 : right2);
            default -> down1; // Default to down1 if direction is unrecognized.
        };

        // Apply invincibility blinking effect
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        // Draw the player sprite
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Reset transparency
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
