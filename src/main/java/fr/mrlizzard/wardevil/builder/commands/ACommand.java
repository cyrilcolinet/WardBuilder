package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Map;

public abstract class ACommand {

    private WardBuilder             instance;
    private String                  subCommand;

    private CommandSender            sender;
    private Command                  command;
    private String[]                 args;

    ACommand(WardBuilder instance, String subCommand) {
        this.instance = instance;
        this.subCommand = subCommand;

        this.instance.getLog().info("Loading " + subCommand + " subcommand...");
    }

    public void setValues(CommandSender sender, Command command, String[] args) {
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public void displayHelp(Map<Map<String, String>, Boolean> subCommands) {
        sender.sendMessage(ChatColor.RED + "--[ " + ChatColor.GOLD + "WardBuilder | " +
                StringUtils.capitalize(subCommand) + " Help " + ChatColor.RED + "]--");
        sender.sendMessage(ChatColor.YELLOW + " /build " + subCommand + " help   \t" + ChatColor.WHITE + "- " +
                ChatColor.GOLD + "Afficher la page d'aide (cette page)");

        subCommands.keySet().forEach(key -> {
            key.forEach((entryCmd, entryDesc) -> {
                String cmd = ChatColor.YELLOW + " /build " + subCommand + " " + entryCmd;
                String desc = ChatColor.WHITE + "- " + ChatColor.GOLD + entryDesc;

                sender.sendMessage(cmd + ChatColor.RESET + "   \t" + desc + ChatColor.RESET);
            });
        });
    }

    public abstract boolean executeCommand(CommandSender sender, Command command, String[] args);

    String getSubCommand() {
        return subCommand;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Command getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

}
