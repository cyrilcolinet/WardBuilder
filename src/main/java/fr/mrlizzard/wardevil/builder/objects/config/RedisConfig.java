package fr.mrlizzard.wardevil.builder.objects.config;

import com.google.gson.annotations.Expose;

public class RedisConfig {

    @Expose private String              host;
    @Expose private Integer             port;
    @Expose private Integer             database;
    @Expose private String              auth;

    public RedisConfig(String host, Integer port, Integer database, String auth) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.auth = auth;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getDatabase() {
        return database;
    }

    public String getAuth() {
        return auth;
    }

}
