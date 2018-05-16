package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
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
        commands.put(new WorldCommand(this.instance, "world"), "Gérer les mondes");
    }

    private void displayHelp(CommandSender sender) {
        sender.sendMessage("--[ WardBuilder | Help ]--");
        sender.sendMessage(" /build help\t\t- Afficher la page d'aide");

        commands.entrySet().forEach(entry -> {
            sender.sendMessage(" " + entry.getKey().getCommand() + "\t\t" + entry.getValue());
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
