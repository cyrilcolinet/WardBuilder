package fr.mrlizzard.wardevil.builder.objects.config;

import java.util.List;

public class Config {

    private String              serverName;
    private String              motd;
    private List<String>        superUsers;

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
