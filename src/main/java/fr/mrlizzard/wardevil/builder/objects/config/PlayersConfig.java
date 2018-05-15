package fr.mrlizzard.wardevil.builder.objects.config;

import fr.mrlizzard.wardevil.builder.objects.WardPlayer;

import java.util.List;

public class PlayersConfig {

    private List<WardPlayer>                players;

    public PlayersConfig(List<WardPlayer> players) {
        this.players = players;
    }

    public List<WardPlayer> getPlayers() {
        return players;
    }

}
