package fr.mrlizzard.wardevil.builder.uitls;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.ChatColor;

public class Logger {

    private WardBuilder             instance;
    private String                  prefix;

    public Logger(WardBuilder instance) {
        this.instance = instance;
        this.prefix = "[" + this.instance.getDescription().getName() + "]";
    }

    public void info(String msg) {
        instance.getServer().getConsoleSender().sendMessage(prefix + msg);
    }

    public void warning(String msg) {
        msg = ChatColor.GOLD + msg + ChatColor.RESET;
        instance.getServer().getConsoleSender().sendMessage(prefix + msg);
    }

    public void error(String msg) {
        msg = ChatColor.RED + msg + ChatColor.RESET;
        instance.getServer().getConsoleSender().sendMessage(prefix + msg);
    }

}
