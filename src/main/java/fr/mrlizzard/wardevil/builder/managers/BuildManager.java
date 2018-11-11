package fr.mrlizzard.wardevil.builder.managers;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.*;

/**
 * BuildManager class
 * @author mrlizzard
 * @version 1.0.0
 */
public class BuildManager {

    private WardBuilder                 instance;

    private List<UUID>                  superUsers;
    private List<UUID>                  whitelisted;
    private Map<UUID, BuildPlayer>      players;

    /**
     * Manage build server and all instanciations (player, etc...)
     * @param instance WardBuild instance
     */
    public BuildManager(WardBuilder instance) {
        this.instance = instance;
        this.superUsers = new ArrayList<>();
        this.whitelisted = new ArrayList<>();
        this.players = new HashMap<>();

        // Load server configuration
        this.loadServerConfiguration();
    }

    /**
     * Load server configuration and create it
     * if not created
     */
    private void loadServerConfiguration() {
        // If one of file is not found
        if (!instance.getConfigManager().loadFiles()) {
            String missingFile = instance.getConfigManager().getMissing();

            // If file missing, print error in console and stop process loading
            if (missingFile != null)
                instance.getLog().error("Config's missing (" + missingFile + ").");
            return;
        }

        // Load configuration
        ConfigManager configManager = instance.getConfigManager();

        configManager.getConfig().getSuperUsers().forEach(uuid -> superUsers.add(UUID.fromString(uuid)));
        configManager.getWhitelistConfig().getWhitelist().forEach(uuid -> whitelisted.add(UUID.fromString(uuid)));
    }

    /**
     * Load player configuration from redis server
     * @param uuid UUID of player
     * @return BuildPlayer Buildplayer object
     */
    private BuildPlayer loadPlayerConfig(UUID uuid) {
        Jedis jedis = instance.getConnector().getRessource();
        String key = "players:" + uuid.toString();
        String uuidStr;
        String rankStr;
        BuildPlayer buildPlayer;

        // If player don't exists in database, create it by default
        if (!jedis.exists(key)) {
            buildPlayer = new BuildPlayer(instance, uuid);
            return buildPlayer;
        }

        uuidStr = ((jedis.hexists(key, "uuid")) ? jedis.hget(key, "uuid") : uuid.toString());
        rankStr = ((jedis.hexists(key, "rank")) ? jedis.hget(key, "rank") : "SPECTATOR");

        // Get build player from UUID and RANK
        return new BuildPlayer(uuidStr, rankStr);
    }

    /**
     * Get the build player
     * @param uuid UUID of player
     * @return Object Player object
     */
    public BuildPlayer getPlayer(UUID uuid) {
        BuildPlayer buildPlayer;

        // If build player doesn't exists, create it from config
        if (!players.containsKey(uuid)) {
            buildPlayer = this.loadPlayerConfig(uuid);

            // Add to current logged players list
            players.put(uuid, buildPlayer);
            return buildPlayer;
        }

        // Get the player and return it
        return players.get(uuid);
    }

    /**
     * Delete player from local list
     * @param uuid UUID of player
     */
    public void deletePlayer(UUID uuid) {
        players.remove(uuid);
    }

    /**
     * Change player parameter in database and local object
     * @param uuid UUID of player
     * @param key Key of new value
     * @param value Value of the new key
     * @return Boolean Success or not success
     */
    public Boolean changePlayerParam(UUID uuid, String key, String value) {
        Jedis jedis = instance.getConnector().getRessource();
        String redisKey = "players:" + uuid.toString();

        // Check if redis has a valid connector
        if (jedis == null) {
            instance.getLog().error("Jedis have not valid connector.");
            return false;
        }

        // Set new value in database for this user
        jedis.hset(redisKey, key, value);
        jedis.close();

        // Log change in console
        instance.getLog().info("Info for " + uuid + " changed (" + key + " -> " + value + ").");
        Player player = instance.getServer().getPlayer(uuid);

        // If player is conncted, send message that inform player of changement
        if (player != null) {
            Rank rank;
            BuildPlayer buildPlayer;

            player.sendMessage("§aUne valeur a été changée: §b" + key + " §e-> §c" + value);

            // If the rank has changed, immediat change
            if (key.equalsIgnoreCase("rank")) {
                rank = Rank.valueOf(value);

                // Set settings for rank
                player.setOp(rank.isOp());
                player.setGameMode(rank.getGamemode());

                // Set names
                player.setDisplayName(rank.getPrefix() + player.getName());
                player.setCustomName(rank.getPrefix() + player.getName());
                player.setPlayerListName(rank.getPrefix() + player.getName());
            }

            // Change field in build player object
            try {
                Field field = BuildPlayer.class.getField(key);
                buildPlayer = this.getPlayer(uuid);

                // In cas of rank key changed
                if (key.equalsIgnoreCase("rank"))
                    field.set(buildPlayer, Rank.valueOf(value));
            } catch (Exception err) {
                instance.getLog().error("Unable to set object field : " + err.getMessage());
            }
        }

        return true;
    }

    public List<UUID> getSuperUsers() {
        return superUsers;
    }

    public List<UUID> getWhitelisted() {
        return whitelisted;
    }

    public Map<UUID, BuildPlayer> getPlayers() {
        return players;
    }
}
