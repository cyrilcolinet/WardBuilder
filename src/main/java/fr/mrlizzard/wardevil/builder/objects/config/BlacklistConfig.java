package fr.mrlizzard.wardevil.builder.objects.config;

import java.util.List;

public class BlacklistConfig {

    private List<String>            blacklist;

    public BlacklistConfig(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }
}
