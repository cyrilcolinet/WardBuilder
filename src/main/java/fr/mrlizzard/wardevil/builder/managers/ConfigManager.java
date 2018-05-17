package fr.mrlizzard.wardevil.builder.managers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.BlacklistConfig;
import fr.mrlizzard.wardevil.builder.objects.config.Config;
import fr.mrlizzard.wardevil.builder.objects.config.PlayersConfig;
import fr.mrlizzard.wardevil.builder.objects.config.WhitelistConfig;

import java.io.File;
import java.lang.reflect.Type;

public class ConfigManager {

    private WardBuilder             instance;
    private String                  missing;
    private File                    data;

    private Config                  config;
    private BlacklistConfig         blacklistConfig;
    private WhitelistConfig         whitelistConfig;
    private PlayersConfig           playersConfig;

    public ConfigManager(WardBuilder instance) {
        this.instance = instance;
        this.missing = null;
        this.data = instance.getDataFolder();
    }

    private Object parseJsonFile(String file, Type type) {
        File config = new File(data, file);
        String content = null;
        Object result = null;

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

    public boolean loadFiles() {
        if (!data.exists()) {
            data.mkdirs();
            missing = "data folder";
            return false;
        }

        config = ((Config) this.parseJsonFile("config.json", Config.class));
        blacklistConfig = ((BlacklistConfig) this.parseJsonFile("blacklist.json", BlacklistConfig.class));
        whitelistConfig = ((WhitelistConfig) parseJsonFile("whitelist.json", WhitelistConfig.class));
        playersConfig = ((PlayersConfig) parseJsonFile("players.json", PlayersConfig.class));

        return true;
    }

    public String getMissing() {
        return missing;
    }

    public BlacklistConfig getBlacklistConfig() {
        return blacklistConfig;
    }

    public Config getConfig() {
        return config;
    }

    public PlayersConfig getPlayersConfig() {
        return playersConfig;
    }

    public WhitelistConfig getWhitelistConfig() {
        return whitelistConfig;
    }

}
