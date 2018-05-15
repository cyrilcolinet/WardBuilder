package fr.mrlizzard.wardevil.builder;

import fr.mrlizzard.wardevil.builder.listeners.player.PlayerDisconnectListener;
import fr.mrlizzard.wardevil.builder.listeners.player.PlayerJoinListener;
import fr.mrlizzard.wardevil.builder.managers.commands.CommandManager;
import fr.mrlizzard.wardevil.builder.uitls.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WardBuilder extends JavaPlugin {

    private PluginManager           pluginManager;
    private Logger                  logger;

    @Override
    public void onLoad() {
        super.onLoad();

        pluginManager = this.getServer().getPluginManager();
        logger = new Logger(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        new CommandManager(this);

        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerDisconnectListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public Logger getLog() {
        return logger;
    }
}
