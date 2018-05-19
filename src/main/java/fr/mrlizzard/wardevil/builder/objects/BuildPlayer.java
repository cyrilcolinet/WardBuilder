package fr.mrlizzard.wardevil.builder.objects;

import com.google.gson.annotations.Expose;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class BuildPlayer {

    private WardBuilder             instance;

    private Player                  player;

    @Expose private UUID            uuid;
    @Expose private Rank            rank;

    public BuildPlayer(String uuid, String rank) {
        this.instance = WardBuilder.getInstance();
        this.uuid = UUID.fromString(uuid);
        this.player = instance.getServer().getPlayer(uuid);
        this.rank = Rank.valueOf(rank);
    }

    public BuildPlayer(WardBuilder instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
        this.player = instance.getServer().getPlayer(uuid);
        this.rank = Rank.SPECTATOR;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }

    public void editRank(Rank rank) {
        this.rank = rank;
    }

    public void savePlayerConfig() {
        Jedis jedis = instance.getConnector().getRessource();
        String key = "players:" + uuid.toString();

        jedis.hset(key, "uuid", uuid.toString());
        jedis.hset(key, "rank", rank.toString());
        jedis.close();
        instance.getManager().deletePlayer(uuid);
        instance.getLog().info("Data for " + uuid.toString() + " been saved.");
    }

}
