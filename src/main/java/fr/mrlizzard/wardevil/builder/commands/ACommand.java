package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public abstract class ACommand {

    public WardBuilder                          instance;
    public String                               subCommand;
    public String                               description;

    public CommandSender                        sender;
    public Command                              command;
    public String[]                             args;

    public Map<String, Runnable>                subCommands;

    ACommand(WardBuilder instance, String subCommand, String description) {
        this.instance = instance;
        this.subCommand = subCommand;
        this.description = description;
        this.subCommands = new HashMap<>();

        this.instance.getLog().info("Loading /build " + subCommand + " command...");
    }

    public void setValues(CommandSender sender, Command command, String[] args) {
        this.sender = sender;
        this.command = command;
        this.args = args;

        this.loadSubCommands();
    }

    public abstract void displayHelp();

    public abstract void loadSubCommands();

    public boolean runCommand(CommandSender sender, Command cmd, String[] args) {
        try {
            this.setValues(sender, cmd, args);

            if (args.length <= 1 || args[1].equalsIgnoreCase("help")) {
                this.displayHelp();
                return true;
            }

            return this.executeCommand();
        } catch(Exception err) {
            sender.sendMessage("§cInternal error: " + err.toString() + " (see logs in console).");
            err.printStackTrace();
        }

        return true;
    }

    public abstract boolean executeCommand();

    String getSubCommand() {
        return subCommand;
    }

    String getDescription() {
        return description;
    }
}
