package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * PlayerJoinListener class
 * @author mrlizzard
 * @version 2.0.0
 */
public class PlayerJoinListener implements Listener {

    private WardBuilder                 instance;

    /**
     * PlayerJoinListener constrctor.
     * @param instance WardBuilder instance
     */
    public PlayerJoinListener(WardBuilder instance) {
        this.instance = instance;
    }

    /**
     * PlayerLogin event
     * When player login
     * @param event PlayerLogin
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        BuildPlayer buildPlayer;

        // If user is not whitelisted
        // TODO : Don't work... why ?
        if (!instance.getManager().getWhitelisted().contains(player.getUniqueId())) {
            player.kickPlayer(ChatColor.RED + "Vous n'avez pas la permission de rentrer" +
                    " sur ce serveur.");
        }

        // Get build player object and load respective configuration
        buildPlayer = instance.getManager().getPlayer(player.getUniqueId());
        if (buildPlayer == null) {
            instance.getLog().error("Error durring loading " + player.getName() + " player configuration.");
            player.kickPlayer("§cUne erreur est survenue lors du chargement de votre configuration. " +
                    "Veuillez réessayer.");
            return;
        }

        // Set rank settings
        player.setOp(buildPlayer.getRank().isOp());
        player.setGameMode(buildPlayer.getRank().getGamemode());

        // Set rank display and chat names
        player.setDisplayName(buildPlayer.getRank().getPrefix() + player.getName());
        player.setCustomName(buildPlayer.getRank().getPrefix() + player.getName());
        player.setPlayerListName(buildPlayer.getRank().getPrefix() + player.getName());

        // Allow flight for all users
        player.setAllowFlight(true);
    }

    /**
     * PlayerJoin event
     * When player connect to server
     * @param event PlayerJoin
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BuildPlayer buildPlayer = instance.getManager().getPlayer(player.getUniqueId());

        // BuildPlayer not found ?
        if (buildPlayer == null) {
            event.setJoinMessage("");
            player.kickPlayer("§cUne erreur est survenue lors du chargement de votre configuration. " +
                    "Veuillez réessayer.");
            return;
        }

        // Display join message
        event.setJoinMessage(buildPlayer.getRank().getPrefix() + player.getName() + " §6nous a rejoint.");

        // Set player default configuration and teleportit to spawn location
        player.setMaxHealth(20.0D);
        player.setHealth(20.0D);
        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.2F);
        player.teleport(instance.getServer().getWorld("world").getSpawnLocation());

        // Only for spawn world, superuser and operators can break blocks
        if (instance.getManager().getSuperUsers().contains(player.getUniqueId()) || buildPlayer.getRank().isOp()) {
            player.setGameMode(GameMode.CREATIVE);
        } else {
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

}
