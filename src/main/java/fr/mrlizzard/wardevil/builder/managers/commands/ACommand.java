package fr.mrlizzard.wardevil.builder.managers.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class ACommand {

    private WardBuilder             instance;
    private String                  subCommand;

    public ACommand(WardBuilder instance, String subCommand) {
        this.instance = instance;
        this.subCommand = subCommand;
    }

    public abstract boolean executeCommand(CommandSender sender, Command command, String[] args);

    public String getSubCommand() {
        return subCommand;
    }

}
