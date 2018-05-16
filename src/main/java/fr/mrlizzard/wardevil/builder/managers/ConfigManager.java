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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private WardBuilder             instance;
    private String                  missing;
    private File                    data;
    private Map<String, Type>       configs;
    private List<Object>            loaders;

    private Config                  config;
    private BlacklistConfig         blacklistConfig;
    private WhitelistConfig         whitelistConfig;
    private PlayersConfig           playersConfig;

    public ConfigManager(WardBuilder instance) {
        this.instance = instance;
        this.missing = null;
        this.data = instance.getDataFolder();
        this.configs = new HashMap<>();
        this.loaders = new ArrayList<>();
    }

    private Object parseJsonFile(String file, Type type) {
        File config = new File(data, file);
        String content = null;
        Object result = null;

        if (config.exists()) {
            missing = file;
            return null;
        }

        try {
            content = Files.toString(config, Charsets.UTF_8);
            if (content == null || content.equals(""))
                throw new Exception(file + " content is null.");

            try {
                result = instance.getGson().fromJson(content, type);
            } catch (Exception err) {
                throw new Exception(file + " file is not a valid JSON format.");
            }

            return result;
        } catch (Exception except) {
            instance.getLog().error(except.getMessage());
        }

        return null;
    }

    public boolean loadFiles() {
        Integer loop = 0;

        if (!data.exists()) {
            data.mkdirs();
            missing = "data folder";
            return false;
        }

        configs.put("config.json", Config.class);
        configs.put("blacklist.json", BlacklistConfig.class);
        configs.put("whitelist.json", WhitelistConfig.class);
        configs.put("players.json", PlayersConfig.class);
        configs.forEach((key, value) -> loaders.add(parseJsonFile(key, value)));

        config = ((Config)loaders.get(0));
        blacklistConfig = ((BlacklistConfig)loaders.get(1));
        whitelistConfig = ((WhitelistConfig)loaders.get(2));
        playersConfig = ((PlayersConfig)loaders.get(3));
        
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
