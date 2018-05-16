package fr.mrlizzard.wardevil.builder.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WardPlayer {

    private static Map<UUID, WardPlayer>    players = new HashMap<UUID, WardPlayer>();

    private UUID                            uuid;

    public WardPlayer(UUID uuid) {
        this.uuid = uuid;

        players.put(this.uuid, this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public static Map<UUID, WardPlayer> getPlayers() {
        return players;
    }

    public static WardPlayer getPlayer(UUID uuid) {
        WardPlayer wardPlayer = null;

        if (players.containsKey(uuid))
            return players.get(uuid);

        new WardPlayer(uuid);
        wardPlayer = players.get(uuid);

        return wardPlayer;
    }

}
