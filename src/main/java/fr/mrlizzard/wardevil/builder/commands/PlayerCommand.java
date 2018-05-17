package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class PlayerCommand extends ACommand {

    private Map<Map<String, String>, Boolean> subCommands;

    public PlayerCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand);

        this.subCommands = new HashMap<>();
        this.loadSubCommands();
    }

    private void loadSubCommands() {
        Map<String, String> desc = new HashMap<>();

        desc.put("promote", "Promouvoir un joueur");
        subCommands.put(desc, promotePlayer());
    }

    private boolean promotePlayer() {
        return true;
    }

    @Override
    public boolean executeCommand(CommandSender sender, Command command, String[] args) {
        this.setValues(sender, command, args);

        if (args.length <= 1 || args[1].equalsIgnoreCase("help")) {
            this.displayHelp(subCommands);
            return true;
        }

        return true;
    }

}
