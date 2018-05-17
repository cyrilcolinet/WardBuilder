package fr.mrlizzard.wardevil.builder.managers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GlobalManager {

    public void sendGlobalMessage(CommandSender sender, List<String> msg) {
        String prefix = "§c(Server)";

        if (sender instanceof Player) {
            Player player = ((Player) sender);

            prefix = player.getName();
        }

        Bukkit.broadcastMessage("[§eGLOBAL§r] " + prefix + ChatColor.RESET + " " + StringUtils.join(msg, " "));
    }

}
