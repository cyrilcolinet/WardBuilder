package fr.mrlizzard.wardevil.builder.objects;

import com.google.gson.annotations.Expose;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.bukkit.entity.Player;

import java.io.FileWriter;
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

    public void savePlayerConfig() {
        FileWriter writer;

        try {
            writer = new FileWriter(instance.getDataFolder() + "/players/" + uuid.toString() + ".json");

            try {
                instance.getGson().toJson(this, writer);
            } catch (Exception err) {
                throw new Exception("File " + uuid.toString() + ".json isn't a valid json file.");
            }
        } catch (Exception except) {
            instance.getLog().error(except.getMessage());
        }
    }

}
