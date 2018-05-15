package fr.mrlizzard.wardevil.builder.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WardPlayer {

    private static Map<UUID, WardPlayer>    players = new HashMap<UUID, WardPlayer>();

    private UUID                            uuid;
    private String                          name;

    public WardPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        players.put(this.uuid, this);
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static Map<UUID, WardPlayer> getPlayers() {
        return players;
    }

}
