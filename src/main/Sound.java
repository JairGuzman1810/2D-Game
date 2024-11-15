package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

// Sound class handles all game sound effects and music.
public class Sound {
    // Logger to handle any errors related to sound loading and playback.
    private static final Logger logger = Logger.getLogger(Sound.class.getName());

    // Clip object to hold and control sound playback.
    Clip clip;

    // Array to store URLs of different sound files used in the game.
    URL[] soundURL = new URL[30];

    // Controls audio volume at the system level.
    FloatControl fc;

    // Volume level scale (0 to 5), default set to medium.
    int volumeScale = 3;

    // Volume in dB based on volumeScale, adjusted in checkVolume().
    float volume;

    // Constructor to initialize sound URLs.
    public Sound() {
        // Load different sound effects and music tracks.

        // Index 0: Background music for the game (adventure theme).
        soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");

        // Index 1: Coin pickup sound (used for key pickup in the game).
        soundURL[1] = getClass().getResource("/sound/coin.wav");

        // Index 2: Power-up sound (used for picking up boots to increase speed).
        soundURL[2] = getClass().getResource("/sound/powerup.wav");

        // Index 3: Unlocking sound (used for unlocking doors with a key).
        soundURL[3] = getClass().getResource("/sound/unlock.wav");

        // Index 4: Victory fanfare sound.
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");

        // Index 5: Hit monster sound.
        soundURL[5] = getClass().getResource("/sound/hitmonster.wav");

        // Index 6: Receive damage sound.
        soundURL[6] = getClass().getResource("/sound/receivedamage.wav");

        // Index 7: Swing weapon sound.
        soundURL[7] = getClass().getResource("/sound/swingweapon.wav");

        // Index 8: Level up sound.
        soundURL[8] = getClass().getResource("/sound/levelup.wav");

        // Index 9: Cursor movement sound.
        soundURL[9] = getClass().getResource("/sound/cursor.wav");

        // Index 10: Shoot projectile sound.
        soundURL[10] = getClass().getResource("/sound/burning.wav");

        // Index 11: Chopping tree sound.
        soundURL[11] = getClass().getResource("/sound/cuttree.wav");

        // Index 12: Game over sound.
        soundURL[12] = getClass().getResource("/sound/gameover.wav");

        // Index 13: Stairs (change location) sound.
        soundURL[13] = getClass().getResource("/sound/stairs.wav");
    }


    // Sets the sound file to play by its index in the soundURL array.
    public void setFile(int i) {
        try {
            // Open the audio input stream from the specified URL.
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip(); // Get a Clip instance for playback.
            clip.open(ais); // Open the audio stream in the Clip.
            // Sets FloatControl for volume adjustment on the clip and applies the current volume.
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        } catch (Exception e) {
            // Log a warning if the sound file could not be loaded or played.
            logger.log(Level.WARNING, "Sound not found!", e);
        }
    }

    // Starts the playback of the selected sound.
    public void play() {
        clip.start();
    }

    // Loops the selected sound indefinitely.
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Stops the playback of the currently playing sound.
    public void stop() {
        clip.stop();
    }

    // Adjusts volume based on volumeScale (0 = mute, 5 = max).
    public void checkVolume() {
        switch (volumeScale) {
            case 0:
                volume = -80f; // Mute
                break;
            case 1:
                volume = -20f; // Low volume
                break;
            case 2:
                volume = -12f; // Moderate volume
                break;
            case 3:
                volume = -5f;  // Default volume level
                break;
            case 4:
                volume = 1f;   // High volume
                break;
            case 5:
                volume = 6f;   // Maximum volume
                break;
        }

        // Applies calculated volume to FloatControl.
        fc.setValue(volume);
    }
}
