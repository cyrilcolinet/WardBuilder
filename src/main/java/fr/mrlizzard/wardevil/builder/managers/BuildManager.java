package fr.mrlizzard.wardevil.builder.managers;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.config.Config;

public class BuildManager {

    private WardBuilder                 instance;

    public BuildManager(WardBuilder instance) {
        this.instance = instance;

        this.loadServerConfiguration();
    }

    private void loadServerConfiguration() {
        Config config = instance.getConfigManager().getConfig();


    }

}
