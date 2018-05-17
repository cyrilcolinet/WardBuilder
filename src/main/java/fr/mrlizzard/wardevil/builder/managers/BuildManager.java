package fr.mrlizzard.wardevil.builder.managers;

import fr.mrlizzard.wardevil.builder.WardBuilder;

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
        if (!instance.getConfigManager().loadFiles()) {
            if (instance.getConfigManager().getMissing() != null)
                instance.getLog().error("Config's missing (" + instance.getConfigManager().getMissing() + ").");
            return;
        }

        instance.getConfigManager().getConfig().getSuperUsers().forEach(uuid -> superUsers.add(UUID.fromString(uuid)));
        instance.getConfigManager().getWhitelistConfig().getWhitelist().forEach(uuid -> {
            whitelisted.add(UUID.fromString(uuid));
        });
    }

    public List<UUID> getSuperUsers() {
        return superUsers;
    }

    public List<UUID> getWhitelisted() {
        return whitelisted;
    }

}
