package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class ACommand {

    private WardBuilder             instance;
    private String                  subCommand;

    ACommand(WardBuilder instance, String subCommand) {
        this.instance = instance;
        this.subCommand = subCommand;

        this.instance.getLog().info("Loading sub command \"" + subCommand + "\"...");
    }

    public abstract void displayHelp(CommandSender sender);

    public abstract boolean executeCommand(CommandSender sender, Command command, String[] args);

    String getSubCommand() {
        return subCommand;
    }

}
