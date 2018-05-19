package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.UUID;

public class PlayerCommand extends ACommand {

    public PlayerCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand, "Gestion des joueurs");
    }

    @Override
    public void loadSubCommands() {
        subCommands.put("promote", () -> promotePlayer());
        subCommands.put("whitelist", () -> whitelistPlayer());
    }

    @Override
    public void displayHelp() {
        sender.sendMessage("§c--[ §6WardBuilder | Players Help §c]--");
        sender.sendMessage("§e /build players help §f- §6Afficher la page d'aide (cette page)");
        sender.sendMessage("§e /build players promote <player> <rank> §f- §6Promouvoir un joueur");
        sender.sendMessage("§e /build players whitelist add <player> §f- §6Whitelister un joueur");
        sender.sendMessage("§e /build players whitelist del <player> §f- §6Dé-Whitelister un joueur");
    }

    private void promotePlayer() {
        Rank rank;
        UUID uuid;

        if (args.length < 4) {
            sender.sendMessage("§cUsage: /build players promote <player> <rank>");
            return;
        }

        try {
            rank = Rank.valueOf(StringUtils.upperCase(args[3]));
        } catch (Exception nullPtr) {
            sender.sendMessage("§cAucun grade nommé " + args[3] + " trouvé.");
            return;
        }

        uuid = instance.getUuidTranslator().getUUID(args[2], true);
        if (uuid == null) {
            sender.sendMessage("§cAucun joueur nommé " + args[2] + " trouvé.");
            return;
        }

        instance.getManager().changePlayerParam(uuid, "rank", rank.toString());
        sender.sendMessage("§a" + args[2] + " a bien été promu " + StringUtils.capitalize(args[3].toLowerCase()));
    }

    private void whitelistPlayer() {
        sender.sendMessage("cc");
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
