package fr.mrlizzard.wardevil.builder.listeners.server;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.World;
import fr.mrlizzard.wardevil.builder.objects.config.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignManagerListener implements Listener {

    private WardBuilder                 instance;

    public SignManagerListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player;
        String location;

        if (event.isCancelled())
            return;

        player = event.getPlayer();
        if (!event.getBlock().getType().equals(Material.WALL_SIGN))
            return;

        location = ConfigUtils.convertLocationBlockToString(event.getBlock().getLocation());
        for (World world : instance.getWorldManager().getWorlds().values()) {
            if (world.getSigns().contains(location)) {
                if (!player.isOp()) {
                    event.setCancelled(true);
                    player.sendMessage("§cVous ne pouvez pas détruire le panneau de ce monde.");
                    break;
                }

                world.getSigns().remove(location);
                player.sendMessage("§aPanneau supprimé pour le monde §e" + world.getName());
                break;
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (!player.isOp())
            return;

        if (event.getLine(0) != null) {
            String line = event.getLine(0);

            if (!line.equalsIgnoreCase("[WardBuild]"))
                return;

            if (event.getLine(1) != null)  {
                String line1 = event.getLine(1);

                if (!instance.getWorldManager().getWorlds().containsKey(line1)) {
                    event.setCancelled(true);
                    event.getBlock().breakNaturally();
                    player.sendMessage("§cMonde " + line1 + " inconnu.");
                    return;
                }

                World world = instance.getWorldManager().getWorlds().get(line1);
                if (world == null) {
                    event.setCancelled(true);
                    event.getBlock().breakNaturally();
                    player.sendMessage("§cMonde " + line1 + " inconnu.");
                    return;
                }

                if (world.getSigns().contains(ConfigUtils.convertLocationBlockToString(event.getBlock().getLocation()))) {
                    event.setCancelled(true);
                    player.sendMessage("§cCe panneau est déjà enregistré pour ce monde.");
                    return;
                }

                for (World world1 : instance.getWorldManager().getWorlds().values()) {
                    if (world1.getSigns().contains(ConfigUtils.convertLocationBlockToString(event.getBlock().getLocation()))) {
                        player.sendMessage("§cCe panneau est déjà enregistré par un autre monde.");
                        return;
                    }
                }

                player.sendMessage("§aPeanneau ajouté pour le monde §e" + world.getName());
                world.getSigns().add(ConfigUtils.convertLocationBlockToString(event.getBlock().getLocation()));
            }
        }
    }

}
