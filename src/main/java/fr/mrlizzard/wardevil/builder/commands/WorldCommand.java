package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;

public class WorldCommand extends ACommand {

    public WorldCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand, "Gestion des mondes");
    }

    @Override
    public void loadSubCommands() {

    }

    @Override
    public void displayHelp() {

    }

    @Override
    public boolean executeCommand() {
        return true;
    }
}
