package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerCommand extends ACommand {

    public PlayerCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand);
    }

    @Override
    public void loadSubCommands() {
        Map<String, String> desc = new HashMap<>();

        desc.put("promote", "Promouvoir un joueur");
        subCommands.put(desc, promotePlayer());
    }

    private boolean promotePlayer() {
        Rank rank;
        Player player;
        BuildPlayer buildPlayer;

        if (args.length < 4) {
            sender.sendMessage("§cUsage: /build players promote <player> <rank>");
            return true;
        }

        player = instance.getServer().getPlayer(args[2]);
        if (player == null) {
            sender.sendMessage("§cAucun joueur nommé " + args[2] + " trouvé.");
            return true;
        }

        try {
            rank = Rank.valueOf(StringUtils.upperCase(args[3]));
        } catch (NullPointerException nullPtr) {
            sender.sendMessage("§cAucun grade nommé " + args[3] + " trouvé.");
            return true;
        }

        buildPlayer = instance.getManager().getPlayer(player.getUniqueId());
        buildPlayer.editRank(rank);
        return true;
    }

}
