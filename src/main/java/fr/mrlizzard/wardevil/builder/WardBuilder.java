package fr.mrlizzard.wardevil.builder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.mrlizzard.wardevil.builder.listeners.ListenersManager;
import fr.mrlizzard.wardevil.builder.commands.CommandManager;
import fr.mrlizzard.wardevil.builder.managers.ConfigManager;
import fr.mrlizzard.wardevil.builder.uitls.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class WardBuilder extends JavaPlugin {

    private Logger                  logger;
    private Gson                    gson;
    private ConfigManager           config;

    @Override
    public void onLoad() {
        super.onLoad();

        logger = new Logger(this);
        gson = new GsonBuilder().setPrettyPrinting().create();
        config = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (!config.loadFiles()) {
            if (config.getMissing() != null)
                logger.error("Configuration file is missing (" + config.getMissing() + ").");
            return;
        }

        new ListenersManager(this);
        new CommandManager(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public Logger getLog() {
        return logger;
    }

    public Gson getGson() {
        return gson;
    }

}
