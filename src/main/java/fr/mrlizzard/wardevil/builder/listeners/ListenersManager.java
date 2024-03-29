package fr.mrlizzard.wardevil.builder.listeners;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.listeners.player.*;
import fr.mrlizzard.wardevil.builder.listeners.server.BlockListener;
import fr.mrlizzard.wardevil.builder.listeners.server.ServerListener;
import fr.mrlizzard.wardevil.builder.listeners.server.SignManagerListener;
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
        pluginManager.registerEvents(new PlayerJoinListener(instance), instance);
        pluginManager.registerEvents(new PlayerDisconnectListener(instance), instance);
        pluginManager.registerEvents(new ServerListener(instance), instance);
        pluginManager.registerEvents(new AsyncPlayerChatListener(), instance);
        pluginManager.registerEvents(new PlayerMiscListener(instance), instance);
        pluginManager.registerEvents(new BlockListener(instance), instance);
        pluginManager.registerEvents(new PlayerMoveListener(instance), instance);
        pluginManager.registerEvents(new SignManagerListener(instance), instance);
    }

}
