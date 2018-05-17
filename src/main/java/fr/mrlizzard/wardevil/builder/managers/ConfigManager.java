package fr.mrlizzard.wardevil.builder.managers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.Config;
import fr.mrlizzard.wardevil.builder.objects.config.WhitelistConfig;

import java.io.File;
import java.lang.reflect.Type;

public class ConfigManager {

    private WardBuilder             instance;
    private String                  missing;
    private File                    data;

    private Config                  config;
    private WhitelistConfig         whitelistConfig;

    public ConfigManager(WardBuilder instance) {
        this.instance = instance;
        this.missing = null;
        this.data = instance.getDataFolder();
    }

    private Object parseJsonFile(String file, Type type) {
        File config = new File(data, file);
        String content;
        Object result;

        if (!config.exists()) {
            missing = file;
            return null;
        }

        try {
            content = Files.toString(config, Charsets.UTF_8);
            if (content == null || content.equals(""))
                throw new Exception("The file " + file + " have a null content.");

            try {
                result = instance.getGson().fromJson(content, type);
            } catch (Exception err) {
                throw new Exception("The file " + file + " is not a valid JSON format.");
            }

            return result;
        } catch (Exception except) {
            instance.getLog().error(except.getMessage());
        }

        return null;
    }

    private void checkMisc() {
        File worldsDir = new File(data, "worlds");
        File playersDir = new File(data, "players");

        if (!worldsDir.exists())
            worldsDir.mkdirs();
        if (!playersDir.mkdirs())
            playersDir.mkdirs();
    }

    public boolean loadFiles() {
        if (!data.exists()) {
            data.mkdirs();
        }

        this.checkMisc();
        config = ((Config) this.parseJsonFile("config.json", Config.class));
        whitelistConfig = ((WhitelistConfig) parseJsonFile("whitelist.json", WhitelistConfig.class));

        return true;
    }

    public String getMissing() {
        return missing;
    }

    public Config getConfig() {
        return config;
    }

    public WhitelistConfig getWhitelistConfig() {
        return whitelistConfig;
    }

}
