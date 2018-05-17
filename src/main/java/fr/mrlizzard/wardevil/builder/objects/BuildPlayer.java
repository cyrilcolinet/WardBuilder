package fr.mrlizzard.wardevil.builder.objects;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BuildPlayer {

    private WardBuilder         instance;

    private UUID                uuid;
    private Player              player;
    private Rank                rank;

    public BuildPlayer(String uuid, String rank) {
        this.instance = WardBuilder.getInstance();
        this.uuid = UUID.fromString(uuid);
        this.player = instance.getServer().getPlayer(uuid);
        this.rank = Rank.valueOf(rank);
    }

    public BuildPlayer(WardBuilder instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return player;
    }
    
}
