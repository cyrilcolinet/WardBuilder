package fr.mrlizzard.wardevil.builder;

import fr.mrlizzard.wardevil.builder.listeners.player.PlayerDisconnectListener;
import fr.mrlizzard.wardevil.builder.listeners.player.PlayerJoinListener;
import fr.mrlizzard.wardevil.builder.managers.CommandManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WardBuilder extends JavaPlugin {

    private PluginManager           pluginManager;

    @Override
    public void onLoad() {
        super.onLoad();

        pluginManager = this.getServer().getPluginManager();
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

}
