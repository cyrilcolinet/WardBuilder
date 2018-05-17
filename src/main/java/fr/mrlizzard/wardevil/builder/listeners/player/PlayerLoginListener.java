package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private WardBuilder                 instance;

    public PlayerLoginListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (!instance.getManager().getWhitelisted().contains(player.getUniqueId())) {
            player.kickPlayer(ChatColor.RED + "Vous n'avez pas la permission de rentrer" +
                    " sur ce serveur.");
        }

        // TODO: Check player configuration
    }

}
