package fr.mrlizzard.wardevil.builder.listeners;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.listeners.player.PlayerDisconnectListener;
import fr.mrlizzard.wardevil.builder.listeners.player.PlayerJoinListener;
import org.bukkit.plugin.PluginManager;

public class ListenersManager {

    private WardBuilder                 instance;
    private PluginManager               pluginManager;

    public ListenersManager(WardBuilder instance) {
        this.instance = instance;
        this.pluginManager = instance.getServer().getPluginManager();

        this.loadListeners();
    }

    private void loadListeners() {
        pluginManager.registerEvents(new PlayerJoinListener(), instance);
        pluginManager.registerEvents(new PlayerDisconnectListener(), instance);
    }

}