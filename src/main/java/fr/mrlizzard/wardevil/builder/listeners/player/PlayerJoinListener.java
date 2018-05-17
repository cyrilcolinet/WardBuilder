package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
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
        BuildPlayer buildPlayer;

        if (instance.getManager().getSuperUsers().contains(player.getUniqueId()))
            player.setOp(true);

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
