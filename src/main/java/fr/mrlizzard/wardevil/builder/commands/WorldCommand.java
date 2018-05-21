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
        sender.sendMessage("§c--[ §6WardBuilder | Worlds Help §c]--");
        sender.sendMessage("§e /build worlds help §f- §6Afficher la page d'aide");
        sender.sendMessage("§e /build worlds list §f- §6Afficher la liste");
        sender.sendMessage("§e /build worlds add <world> §f- §6Ajouter");
        sender.sendMessage("§e /build worlds del <world> §f- §6Supprimer");
        sender.sendMessage("§e /build worlds disable <world> §f- §6Désactiver");
        sender.sendMessage("§e /build worlds enable <world> §f- §6Activer");
        sender.sendMessage("§e /build worlds players list <world> §f- §6Liste des joueurs");
        sender.sendMessage("§e /build worlds players add <world> <name> §f- §6Ajouter joueur");
        sender.sendMessage("§e /build worlds players del <world> <name> §f- §6Supprimer joueur");
    }

    @Override
    public boolean executeCommand() {
        return true;
    }
}
