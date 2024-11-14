package main;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// The Config class is responsible for saving and loading game configuration settings,
// allowing these settings to persist even after the game is restarted.
public class Config {

    // Logger to handle any errors related to reading or writing the configuration file.
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    GamePanel gp; // Reference to the GamePanel to access game settings.

    // Constructor that initializes the Config class with the GamePanel.
    public Config(GamePanel gp) {
        this.gp = gp;
    }

    // Method to save the current game settings to a configuration file.
    public void saveConfig() {
        try {
            // Open BufferedWriter to write to "config.txt".
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // Write full screen setting to the file.
            if (gp.fullScreenOn) {
                bw.write("On");
            } else {
                bw.write("Off");
            }
            bw.newLine(); // New line after full screen setting.

            // Write music volume setting to the file.
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine(); // New line after music volume.

            // Write sound effects (SE) volume setting to the file.
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine(); // New line after SE volume.

            // Close the BufferedWriter after writing all settings.
            bw.close();
        } catch (IOException e) {
            // Log a warning if there is an error while saving the config file.
            logger.log(Level.WARNING, "Error saving config file!", e);
        }
    }

    // Method to load game settings from a configuration file.
    public void loadConfig() {
        try {
            // Open BufferedReader to read from "config.txt".
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

            // Read the full screen setting from the file.
            String s = br.readLine();
            gp.fullScreenOn = s.equals("On");

            // Read and set the music volume from the file.
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            // Read and set the sound effects (SE) volume from the file.
            s = br.readLine();
            gp.se.volumeScale = Integer.parseInt(s);

            // Close the BufferedReader after reading all settings.
            br.close();
        } catch (FileNotFoundException e) {
            // Log a warning if the config file is not found.
            logger.log(Level.WARNING, "Config file not found!", e);
        } catch (IOException e) {
            // Log a warning if there is an error reading a line from the config file.
            logger.log(Level.WARNING, "Error reading config file!", e);
        }
    }
}
