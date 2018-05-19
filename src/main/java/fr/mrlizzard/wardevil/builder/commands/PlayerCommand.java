package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;

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
        subCommands.put("info", () -> infoPlayer());
    }

    @Override
    public void displayHelp() {
        sender.sendMessage("§c--[ §6WardBuilder | Players Help §c]--");
        sender.sendMessage("§e /build players help §f- §6Afficher la page d'aide");
        sender.sendMessage("§e /build players promote <player> <rank> §f- §6Promouvoir");
        sender.sendMessage("§e /build players whitelist add <player> §f- §6Whitelister");
        sender.sendMessage("§e /build players whitelist del <player> §f- §6Dé-Whitelister");
        sender.sendMessage("§e /build players info <player> §f- §6Voir les infos");
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

    private void infoPlayer() {
        UUID uuid;
        Jedis jedis;
        Map<String, String> values;

        if (args.length < 3) {
            sender.sendMessage("§cUsage: /build players info <player>");
            return;
        }

        uuid = instance.getUuidTranslator().getUUID(args[2], true);
        if (uuid == null) {
            sender.sendMessage("§cAucun joueur nommé " + args[2] + " trouvé.");
            return;
        }

        jedis = instance.getConnector().getRessource();

        if (!jedis.exists("players:" + uuid)) {
            sender.sendMessage("§cAucune données présentes concernant " + args[2]);
            return;
        }

        values = jedis.hgetAll("players:" + uuid);
        sender.sendMessage("§aVoici les données concernant " + args[2]);
        values.forEach((key, value) -> sender.sendMessage("  §e" + key + " §c-> §b" + value));
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
