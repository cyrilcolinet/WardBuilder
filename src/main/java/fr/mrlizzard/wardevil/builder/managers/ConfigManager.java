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
import java.util.List;

public class ConfigManager {

    private WardBuilder             instance;
    private String                  missing;
    private File                    data;
    private List<Boolean>           loaders;

    public ConfigManager(WardBuilder instance) {
        this.instance = instance;
        this.missing = null;
        this.data = instance.getDataFolder();
        this.loaders = new ArrayList<Boolean>();
    }

    private boolean parseJsonFile(String file, Type type) {
        File config = new File(data, file);
        String content = null;

        if (config.exists()) {
            missing = file;
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

    public boolean containsAllFiles() {
        if (!data.exists()) {
            data.mkdirs();
            missing = "data folder";
            return false;
        }

        loaders.add(parseJsonFile("config.json", Config.class));
        loaders.add(parseJsonFile("blacklist.json", BlacklistConfig.class));
        loaders.add(parseJsonFile("whitelist.json", WhitelistConfig.class));
        loaders.add(parseJsonFile("players.json", PlayersConfig.class));

        for (Boolean loader : loaders) {
            if (!loader)
                return loader;
        }

        return true;
    }

    public String getMissing() {
        return missing;
    }

}
