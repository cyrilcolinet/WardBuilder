package fr.mrlizzard.wardevil.builder.objects.config;

import java.util.List;

public class WhitelistConfig {

    private List<String>            whitlist;

    public WhitelistConfig(List<String> whitelist) {
        this.whitlist = whitelist;
    }

    public List<String> getWhitlist() {
        return whitlist;
    }

}
