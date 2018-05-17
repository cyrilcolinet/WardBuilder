package fr.mrlizzard.wardevil.builder.objects.config;

import com.google.gson.annotations.Expose;

import java.util.List;

public class WhitelistConfig {

    @Expose
    private List<String>            whitelist;

    public WhitelistConfig(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

}
