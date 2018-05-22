package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectListener implements Listener {

    private WardBuilder                 instance;

    public PlayerDisconnectListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BuildPlayer buildPlayer = instance.getManager().getPlayer(player.getUniqueId());
        World world = player.getWorld();

        if (buildPlayer == null) {
            instance.getLog().error("Profile for " + player.getUniqueId().toString() + " is null!");
            return;
        }

        if (!world.getName().equalsIgnoreCase("world")) {
            instance.getServer().getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(world.getName()))
                    .forEach(p -> {
                p.sendMessage("§e<> " + buildPlayer.getRank().getPrefix() + player.getName() +
                        " §cs'est déconnecté de ce monde. §e<>");
            });
        }

        event.setQuitMessage(buildPlayer.getRank().getPrefix() + player.getName() + " §6nous a quitté.");
        buildPlayer.savePlayerConfig();
    }

}
