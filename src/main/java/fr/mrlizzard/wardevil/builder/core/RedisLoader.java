package fr.mrlizzard.wardevil.builder.core;

import fr.mrlizzard.wardevil.builder.WardBuilder;

public class RedisLoader {

    private WardBuilder             instance;

    public RedisLoader(WardBuilder instance) {
        this.instance = instance;

        this.initializeConnection();
    }

    private void initializeConnection() {

    }

}
