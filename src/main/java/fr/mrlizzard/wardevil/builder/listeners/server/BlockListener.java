package fr.mrlizzard.wardevil.builder.listeners.server;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.World;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockListener implements Listener {

    private WardBuilder                 instance;

    public BlockListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBlockBlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Boolean onSpawn = event.getBlock().getWorld().getName().equals("world");

        if (onSpawn && !player.isOp()) {
            event.setCancelled(true);
            player.setGameMode(GameMode.ADVENTURE);
        }

        if (!onSpawn) {
            World world = instance.getWorldManager().getWorlds().get(event.getBlock().getWorld().getName());
            if (world == null)
                return;

            if (world.getBuilders().containsKey(player.getUniqueId()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Boolean onSpawn = event.getBlock().getWorld().getName().equals("world");

        if (onSpawn && !player.isOp()) {
            event.setCancelled(true);
            player.setGameMode(GameMode.ADVENTURE);
        }

        if (!onSpawn) {
            World world = instance.getWorldManager().getWorlds().get(event.getBlock().getWorld().getName());
            if (world == null)
                return;

            if (world.getBuilders().containsKey(player.getUniqueId()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Boolean onSpawn = event.getBlock().getWorld().getName().equals("world");

        if (onSpawn && !player.isOp()) {
            event.setCancelled(true);
            player.setGameMode(GameMode.ADVENTURE);
        }

        if (!onSpawn) {
            World world = instance.getWorldManager().getWorlds().get(event.getBlock().getWorld().getName());
            if (world == null)
                return;

            if (world.getBuilders().containsKey(player.getUniqueId()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        event.blockList().clear();
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.getBlock().getType() != Material.getMaterial(2) || event.getBlock().getType() != Material.getMaterial(3)) {
            event.setCancelled(true);
        }
    }

}
