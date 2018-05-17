package fr.mrlizzard.wardevil.builder.objects;

import java.util.UUID;

public class WardPlayer {

    private UUID                            uuid;

    public WardPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

}
