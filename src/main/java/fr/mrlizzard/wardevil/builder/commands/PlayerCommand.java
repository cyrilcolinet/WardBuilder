package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerCommand extends ACommand {

    public PlayerCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand, "Gestion des joueurs");
    }

    @Override
    public void loadSubCommands() {
        subCommands.put("promote", () -> promotePlayer());
    }

    @Override
    public void displayHelp() {
        sender.sendMessage("§c--[ §6WardBuilder | Players Help §c]--");
        sender.sendMessage("§e /build players help   \t\t\t§f- §6Afficher la page d'aide (cette page)");

        sender.sendMessage("§e /build players promote <player> <rank>\t§f- §6Promouvoir un joueur");
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
        } catch (Exception nullPtr) {
            sender.sendMessage("§cAucun grade nommé " + args[3] + " trouvé.");
            return true;
        }

        buildPlayer = instance.getManager().getPlayer(player.getUniqueId());
        buildPlayer.editRank(rank);
        sender.sendMessage("§a" + player.getName() + " a bien été promu " + args[3]);
        return true;
    }

    @Override
    public boolean executeCommand() {
        for (Map.Entry<String, Runnable> entry : subCommands.entrySet()) {
            if (args[1].equalsIgnoreCase(entry.getKey())) {
                entry.getValue().run();
                return true;
            }
        }

        sender.sendMessage("§cCommande inconnue. Taper /build players pour voir l'aide.");
        return true;
    }
}
