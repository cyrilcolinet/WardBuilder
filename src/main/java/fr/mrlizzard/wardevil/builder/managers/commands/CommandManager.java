package fr.mrlizzard.wardevil.builder.managers.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandManager implements CommandExecutor {

    private WardBuilder             instance;
    private List<ACommand>          commands;

    public CommandManager(WardBuilder instance) {
        this.instance = instance;

        this.configureCommands();
        this.instance.getCommand("build").setExecutor(this);
    }

    private void configureCommands() {
        commands.add(new WorldCommand(this.instance, "world"));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            sender.sendMessage("Display help here");
            return true;
        }

        for (ACommand cmd : commands)
            if (cmd.getSubCommand().equalsIgnoreCase(args[0]))
                return cmd.executeCommand(sender, command, args);

        sender.sendMessage("Unknown command");
        return false;
    }
}
