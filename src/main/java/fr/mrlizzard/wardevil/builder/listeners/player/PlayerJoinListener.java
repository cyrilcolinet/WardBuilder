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
        BuildPlayer buildPlayer = instance.getManager().getPlayer(player.getUniqueId());

        if (instance.getManager().getSuperUsers().contains(player.getUniqueId()))
            player.setOp(true);

        event.setJoinMessage(buildPlayer.getRank().getPrefix() + player.getName() + " §6nous a rejoint.");
    }

}
