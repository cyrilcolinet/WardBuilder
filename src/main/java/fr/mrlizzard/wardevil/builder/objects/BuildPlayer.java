package fr.mrlizzard.wardevil.builder.objects;

import com.google.gson.annotations.Expose;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.bukkit.entity.Player;

import java.io.File;
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

    public void editRank(Rank rank) {
        this.rank = rank;
    }

    public void savePlayerConfig() {
        File playerFile = new File(instance.getDataFolder(), "players/" + uuid.toString() + ".json");
        FileWriter writer;
        String content;

        try {
            content = instance.getGson().toJson(this);
            instance.getLog().warning(content);
        } catch (Exception exception) {
            instance.getLog().error(exception.getMessage());
            return;
        }

        if (!playerFile.exists()) {
            try {
                writer = new FileWriter(instance.getDataFolder() + "/players/" + uuid.toString() + ".json");

                try {
                    writer.write(content);
                } catch (Exception err) {
                    throw new Exception("File " + uuid.toString() + ".json isn't a valid json file.");
                }
                writer.close();
            } catch (Exception except) {
                instance.getLog().error(except.getMessage());
            }

            return;
        }


    }

}
