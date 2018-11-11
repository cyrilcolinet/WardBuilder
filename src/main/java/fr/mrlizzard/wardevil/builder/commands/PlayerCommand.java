package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import fr.mrlizzard.wardevil.builder.uitls.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * PlayerCommand class
 * @author mrlizzard
 * @version 1.2.9
 */
public class PlayerCommand extends ACommand {

    /**
     * PlayerCommand constructor.
     * @param instance WardBuilder instance
     * @param subCommand String subcommand
     */
    public PlayerCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand, "Gestion des joueurs");
    }

    /**
     * Load subcommands for this command
     * SubCommands
     *  - promote
     *  - whitelist
     *  - info
     */
    @Override
    public void loadSubCommands() {
        subCommands.put("promote", () -> promotePlayer());
        subCommands.put("whitelist", () -> whitelistPlayer());
        subCommands.put("info", () -> infoPlayer());
    }

    /**
     * Display help page for this subcommand
     */
    @Override
    public void displayHelp() {
        sender.sendMessage("§c--[ §6WardBuilder | Players Help §c]--");
        sender.sendMessage("§e /build players help §f- §6Afficher la page d'aide");
        sender.sendMessage("§e /build players promote <player> <rank> §f- §6Promouvoir");
        sender.sendMessage("§e /build players whitelist add <player> §f- §6Whitelister");
        sender.sendMessage("§e /build players whitelist del <player> §f- §6Dé-Whitelister");
        sender.sendMessage("§e /build players info <player> §f- §6Voir les infos");
    }

    /**
     * Promote player command
     */
    private void promotePlayer() {
        Rank rank;
        UUID uuid;
        BuildPlayer current;

        // Not enough arguments
        if (args.length < 4) {
            sender.sendMessage("§cUsage: /build players promote <player> <rank>");
            return;
        }

        // Try to get rank enum
        try {
            rank = Rank.valueOf(StringUtils.upperCase(args[3]));
        } catch (Exception nullPtr) {
            sender.sendMessage("§cAucun grade nommé " + args[3] + " trouvé.");

            // Get the list of ranks and display it
            List<Rank> ranks = new ArrayList<>(Arrays.asList(Rank.values()));
            String joinedRanks = StringUtils.join(ranks, ", ");

            // Send message to current user
            sender.sendMessage("§cVoici la list des grades possibles: " + joinedRanks + ".");
            return;
        }

        // If command sender is a player
        if (sender instanceof Player) {
            current = instance.getManager().getPlayer(((Player) sender).getUniqueId());

            // Impossible to set a rank grater than yours !
            if (current.getRank().getId() < rank.getId()) {
                sender.sendMessage("§cVous ne pouvez pas assigner de grade supérieur au votre.");
                return;
            }
        }

        // Check if user exists in Mojang database
        uuid = instance.getUuidTranslator().getUUID(args[2], true);

        // User is crack or not exists in Mojang database (only crack used in tis server)
        if (uuid == null) {
            sender.sendMessage("§cAucun joueur nommé " + args[2] + " trouvé.");
            return;
        }

        // If superuser rank removed from this user, remove her permissions
        if (rank.getId() < Rank.SUPER_USER.getId())
            instance.getManager().getSuperUsers().remove(uuid);

        // Change player rank parameter
        if (instance.getManager().changePlayerParam(uuid, "rank", rank.toString())) {
            sender.sendMessage("§a" + args[2] + " a bien été promu " + StringUtils.capitalize(args[3].toLowerCase()));
        } else {
            sender.sendMessage("§cUne erreur est survenue lors de l'assignation du grade à §e" + args[2] + "§c.");
        }
    }

    /**
     * Whitelist command
     */
    private void whitelistPlayer() {
        sender.sendMessage("§eEn développement :)");
    }

    /**
     * Get players informations
     */
    private void infoPlayer() {
        // Not enought arguments
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /build players info <player>");
            return;
        }

        // Check if user exists in Mojang databases
        UUID uuid = instance.getUuidTranslator().getUUID(args[2], true);

        // user does not exists, or is cracked (only premium is allowed here)
        if (uuid == null) {
            sender.sendMessage("§cAucun joueur nommé " + args[2] + " trouvé.");
            return;
        }

        // Check if user exists in database
        Jedis jedis = instance.getConnector().getRessource();

        // No data for this user exists in database
        if (!jedis.exists("players:" + uuid)) {
            sender.sendMessage("§cAucune données présentes concernant " + args[2]);
            return;
        }

        // Get all values from the user database and close connection
        Map<String, String> values = jedis.hgetAll("players:" + uuid);
        jedis.close();

        sender.sendMessage("§aVoici les données concernant " + args[2]);
        values.forEach((key, value) -> sender.sendMessage("  §e" + key + " §c-> §b" + value));
        sender.sendMessage("  §eSuperUser §c-> "
                + ((instance.getManager().getSuperUsers().contains(uuid)) ? "§aoui" : "§cnon"));
    }

    /**
     * Execute command function
     * @return Boolean
     */
    @Override
    public boolean executeCommand() {
        for (Map.Entry<String, Runnable> entry : subCommands.entrySet()) {
            if (args[1].equalsIgnoreCase(entry.getKey())) {
                // Check permission to execute command
                if (!sender.hasPermission("wardbuilder.players.command")) {
                    sender.sendMessage("§cVous n'avez pas la permission d'intéragir avec les joueurs.");
                    return true;
                }

                // Permission accored, run subcommand function
                entry.getValue().run();
                return true;
            }
        }

        // Unknowned command for player subcommand
        sender.sendMessage("§cCommande inconnue. Taper /build players pour voir l'aide.");
        return true;
    }
}
