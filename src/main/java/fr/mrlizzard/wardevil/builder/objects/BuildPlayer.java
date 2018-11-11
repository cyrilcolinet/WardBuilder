package fr.mrlizzard.wardevil.builder.objects;

import com.google.gson.annotations.Expose;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * BuildPlayer class
 * @author mrlizzard
 * @version 1.2.3
 */
public class BuildPlayer {

    private WardBuilder             instance;

    private Player                  player;

    @Expose public UUID             uuid;
    @Expose public Rank             rank;

    /**
     * Used to create local player from the redis configuration
     * @param uuid UUID of player
     * @param rank Rank (enum Rank)
     */
    public BuildPlayer(String uuid, String rank) {
        this.instance = WardBuilder.getInstance();
        this.uuid = UUID.fromString(uuid);
        this.player = instance.getServer().getPlayer(uuid);
        this.rank = Rank.valueOf(rank);
    }

    /**
     * Used to create the player if don't exists (database too)
     * @param instance Plugin instanciation
     * @param uuid UUID of player
     */
    public BuildPlayer(WardBuilder instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
        this.player = instance.getServer().getPlayer(uuid);
        this.rank = Rank.SPECTATOR;

        // Create redis player configuration
        Jedis jedis = instance.getConnector().getRessource();

        // Don't process if connector is null or invalid
        if (jedis == null)
            return;

        // Add default values in redis configuration
        jedis.hset("players:" + uuid.toString(), "uuid", uuid.toString());
        jedis.hset("players:" + uuid.toString(), "rank", Rank.SPECTATOR.toString());
        jedis.close();
    }

    /**
     * Get UUID from object
     * @return UUID of player
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get bukkit player object
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get player rank from object
     * @return Rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Save player configuration in redis server
     */
    public void savePlayerConfig() {
        Jedis jedis = instance.getConnector().getRessource();
        String key = "players:" + uuid.toString();

        // Don't save if redis is not connected or invalid (problem)
        // TODO : Save in file and save it when redis connection comes back
        if (jedis == null)
            return;

        jedis.hset(key, "uuid", uuid.toString());
        jedis.hset(key, "rank", rank.toString());
        jedis.close();

        // Delete local player configuration
        instance.getManager().deletePlayer(uuid);
        instance.getLog().info("Data for " + uuid.toString() + " been saved.");
    }

}
