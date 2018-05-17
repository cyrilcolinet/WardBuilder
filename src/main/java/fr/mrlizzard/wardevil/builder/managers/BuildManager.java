package fr.mrlizzard.wardevil.builder.managers;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildManager {

    private WardBuilder                 instance;
    private List<UUID>                  superUsers;

    public BuildManager(WardBuilder instance) {
        this.instance = instance;
        this.superUsers = new ArrayList<>();

        this.loadServerConfiguration();
    }

    private void loadServerConfiguration() {
        Config config = instance.getConfigManager().getConfig();

        config.getSuperUsers().forEach(uuid -> superUsers.add(UUID.fromString(uuid)));
    }

    public List<UUID> getSuperUsers() {
        return superUsers;
    }
}
