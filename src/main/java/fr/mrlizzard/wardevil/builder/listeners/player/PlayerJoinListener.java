package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private WardBuilder                 instance;

    public PlayerJoinListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (instance.getManager().getSuperUsers().contains(player.getUniqueId()))
            player.setOp(true);

    }

}
