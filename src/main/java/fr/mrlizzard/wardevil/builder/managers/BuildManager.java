package fr.mrlizzard.wardevil.builder.managers;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.Config;
import fr.mrlizzard.wardevil.builder.objects.config.WhitelistConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildManager {

    private WardBuilder                 instance;

    private List<UUID>                  superUsers;
    private List<UUID>                  whitelisted;

    public BuildManager(WardBuilder instance) {
        this.instance = instance;
        this.superUsers = new ArrayList<>();
        this.whitelisted = new ArrayList<>();

        this.loadServerConfiguration();
    }

    private void loadServerConfiguration() {
        Config config = instance.getConfigManager().getConfig();
        WhitelistConfig whitelistConfig = instance.getConfigManager().getWhitelistConfig();

        config.getSuperUsers().forEach(uuid -> superUsers.add(UUID.fromString(uuid)));
        whitelistConfig.getWhitelist().forEach(uuid -> whitelisted.add(UUID.fromString(uuid)));
    }

    public List<UUID> getSuperUsers() {
        return superUsers;
    }

    public List<UUID> getWhitelisted() {
        return whitelisted;
    }

}
