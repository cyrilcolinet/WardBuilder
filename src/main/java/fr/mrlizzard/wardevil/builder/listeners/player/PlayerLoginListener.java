package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
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
        BuildPlayer buildPlayer;

        if (!instance.getManager().getWhitelisted().contains(player.getUniqueId())) {
            player.kickPlayer(ChatColor.RED + "Vous n'avez pas la permission de rentrer" +
                    " sur ce serveur.");
        }

        buildPlayer = instance.getManager().getPlayer(player.getUniqueId());
        if (buildPlayer == null) {
            instance.getLog().error("Error durring loading " + player.getName() + " player configuration.");
            player.kickPlayer("§cUne erreur est survenue lors du chargement de votre configuration.");
            return;
        }

        player.setDisplayName(buildPlayer.getRank().getPrefix() + player.getName());
        player.setCustomName(buildPlayer.getRank().getPrefix() + player.getName());
        player.setPlayerListName(buildPlayer.getRank().getPrefix() + player.getName());
        player.setAllowFlight(true);
    }

}
