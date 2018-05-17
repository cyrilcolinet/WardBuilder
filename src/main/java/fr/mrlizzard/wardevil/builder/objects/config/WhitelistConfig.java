package fr.mrlizzard.wardevil.builder.objects.config;

import java.util.List;

public class WhitelistConfig {

    private List<String>            whitelist;

    public WhitelistConfig(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

}
