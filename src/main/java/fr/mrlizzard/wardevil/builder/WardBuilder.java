package fr.mrlizzard.wardevil.builder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.mrlizzard.wardevil.builder.core.RedisLoader;
import fr.mrlizzard.wardevil.builder.listeners.ListenersManager;
import fr.mrlizzard.wardevil.builder.commands.CommandManager;
import fr.mrlizzard.wardevil.builder.managers.BuildManager;
import fr.mrlizzard.wardevil.builder.managers.ConfigManager;
import fr.mrlizzard.wardevil.builder.managers.WorldManager;
import fr.mrlizzard.wardevil.builder.uitls.Logger;
import fr.mrlizzard.wardevil.builder.uitls.players.UUIDTranslator;
import org.bukkit.plugin.java.JavaPlugin;

public class WardBuilder extends JavaPlugin {

    private static WardBuilder      instance;

    private Logger                  logger;
    private Gson                    gson;
    private ConfigManager           config;
    private BuildManager            buildManager;
    private RedisLoader             connector;
    private UUIDTranslator          uuidTranslator;
    private WorldManager            worldManager;

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;
        logger = new Logger(this);
        gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        config = new ConfigManager(this);
        buildManager = new BuildManager(this);
        connector = new RedisLoader(this);
        uuidTranslator = new UUIDTranslator(this);
        worldManager = new WorldManager(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        new ListenersManager(this);
        new CommandManager(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        instance = null;

        getServer().getOnlinePlayers().forEach(player -> player.kickPlayer("§cRedémarrage en cours."));
        connector.destroy();
        worldManager.killWorldTasks();
    }

    public Logger getLog() {
        return logger;
    }

    public Gson getGson() {
        return gson;
    }

    public ConfigManager getConfigManager() {
        return config;
    }

    public BuildManager getManager() {
        return buildManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public RedisLoader getConnector() {
        return connector;
    }

    public UUIDTranslator getUuidTranslator() {
        return uuidTranslator;
    }

    public static WardBuilder getInstance() {
        return instance;
    }

}
