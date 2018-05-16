package fr.mrlizzard.wardevil.builder.listeners.server;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener {

    private WardBuilder             instance;

    public ServerListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        Config config = instance.getConfigManager().getConfig();

        event.setMotd(config.getServerName() + "\n" + config.getMotd());
        event.setMaxPlayers(event.getNumPlayers() + 1);
    }
}
