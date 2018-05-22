package fr.mrlizzard.wardevil.builder.managers;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.*;

public class BuildManager {

    private WardBuilder                 instance;

    private List<UUID>                  superUsers;
    private List<UUID>                  whitelisted;
    private Map<UUID, BuildPlayer>      players;

    public BuildManager(WardBuilder instance) {
        this.instance = instance;
        this.superUsers = new ArrayList<>();
        this.whitelisted = new ArrayList<>();
        this.players = new HashMap<>();

        this.loadServerConfiguration();
    }

    private void loadServerConfiguration() {
        if (!instance.getConfigManager().loadFiles()) {
            if (instance.getConfigManager().getMissing() != null)
                instance.getLog().error("Config's missing (" + instance.getConfigManager().getMissing() + ").");
            return;
        }

        instance.getConfigManager().getConfig().getSuperUsers().forEach(uuid -> superUsers.add(UUID.fromString(uuid)));
        instance.getConfigManager().getWhitelistConfig().getWhitelist().forEach(uuid -> {
            whitelisted.add(UUID.fromString(uuid));
        });
    }

    private BuildPlayer loadPlayerConfig(UUID uuid) {
        Jedis jedis = instance.getConnector().getRessource();
        String key = "players:" + uuid.toString();
        String uuidStr;
        String rankStr;
        BuildPlayer buildPlayer;

        if (!jedis.exists(key)) {
            buildPlayer = new BuildPlayer(instance, uuid);
            return buildPlayer;
        }

        uuidStr = ((jedis.hexists(key, "uuid")) ? jedis.hget(key, "uuid") : uuid.toString());
        rankStr = ((jedis.hexists(key, "rank")) ? jedis.hget(key, "rank") : "SPECTATOR");
        return new BuildPlayer(uuidStr, rankStr);
    }

    public BuildPlayer getPlayer(UUID uuid) {
        BuildPlayer buildPlayer;

        if (!players.containsKey(uuid)) {
            buildPlayer = loadPlayerConfig(uuid);

            players.put(uuid, buildPlayer);
            return buildPlayer;
        }

        return players.get(uuid);
    }

    public void deletePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public void changePlayerParam(UUID uuid, String key, String value) {
        Jedis jedis = instance.getConnector().getRessource();
        String redisKey = "players:" + uuid.toString();
        Player player = instance.getServer().getPlayer(uuid);

        jedis.hset(redisKey, key, value);
        if (!jedis.hexists(redisKey, "uuid"))
            jedis.hset(redisKey, "uuid", uuid.toString());
        jedis.close();
        instance.getLog().info("Info for " + uuid + " changed (" + key + " -> " + value + ").");

        if (player != null) {
            Rank rank;
            BuildPlayer buildPlayer;

            player.sendMessage("§aUne valeur a été changée: " + key + " -> " + value);
            if (key.equalsIgnoreCase("rank")) {
                rank = Rank.valueOf(value);
                if (!rank.isOp())
                    player.setOp(false);

                player.setDisplayName(rank.getPrefix() + player.getName());
                player.setCustomName(rank.getPrefix() + player.getName());
                player.setPlayerListName(rank.getPrefix() + player.getName());
            }

            try {
                Field field = BuildPlayer.class.getField(key);
                buildPlayer = this.getPlayer(uuid);

                if (key.equalsIgnoreCase("rank"))
                    field.set(buildPlayer, Rank.valueOf(value));
            } catch (Exception err) {
                instance.getLog().error("Unable to set object field : " + err.getMessage());
            }
        }
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
