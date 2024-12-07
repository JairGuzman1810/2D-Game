package entity;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    // Indicates whether the player's light source (e.g., lantern) has been updated.
    public boolean lightUpdated = false;

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
        solidArea.y = 14; // Offset within the sprite.
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;  // Width of the player collision area.
        solidArea.height = 32; // Height of the player collision area.


        // Set the player's initial position and speed.
        setDefaultValues();
    }

    // Sets the player's starting position in the world (worldX, worldY) in tile units.
    // This position is calculated based on the tile size, placing the player at a specific point on the map.
    public void setDefaultPosition() {
        worldX = gp.tileSize * 23;  // Sets the player's world X position.
        worldY = gp.tileSize * 21;  // Sets the player's world Y position.

        // Sets the player's initial movement direction to "down".
        direction = "down";
    }

    // Sets the series of dialogues for this player, which will display sequentially when level up.
    public void setDialogues() {
        dialogues[0][0] = "You are level " + level + " now!\n You feel stronger!";
    }

    // Restores the player's life and mana to their maximum values, essentially healing the player.
    // It also disables invincibility, allowing the player to take damage again.
    public void restoreStatus() {
        life = maxLife;        // Restore the player's health to the maximum.
        mana = maxMana;        // Restore the player's mana to the maximum.
        invincible = false;    // Disable invincibility, allowing the player to take damage.
        transparent = false;   // Disable transparency.
        attacking = false;     // Ensure the player is no longer in an attacking state.
        guarding = false;      // Ensure the player is no longer guarding.
        knockBack = false;     // Disable knockback effects.
        lightUpdated = true;   // Mark the light as updated since unequipping a light restores defaults.
        speed = defaultSpeed;  // Reset the player's speed to the default value.
    }

    // Sets the default values for the player's position, stats, equipment, and attributes.
    public void setDefaultValues() {
        // Player's starting position in the world (worldX, worldY) in tile units.
        worldX = gp.tileSize * 23; // Initial horizontal position (in tiles).
        worldY = gp.tileSize * 21; // Initial vertical position (in tiles).

        // The base movement speed of the player.
        defaultSpeed = 4; // Default speed value assigned to the player.
        speed = defaultSpeed; // Set the player's current speed.

        // Default movement direction.
        direction = "down"; // Player faces downward at the start of the game.

        // Player stats.
        maxLife = 6; // Player's maximum health, represented by hearts on the UI.
        life = maxLife; // Sets the player's starting health to full (all hearts filled).
        maxMana = 4; // Player's maximum mana, represented by crystals on the UI.
        mana = maxMana; // Sets the player's starting mana to full (all crystals filled).
        level = 1; // Player's initial level.
        strength = 1; // Affects the player's attack power (higher strength = more damage).
        dexterity = 1; // Affects the player's defense power (higher dexterity = less damage taken).
        exp = 0; // Starting experience points.
        nextLevelExp = 5; // Experience required to level up.
        coin = 0; // Initial coin count (used for in-game purchases).

        // Equipment setup.
        currentWeapon = new OBJ_Sword_Normal(gp); // The default weapon equipped by the player.
        currentShield = new OBJ_Shield_Wood(gp); // The default shield equipped by the player.
        currentLight = null; // No light source equipped initially.
        projectile = new OBJ_Fireball(gp); // Default projectile assigned to the player.

        // Calculated stats based on player attributes and equipment.
        attack = getAttack(); // Calculate attack power based on strength and weapon.
        defense = getDefense(); // Calculate defense power based on dexterity and shield.

        // Initialize player images and animations.
        getImage(); // Load movement sprites for the player.
        getAttackImage(); // Load attack animation sprites.
        getGuardImage(); // Load guard/block animation sprites.

        // Set the player's initial inventory.
        setItems(); // Populate the player's inventory with starting items (e.g., health potions).
        setDialogues();
    }

    // Sets the initial items for the player's inventory.
    public void setItems() {

        // Clears any items already in the player's inventory.
        inventory.clear();

        // Adds the current weapon to the inventory.
        inventory.add(currentWeapon);

        // Adds the current shield to the inventory.
        inventory.add(currentShield);

        // Adds a new key object to the inventory, allowing the player to open doors.
        inventory.add(new OBJ_Key(gp));
    }


    // Returns the player's total attack power based on their strength and equipped weapon.
    public int getAttack() {
        // Sets the attack area size according to the equipped weapon.
        attackArea = currentWeapon.attackArea;

        // Sets the duration of the first motion phase for the weapon's attack.
        motion1_duration = currentWeapon.motion1_duration;

        // Sets the duration of the second motion phase for the weapon's attack.
        motion2_duration = currentWeapon.motion2_duration;

        // Calculates the total attack power as strength multiplied by the weapon's attack value.
        return strength * currentWeapon.attackValue;
    }

    // Calculates the player's total defense based on dexterity and shield.
    public int getDefense() {
        return dexterity * currentShield.defenseValue;
    }

    // Finds the index of the currently equipped weapon in the inventory.
    public int getCurrentWeaponSlot() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) == currentWeapon) {
                return i; // Return the index immediately when found.
            }
        }
        return -1; // Return -1 if no weapon is found.
    }

    // Finds the index of the currently equipped shield in the inventory.
    public int getCurrentShieldSlot() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) == currentShield) {
                return i; // Return the index immediately when found.
            }
        }
        return -1; // Return -1 if no shield is found.
    }


    // Load the images for the player's movement in all four directions.
    public void getImage() {
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

    // Sets all player movement images to the provided sleeping image.
    public void getSleepingImage(BufferedImage image) {
        // Use the setup method to load and scale player images for different movements
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }

    // Loads the images for the player's attack animations in all four directions based on the equipped weapon.
    public void getAttackImage() {
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

    // Load the images for the player's blocking in all four directions.
    public void getGuardImage() {
        guardUp = setup("/player/boy_guard_up", gp.tileSize, gp.tileSize);
        guardDown = setup("/player/boy_guard_down", gp.tileSize, gp.tileSize);
        guardLeft = setup("/player/boy_guard_left", gp.tileSize, gp.tileSize);
        guardRight = setup("/player/boy_guard_right", gp.tileSize, gp.tileSize);
    }


    // Update method, called every frame, processes key inputs, moves the player, and handles collisions and object interaction.
    public void update() {
        // Handles knockback state, where the entity is pushed back upon certain events (e.g., being hit).
        if (knockBack) {

            // Check for tile collision.
            collisionOn = false; // Reset collision state.
            gp.cChecker.checkTile(this); // Check if the player is colliding with any tiles.

            // Check for collisions with objects (like keys or doors).
            // objIndex will hold the index of the object the player collides with.
            gp.cChecker.checkObject(this, true);

            // Check for collisions with NPC
            // npcIndex will hold the index of the NPC the player collides with.
            gp.cChecker.checkEntity(this, gp.npc);

            // Check for collisions with monster.
            gp.cChecker.checkEntity(this, gp.monster);

            // Check for collision with interactive tiles.
            gp.cChecker.checkEntity(this, gp.iTile);


            if (collisionOn) {
                // Stop knockback if a collision is detected, resetting speed and state.
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            } else {
                // Apply movement based on knockback direction.
                switch (knockBackDirection) {
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

        }
        // Check if player its attacking.
        else if (attacking) {
            // Call method of attacking.
            attacking();
        } else if (keyH.spacePressed) {
            // Sets the guarding state to true when the space key is pressed.
            guarding = true;
            // Increment the guard counter each frame when guarding.
            guardCounter++;
        }
        // Check if no movement keys are pressed to keep the player idle.
        else if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed && !keyH.enterPressed) {
            // Increment the stand counter each frame when idle.
            standCounter++;

            // If the player has been idle for 20 frames, switch to the standstill sprite.
            if (standCounter == 20) {
                spriteNum = 1; // Set sprite to represent the standing still position.
                standCounter = 0; // Reset the stand counter for future idle checks.
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

            // Reset the enter guarding state.
            guarding = false;

            // Reset the enter guard counter.
            guardCounter = 0;

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
            for (int i = 0; i < gp.projectile[i].length; i++) {
                if (gp.projectile[gp.currentMap][i] == null) {
                    gp.projectile[gp.currentMap][i] = projectile;
                    break;
                }
            }

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
                transparent = false; // End transparency.
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

        // Checks if the player's life has dropped to 0 or below.
        // If so, it triggers the game over state and plays a sound effect for game over.
        if (life <= 0) {
            gp.gameState = gp.gameOverState;  // Changes the game state to the game over state.
            gp.ui.commandNum = -1; // Unselect the option.
            gp.stopMusic();  // Stop the background music.
            gp.playSE(12);  // Plays the game over sound effect.
        }
    }

    // Handles object interaction when the player collides with an object (e.g., key, door, chest).
    // This method checks if an object is available at the collision point and processes the pickup.
    public void pickUpObject(int i) {
        // Verify that a valid object is present at the collision point (999 indicates no object).
        if (i != 999) {

            // If the object is of type "pickup only," it cannot be added to the inventory and is used immediately.
            if (gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                // Trigger immediate use of the object (e.g., health or mana pickup).
                gp.obj[gp.currentMap][i].use(this);
                // Remove the object from the world since it has been picked up.
                gp.obj[gp.currentMap][i] = null;

            } else if (gp.obj[gp.currentMap][i].type == type_obstacle) {
                // Check if the player has pressed the enter key to interact with an obstacle object.
                if (keyH.enterPressed) {
                    attackCancel = true;  // Cancel any ongoing attack.
                    // Trigger interaction with the obstacle object (e.g., open a door or chest).
                    gp.obj[gp.currentMap][i].interact();
                }

            } else {
                String text;

                // Inventory items can be stored if there's space available.
                if (canObtainItem(gp.obj[gp.currentMap][i])) {
                    // Play a pickup sound effect to confirm the action.
                    gp.playSE(1);
                    // Show a message with the item's name to the player.
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";

                } else {
                    // Notify the player when inventory is full and can't carry more items.
                    text = "You cannot carry more items!";
                }

                // Remove the object from the world since it has been picked up.
                gp.obj[gp.currentMap][i] = null;

                // Display the pickup or inventory message in the UI.
                gp.ui.addMessage(text);
            }
        }
    }

    // Manages interaction with non-playable characters (NPCs).
    // This method checks for collisions with NPCs and initiates dialogue if the player interacts with them.
    public void interactNPC(int i) {
        // If there's an NPC at the collision point and the enter key is pressed, start dialogue.
        if (i != 999 && gp.keyH.enterPressed) {
            attackCancel = true; // Prevent the player from attacking during the dialogue.
            gp.npc[gp.currentMap][i].speak(); // Call the speak method of the colliding NPC to display its dialogue.
        }
    }

    // This method handles damaging a monster by decreasing its life when the player attacks.
    // It checks if the monster is invincible to prevent damage during the invincibility period.
    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {
        // Ensure the monster index is valid (not 999, which indicates no monster).
        if (i != 999) {
            // Check if the monster is not currently invincible.
            if (!gp.monster[gp.currentMap][i].invincible) {
                gp.playSE(5); // Play sound effect indicating a hit on the monster

                // Apply knockback if the knockBackPower is greater than 0.
                if (knockBackPower > 0) {
                    setKnockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower); // Push the monster back.
                }

                // If the monster is in an off-balance state, it receives increased damage (multiplied by 5 in this case).
                if (gp.monster[gp.currentMap][i].offBalance) {
                    attack *= 5;
                }

                // Calculates the damage dealt, ensuring it is at least zero (if defense is higher).
                int damage = Math.max(attack - gp.monster[gp.currentMap][i].defense, 0);
                gp.ui.addMessage(damage + " damage!"); // Display the amount of damage dealt to the monster
                gp.monster[gp.currentMap][i].life -= damage; // Reduce the monster's life by the damage amount
                gp.monster[gp.currentMap][i].invincible = true; // Set invincibility to prevent further hits
                gp.monster[gp.currentMap][i].damageReaction(); // Trigger the monster's reaction to damage

                // Mark the monster as dying if its life reaches zero or below
                if (gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!"); // Display a message indicating the monster was killed
                    gp.ui.addMessage("Exp +" + gp.monster[gp.currentMap][i].exp); // Display experience gained
                    exp += gp.monster[gp.currentMap][i].exp; // Add monster's experience points to player
                    checkLevelUp(); // Check if player has leveled up
                }
            }
        }
    }


    // Handles damaging an interactive tile, such as a destructible object in the game world.
    public void damageInteractiveTile(int i) {

        // Check if there is a valid interactive tile at index i that is destructible,
        // can be damaged by the player's current item, and is not currently invincible.
        if (i != 999 && gp.iTile[gp.currentMap][i].destructible && gp.iTile[gp.currentMap][i].isCorrectItem(this) && !gp.iTile[gp.currentMap][i].invincible) {

            // Play the tile's sound effect for taking damage.
            gp.iTile[gp.currentMap][i].playSE();

            // Decrease the tile's life by 1.
            gp.iTile[gp.currentMap][i].life--;

            // Set the tile to invincible to prevent immediate consecutive hits.
            gp.iTile[gp.currentMap][i].invincible = true;

            // Generate particles.
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            // If the tile's life reaches zero, replace it with its destroyed variant.
            if (gp.iTile[gp.currentMap][i].life == 0) {
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedVariant();
            }
        }
    }

    // Handles damaging an interactive tile (e.g., a rock) using a projectile.
    // Deactivates the projectile upon impact and generates particles at its position.
    public void damageProjectile(int i) {

        if (i != 999) { // Check if a valid projectile index is provided.

            Entity projectile = gp.projectile[gp.currentMap][i]; // Retrieve the projectile entity.

            projectile.alive = false; // Mark the projectile as inactive upon impact.

            generateParticle(projectile, projectile); // Generate impact particles at the projectile's position.
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
            startDialogue(this, 0);
        }
    }

    // Checks if a monster is encountered at a specific collision point and, if so,
    // reduces the player's life by 1 unless the player is currently invincible.
    public void contactMonster(int i) {
        // Verify if a monster is found at the collision point (999 indicates no monster present).
        if (i != 999) {
            // If the player is not invincible, reduce life and activate invincibility.
            if (!invincible && !gp.monster[gp.currentMap][i].dying) {
                gp.playSE(6); // Play sound effect receive damage
                int damage = Math.max(gp.monster[gp.currentMap][i].attack - defense, 1);
                life -= damage;
                invincible = true; // Set invincibility to prevent immediate further damage.
                transparent = true; // Set transparency effect.
            }
        }
    }

    // Selects an item from the inventory and handles its use or equipping.
    // This method checks the type of the selected item and applies the appropriate effect, such as equipping it or using it.
    public void selectItem() {
        // Get the index of the item selected in the inventory slot from the UI
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        // Check if the selected index is valid (within bounds of the inventory)
        if (itemIndex < inventory.size()) {
            // Retrieve the item from the inventory at the selected index
            Entity selectedItem = inventory.get(itemIndex);

            // If the selected item is a weapon (either a sword or an axe),
            // equip it, update the player's attack power, and load the corresponding attack images.
            if (selectedItem.type == type_sword || selectedItem.type == type_axe) {
                currentWeapon = selectedItem;  // Equip the weapon
                attack = getAttack();          // Update the player's attack power based on the equipped weapon
                getAttackImage();        // Load the attack animations based on the weapon type
            }

            // If the selected item is a shield, equip it, and update the player's defense stats.
            if (selectedItem.type == type_shield) {
                currentShield = selectedItem;  // Equip the shield
                defense = getDefense();        // Update the player's defense value based on the shield's defense power
            }

            // Toggle the selected item as a light source and trigger a lighting update.
            if (selectedItem.type == type_light) {
                if (currentLight == selectedItem) {
                    currentLight = null; // Turn off the light if it's already equipped.
                } else {
                    currentLight = selectedItem; // Equip the light source.
                }
                lightUpdated = true; // Signal that lighting needs to be recalculated.
            }

            // If the selected item is consumable (e.g., a potion),
            // use it and remove it from the inventory.
            if (selectedItem.type == type_consumable) {
                // Use the consumable item (e.g., heal or apply effect)
                if (selectedItem.use(this)) {

                    if (selectedItem.amount > 1) {
                        selectedItem.amount--;
                    } else {
                        inventory.remove(itemIndex);     // Remove the used item from the inventory
                    }

                }
            }
        }
    }

    // Searches for an item in the inventory by name.
    // Returns its index or 999 if not found.
    public int searchItemInInventory(String itemName) {
        int itemIndex = 999; // Default index for not found.

        // Loop through the inventory to find the item.
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(itemName)) {
                itemIndex = i; // Found the item, set its index.
                break;         // Exit the loop.
            }
        }

        return itemIndex; // Return the item's index or 999.
    }

    // Checks if an item can be added to the inventory.
    public boolean canObtainItem(Entity item) {
        // If the item is stackable, check for it in the inventory.
        if (item.stackable) {
            int index = searchItemInInventory(item.name);
            if (index != 999) {
                inventory.get(index).amount++; // Increase the item's amount.
                return true; // Successfully updated.
            }
        }

        // If inventory has space, add the item.
        if (inventory.size() < maxInventorySize) {
            inventory.add(item);
            return true; // Successfully added.
        }

        return false; // Inventory is full, cannot add.
    }


    // Draws the player sprite, adjusting position and appearance based on direction, attack, and guarding status.
    public void draw(Graphics2D g2) {
        // Adjust tempScreenX and tempScreenY for attack animation positioning.
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Adjust position for attack animations in specific directions.
        if (attacking) {
            switch (direction) {
                case "up" -> tempScreenY = screenY - gp.tileSize;
                case "left" -> tempScreenX = screenX - gp.tileSize;
            }
        }

        // Select the correct sprite based on direction, guarding, or attacking status.
        BufferedImage image = switch (direction) {
            case "up" ->
                    guarding ? guardUp : (attacking ? (spriteNum == 1 ? attackUp1 : attackUp2) : (spriteNum == 1 ? up1 : up2));
            case "down" ->
                    guarding ? guardDown : (attacking ? (spriteNum == 1 ? attackDown1 : attackDown2) : (spriteNum == 1 ? down1 : down2));
            case "left" ->
                    guarding ? guardLeft : (attacking ? (spriteNum == 1 ? attackLeft1 : attackLeft2) : (spriteNum == 1 ? left1 : left2));
            case "right" ->
                    guarding ? guardRight : (attacking ? (spriteNum == 1 ? attackRight1 : attackRight2) : (spriteNum == 1 ? right1 : right2));
            default -> down1; // Default to a neutral sprite if the direction is unrecognized.
        };

        // Apply transparency effect if the player is invincible (e.g., during a damage phase).
        if (transparent) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        // Draw the selected sprite at the calculated position.
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Reset transparency to normal after rendering.
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

}
