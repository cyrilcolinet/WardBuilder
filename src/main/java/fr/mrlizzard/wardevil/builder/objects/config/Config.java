package fr.mrlizzard.wardevil.builder.objects.config;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Config {

    @Expose private String              serverName;
    @Expose private String              motd;
    @Expose private List<String>        superUsers;
    @Expose private RedisConfig         redis;

    public Config(String serverName, String motd, List<String> superUsers, RedisConfig redis) {
        this.serverName = serverName;
        this.motd = motd;
        this.superUsers = superUsers;
        this.redis = redis;
    }

    public String getServerName() {
        return serverName;
    }

    public String getMotd() {
        return motd;
    }

    public List<String> getSuperUsers() {
        return superUsers;
    }

    public RedisConfig getRedisConfig() {
        return redis;
    }
}
