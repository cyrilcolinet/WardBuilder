package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.World;
import fr.mrlizzard.wardevil.builder.objects.config.ConfigUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMiscListener implements Listener {

    private WardBuilder                     instance;
    private static Map<UUID, Long>          antiFlood = new HashMap<>();

    public PlayerMiscListener(WardBuilder instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if ((event.getEntity() != null) && (event.getEntity().getType().equals(EntityType.PLAYER))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (player.getWorld().getName().equalsIgnoreCase("world") && !event.getClickedBlock().getType().equals(Material.WALL_SIGN) && player.getGameMode().equals(GameMode.ADVENTURE)) {
            event.setCancelled(true);
            return;
        }

        if (!event.getClickedBlock().getType().equals(Material.WALL_SIGN))
            return;

        World world = null;
        Location location = null;

        for (World world1 : instance.getWorldManager().getWorlds().values()) {
            for (String sign : world1.getSigns()) {
                Location loc = ConfigUtils.convertStringToBlockLocation(sign);
                if (loc.equals(event.getClickedBlock().getLocation())) {
                    location = loc;
                    world = world1;
                    break;
                }
            }
        }

        if (world == null || location == null)
            return;

        if (antiFlood.containsKey(player.getUniqueId())) {
            Long time = antiFlood.get(player.getUniqueId());
            if (time > System.currentTimeMillis()) {
                if (time - 500 < System.currentTimeMillis())
                    player.sendMessage("§cVeuillez patienter entre chaque interactions.");
                return;
            }
        }

        antiFlood.put(player.getUniqueId(), System.currentTimeMillis() + 2000);
        if (event.getClickedBlock().getWorld().getName().equalsIgnoreCase(world.getName())) {
            player.sendMessage("§cVous êtes déjà connecté sur ce monde.");
            return;
        }

        if (world.isDisabled()) {
            player.sendMessage("§cCe monde n'est pas ouvert.");
            return;
        }

        if (world.isProtected() && !instance.getManager().getSuperUsers().contains(player.getUniqueId())) {
            player.sendMessage("§cCe monde est protégé et vous n'y avez pas accès, dommage ! :)");
            return;
        }

        if (!world.getSpecators().containsKey(player.getUniqueId()) && !world.getBuilders().containsKey(player.getUniqueId()) && !player.isOp()) {
            player.sendMessage("§cVous n'avez pas accès à ce monde.");
            return;
        }

        if (instance.getServer().getWorld(world.getName()) == null) {
            player.sendMessage("§aSuppression de mode économie...");
            instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), "mv load " + world.getName());
        }

        if (instance.getServer().getWorld(world.getName()) == null) {
            player.sendMessage("§cCe monde n'est pas ouvert.");
            return;
        }

        player.teleport(instance.getServer().getWorld(world.getName()).getSpawnLocation());
    }
}
