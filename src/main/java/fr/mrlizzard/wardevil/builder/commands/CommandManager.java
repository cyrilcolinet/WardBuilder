package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {

    private WardBuilder             instance;
    private Map<ACommand, String>   commands;

    public CommandManager(WardBuilder instance) {
        this.instance = instance;
        this.commands = new HashMap<>();

        this.configureCommands();
        this.instance.getCommand("build").setExecutor(this);
    }

    private void configureCommands() {
        commands.put(new WorldCommand(instance, "worlds"), "Gérer les mondes");
        commands.put(new PlayerCommand(instance, "players"), "Gérer les joueurs");
    }

    private void displayHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "--[ " + ChatColor.GOLD + "WardBuilder | Help " + ChatColor.RED + "]--");
        sender.sendMessage(ChatColor.YELLOW + " /build help   \t" + ChatColor.WHITE + "- " + ChatColor.GOLD +
                "Afficher la page d'aide (cette page)");

        commands.forEach((key, value) -> {
            String cmd = ChatColor.YELLOW + " /build " + key.getSubCommand();
            String desc = ChatColor.WHITE + "- " + ChatColor.GOLD + value;

            sender.sendMessage(cmd + ChatColor.RESET + "   \t" + desc + ChatColor.RESET);
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0 || args[0].equalsIgnoreCase("help")) {
            this.displayHelp(sender);
            return true;
        }

        for (Map.Entry<ACommand, String> entry : commands.entrySet()) {
            if (entry.getKey().getSubCommand().equalsIgnoreCase(args[0]))
                return entry.getKey().executeCommand(sender, command, args);
        }

        sender.sendMessage("Unknown command");
        return false;
    }
}
