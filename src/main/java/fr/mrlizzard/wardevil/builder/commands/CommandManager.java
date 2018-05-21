package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private WardBuilder                 instance;
    private List<ACommand>              commands;

    public CommandManager(WardBuilder instance) {
        this.instance = instance;
        this.commands = new ArrayList<>();

        this.configureCommands();
        this.instance.getCommand("build").setExecutor(this);
    }

    private void configureCommands() {
        commands.add(new WorldCommand(instance, "worlds"));
        commands.add(new PlayerCommand(instance, "players"));
        commands.add(new GlobalCommand(instance, "global"));
    }

    private void displayHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "--[ " + ChatColor.GOLD + "WardBuilder | Help " + ChatColor.RED + "]--");
        sender.sendMessage(ChatColor.YELLOW + " /build help   \t\t" + ChatColor.WHITE + "- " + ChatColor.GOLD +
                "Afficher la page d'aide (cette page)");

        commands.forEach(obj -> {
            String cmd = ChatColor.YELLOW + " /build " + obj.getSubCommand();
            String desc = ChatColor.WHITE + "- " + ChatColor.GOLD + obj.getDescription();

            sender.sendMessage(cmd + ChatColor.RESET + "   \t\t" + desc + ChatColor.RESET);
        });
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0 || args[0].equalsIgnoreCase("help")) {
            this.displayHelp(sender);
            return true;
        }

        for (ACommand cmd : commands) {
            if (cmd.getSubCommand().equalsIgnoreCase(args[0]))
                return cmd.runCommand(sender, command, args);
        }

        sender.sendMessage("§cCommande inconnue. Taper /build help pour voir l'aide.");
        return true;
    }
}
