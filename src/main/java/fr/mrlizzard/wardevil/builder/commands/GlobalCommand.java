package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.managers.GlobalManager;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.Arrays;
import java.util.List;

public class GlobalCommand extends ACommand {

    public GlobalCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand, "Envoyer un message global");
    }

    @Ignore
    public void loadSubCommands() {}

    @Override
    public void displayHelp() {

    }

    @Override
    public boolean executeCommand() {
        GlobalManager glob = new GlobalManager();
        List<String> msg = Arrays.asList(args);

        this.setValues(sender, command, args);
        if (msg.size() >= 2) {
            glob.sendGlobalMessage(sender, msg);
            return true;
        }

        sender.sendMessage("§cUsage: /build global <msg>");
        return true;
    }
}
