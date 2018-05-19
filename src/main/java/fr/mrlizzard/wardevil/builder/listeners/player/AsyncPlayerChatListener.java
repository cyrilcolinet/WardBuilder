package fr.mrlizzard.wardevil.builder.listeners.player;

import fr.mrlizzard.wardevil.builder.managers.GlobalManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class AsyncPlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        String[] split = StringUtils.split(msg, " ");
        GlobalManager glob;

        if (msg.startsWith("!") && split.length >= 1) {
            split[0].substring(1);

            glob = new GlobalManager();
            glob.sendGlobalMessage(player, Arrays.asList(split));
            event.setCancelled(true);
            return;
        }
    }

}
