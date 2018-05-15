package fr.mrlizzard.wardevil.builder.managers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.Config;

import java.io.File;
import java.lang.reflect.Type;

public class ConfigManager {

    private WardBuilder             instance;
    private String                  missing;
    private File                    data;

    public ConfigManager(WardBuilder instance) {
        this.instance = instance;
        this.missing = null;
        this.data = instance.getDataFolder();
    }

    public boolean containsAllFiles() {
        if (!data.exists()) {
            data.mkdirs();
            setMissing("data folder");
            return false;
        }

        return containsConfig() && containsBlacklist() && containsWhitelist() && containsPlayers();
    }

    private boolean parseJsonFile(String file, Type type) {
        File config = new File(data, file);
        String content = null;

        if (config.exists()) {
            setMissing(file);
            return false;
        }

        try {
            content = Files.toString(config, Charsets.UTF_8);
            if (content == null || content.equals(""))
                throw new Exception(file + " content is null.");

            try {
                instance.getGson().fromJson(content, type);
            } catch (Exception err) {
                throw new Exception(file + " file is not a valid JSON format.");
            }

            return true;
        } catch (Exception except) {
            instance.getLog().error(except.getMessage());
        }

        return false;
    }

    private boolean containsConfig() {
        return parseJsonFile("config.json", Config.class);
    }

    private boolean containsBlacklist() {
        return true;
    }

    private boolean containsWhitelist() {
        return true;
    }

    private boolean containsPlayers() {
        return true;
    }

    private void setMissing(String missing) {
        this.missing = missing;
    }

    public String getMissing() {
        return missing;
    }

}
