package fr.mrlizzard.wardevil.builder.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.util.UUID;

public class WardPlayer {

    private UUID                uuid;
    private String              name;

    public WardPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

}
