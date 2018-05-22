package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private WardBuilder                 instance;

    public PlayerMoveListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Boolean onSpawn = event.getTo().getWorld().getName().equals("world");

        if (!onSpawn) {
            World world = instance.getWorldManager().getWorlds().get(event.getTo().getWorld().getName());

            if (world == null)
                return;

            if (!world.getBuilders().containsKey(player.getUniqueId()) && !world.getSpecators().containsKey(player.getUniqueId()) && !player.isOp()) {
                event.setTo(instance.getServer().getWorld("world").getSpawnLocation());
                player.sendMessage("§cAccès non autorisé.");
            }
        }
    }
}
