package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WorldCommand extends ACommand {

    public WorldCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand);
    }

    @Override
    public void loadSubCommands() {

    }

    @Override
    public boolean executeCommand(CommandSender sender, Command command, String[] args) {
        this.setValues(sender, command, args);

        if (args.length <= 1 || args[1].equalsIgnoreCase("help")) {
            //this.displayHelp(sender);
            return true;
        }

        return true;
    }
}
