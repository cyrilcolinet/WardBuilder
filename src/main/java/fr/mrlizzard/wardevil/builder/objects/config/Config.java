package fr.mrlizzard.wardevil.builder.objects.config;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Config {

    @Expose private String              serverName;
    @Expose private String              motd;
    @Expose private List<String>        superUsers;

    public Config(String serverName, String motd, List<String> superUsers) {
        this.serverName = serverName;
        this.motd = motd;
        this.superUsers = superUsers;
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

}
