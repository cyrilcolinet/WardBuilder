package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public abstract class ACommand {

    public WardBuilder                          instance;
    public String                               subCommand;

    public CommandSender                        sender;
    public Command                              command;
    public String[]                             args;

    public Map<Map<String, String>, Boolean>    subCommands;

    ACommand(WardBuilder instance, String subCommand) {
        this.instance = instance;
        this.subCommand = subCommand;
        this.subCommands = new HashMap<>();

        this.instance.getLog().info("Loading /build " + subCommand + " command...");
    }

    public void setValues(CommandSender sender, Command command, String[] args) {
        this.sender = sender;
        this.command = command;
        this.args = args;

        this.loadSubCommands();
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

    public abstract void loadSubCommands();

    public boolean executeCommand(CommandSender sender, Command command, String[] args) {
        this.setValues(sender, command, args);

        if (args.length <= 1 || args[1].equalsIgnoreCase("help")) {
            this.displayHelp(subCommands);
            return true;
        }

        for (Map.Entry<Map<String, String>, Boolean> entry : subCommands.entrySet()) {
            for (Map.Entry<String, String> keyEntry : entry.getKey().entrySet()) {
                instance.getLog().info(args[2]);
                if (args[2].equalsIgnoreCase(keyEntry.getKey()))
                    return entry.getValue();
            }
        }

        return true;
    }

    String getSubCommand() {
        return subCommand;
    }

}
