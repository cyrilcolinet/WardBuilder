package fr.mrlizzard.wardevil.builder.managers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;

import java.io.File;
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

    private BuildPlayer loadPlayerFile(UUID uuid) {
        BuildPlayer buildPlayer;
        String content;
        File playerFile = new File(instance.getDataFolder(), "players/" + uuid.toString() + ".json");

        if (!playerFile.exists()) {
            buildPlayer = new BuildPlayer(instance, uuid);
            return buildPlayer;
        }

        try {
            content = Files.toString(playerFile, Charsets.UTF_8);
            if (content == null || content.equals(""))
                throw new Exception("The file " + uuid.toString() + ".json have a null content.");

            try {
                buildPlayer = instance.getGson().fromJson(content, BuildPlayer.class);
            } catch (Exception err) {
                throw new Exception("The file " + uuid.toString() + ".json is not a valid JSON format.");
            }

            return buildPlayer;
        } catch (Exception except) {
            instance.getLog().error(except.getMessage());
        }

        return null;
    }

    public BuildPlayer getPlayer(UUID uuid) {
        BuildPlayer buildPlayer;

        if (!players.containsKey(uuid)) {
            buildPlayer = loadPlayerFile(uuid);

            players.put(uuid, buildPlayer);
            return buildPlayer;
        }

        return players.get(uuid);
    }

    public List<UUID> getSuperUsers() {
        return superUsers;
    }

    public List<UUID> getWhitelisted() {
        return whitelisted;
    }

}
