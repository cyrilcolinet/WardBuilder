package fr.mrlizzard.wardevil.builder.managers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.stream.JsonReader;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.Config;
import fr.mrlizzard.wardevil.builder.objects.config.WhitelistConfig;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ConfigManager class
 * @author mrlizzard
 * @version 1.3.6
 */
public class ConfigManager {

    private WardBuilder             instance;
    private String                  missing;
    private File                    data;

    private Config                  config;
    private WhitelistConfig         whitelistConfig;

    /**
     * Manage config files in local
     * @param instance WardBuild instance
     */
    public ConfigManager(WardBuilder instance) {
        this.instance = instance;
        this.missing = null;
        this.data = instance.getDataFolder();
    }

    /**
     * Parse json file and fill class object
     * @param file String File path
     * @param type Type Class to convert this config file
     * @return Converted object
     */
    private Object parseJsonFile(String file, Type type) {
        File config = new File(data, file);
        Object result = null;

        // If config file doesn't erxists, set String missing to file name
        if (!config.exists()) {
            missing = file;
            return null;
        }

        // Try to parse file
        try {
            JsonReader reader = new JsonReader(new FileReader(config));

            // Check if json config has a valid format
            try {
                result = instance.getGson().fromJson(reader, type);
            } catch (Exception err) {
                throw new Exception("The file " + file + " is not a valid JSON format.");
            }
        } catch (Exception except) {
            instance.getLog().error(except.getMessage());
        }

        // Return json converted to object (or null if error occured)
        return result;
    }

    /**
     * Create default folders if not exists
     * Folders :
     *  - players/
     *  - worlds/
     */
    private void createFoldersIfNotExists() {
        List<String> folders = new ArrayList<>(Arrays.asList("players", "worlds"));

        // Create folders set in list in top
        folders.forEach(folderName -> {
            File dir = new File(data, folderName);

            // Create if not exists
            if (!dir.exists()) {
                instance.getLog().info("Creating folder : " + folderName);

                // Check if folder can be created (permissions problems or more)
                if (!dir.mkdirs())
                    instance.getLog().error("Unable to create folder... See your permissions configurations.");
            }
        });
    }

    /**
     * Load alll configuration files
     * Current files : config.json and whitelist.json
     * @return Boolean If files is correctly loaded
     */
    public boolean loadFiles() {
        // Check existing of file
        if (!data.exists()) {
            // File can't be created ? Error
            if (!data.mkdirs()) {
                instance.getLog().error("Datafolder can't be created ! See your permissions configurations.");
                return false;
            }

            // Set default files configuration if not exists
            List<String> configFiles = new ArrayList<>(Arrays.asList("config.json", "whitelist.json"));

            configFiles.forEach(file -> {
                File configFile = new File(data, file);

                // Save ressource to new path if file not exists
                if (!configFile.exists()) {
                    instance.saveResource(file, false);
                    instance.getLog().info("New file " + file + " has beean created (loaded for default config).");
                }
            });
        }

        // Create folder if not exists
        this.createFoldersIfNotExists();

        // Instanciate configurations files in object
        config = ((Config) this.parseJsonFile("config.json", Config.class));
        whitelistConfig = ((WhitelistConfig) parseJsonFile("whitelist.json", WhitelistConfig.class));

        return !(config == null || whitelistConfig == null);
    }

    /**
     * Get missing file name
     * @return String Missing config file
     */
    public String getMissing() {
        return missing;
    }

    /**
     * Get global configuration
     * @return Config
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Get whitelist configuration
     * @return WhitelistConfig
     */
    public WhitelistConfig getWhitelistConfig() {
        return whitelistConfig;
    }

}
