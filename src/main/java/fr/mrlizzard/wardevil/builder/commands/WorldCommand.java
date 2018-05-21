package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.managers.WorldManager;
import fr.mrlizzard.wardevil.builder.objects.BuildPlayer;
import fr.mrlizzard.wardevil.builder.objects.World;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldCommand extends ACommand {

    private WorldManager                    manager;

    public WorldCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand, "Gestion des mondes");

        this.manager = instance.getWorldManager();
    }

    @Override
    public void loadSubCommands() {
        subCommands.put("list", () -> listWorlds());
        subCommands.put("add", () -> addWorld());
        subCommands.put("del", () -> deleteWorld());
        subCommands.put("disable", () -> disableWorld());
        subCommands.put("enable", () -> enableWorld());
        subCommands.put("protect", () -> enableWorldProtection());
        subCommands.put("unprotect", () -> disableWorldProtection());
        subCommands.put("players", () -> playersGesture());
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
        sender.sendMessage("§e /build worlds protect <world> §f- §6Proteger (SuperUser)");
        sender.sendMessage("§e /build worlds unprotect <world> §f- §6Dévérouiller (SuperUser)");
        sender.sendMessage("§e /build worlds players list <world> §f- §6Liste des joueurs");
        sender.sendMessage("§e /build worlds players add <world> <name> §f- §6Ajouter joueur");
        sender.sendMessage("§e /build worlds players del <world> <name> §f- §6Supprimer joueur");
    }

    private void listWorlds() {
        Integer page = 1;
        Integer maxPages = 5;
        Integer key;
        List<World> worlds;

        if (args.length > 3) {
            sender.sendMessage("§cUsage: /build worlds list <page>");
            return;
        } else if (args.length == 3) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (Exception err) {
                sender.sendMessage("§cLe numéro de page doit être un nombre.");
                return;
            }
        }

        key = ((page == 1) ? 0 : (maxPages * page - 1));
        worlds = new ArrayList<>(manager.getWorlds().values());

        if (worlds.size() == 0) {
            sender.sendMessage("§cAucun monde n'a été créé. Tapez /build worlds pour voir l'aide.");
            return;
        }

        sender.sendMessage("§eListe des mondes (" + page + "/" + maxPages + "):");
        for (int loop = 0; loop < maxPages; loop++) {
            World world;
            ChatColor color;

            try {
                world = worlds.get(key);
            } catch (IndexOutOfBoundsException err) {
                break;
            }

            color = ((world.isDisabled()) ? ChatColor.RED : ChatColor.GREEN);
            sender.sendMessage("  §b- " + color + world.getName() + ((world.isProtected()) ? " §c(protégé)" : ""));
            key++;
        }
    }

    private void addWorld() {
        String worldName;
        BuildPlayer buildPlayer;
        String strPlayer;

        if (args.length != 3) {
            sender.sendMessage("§cUsage: /build worlds add <world>");
            return;
        }

        worldName = args[2];
        if (worldName.equalsIgnoreCase("world")) {
            sender.sendMessage("§cImpossible de créer le monde: ce monde est un hub.");
            return;
        }

        for (World world : manager.getWorlds().values()) {
            if (world.getName().equalsIgnoreCase(worldName)) {
                sender.sendMessage("§cUn monde ayant déjà la même nom a été trouvé: " + world.getName());
                return;
            }
        }

        org.bukkit.World world = instance.getServer().getWorld(worldName);
        if (world == null)
            instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), "mv create " + worldName + " normal -t flat");

        manager.getWorlds().put(worldName, new World(instance, worldName));

        if (sender instanceof Player) {
            buildPlayer = instance.getManager().getPlayer(((Player) sender).getUniqueId());
            strPlayer = buildPlayer.getRank().getPrefix() + sender.getName() + ChatColor.RESET;

            instance.getServer().broadcastMessage("§aNouveau monde créé par " + strPlayer + ": §e" + worldName);
        } else {
            instance.getServer().broadcastMessage("§aNouveau monde créé: §e" + worldName);
        }
    }

    private void deleteWorld() {
        String worldName;
        Boolean safe;
        World world;
        BuildPlayer buildPlayer;
        String strPlayer;
        String protectedWorld = "";

        if (args.length != 3) {
            sender.sendMessage("§cUsage: /build worlds del <world>");
            return;
        }

        worldName = args[2];
        if (worldName.equalsIgnoreCase("world")) {
            sender.sendMessage("§cImpossible de suprimer le monde: ce monde est un hub.");
            return;
        }

        world = manager.getWorlds().getOrDefault(worldName, null);
        if (world == null) {
            sender.sendMessage("§cAucun monde trouvé pour " + worldName);
            return;
        }

        safe = world.isProtected();
        if (safe && !sender.hasPermission("wardbuilder.worlds.delete.safe")) {
            sender.sendMessage("§cCe monde est protégé. Seul un SuperUser peut le supprimer.");
            return;
        }

        world.getSigns().forEach(sign-> {
            Location loc = null;
            String[] split = StringUtils.split(sign, ",");
            org.bukkit.World w = instance.getServer().getWorld(split[0]);

            if (w != null)
                loc = new Location(w, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));

            if (loc != null && loc.getBlock().getType().equals(Material.WALL_SIGN))
                loc.getBlock().breakNaturally();
        });

        if (safe)
            protectedWorld = "§c(protégé)§a ";

        instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), "mv unload " + worldName);
        manager.getWorlds().remove(worldName);

        if (sender instanceof Player) {
            buildPlayer = instance.getManager().getPlayer(((Player) sender).getUniqueId());
            strPlayer = buildPlayer.getRank().getPrefix() + sender.getName() + ChatColor.RESET;

            instance.getServer().broadcastMessage("§aMonde " + protectedWorld + "supprimé par " + strPlayer + ": §e" + worldName);
        } else {
            instance.getServer().broadcastMessage("§aMonde " + protectedWorld + "supprimé: §e" + worldName);
        }
    }

    private void disableWorld() {

    }

    private void enableWorld() {

    }

    private void enableWorldProtection() {

    }

    private void disableWorldProtection() {

    }

    private void playersGesture() {

    }

    @Override
    public boolean executeCommand() {
        for (Map.Entry<String, Runnable> entry : subCommands.entrySet()) {
            if (args[1].equalsIgnoreCase(entry.getKey())) {
                if (!sender.hasPermission("wardbuilder.worlds.command")) {
                    sender.sendMessage("§cVous n'avez pas la permission d'intéragir avec les mondes.");
                    return true;
                }

                entry.getValue().run();
                return true;
            }
        }

        sender.sendMessage("§cCommande inconnue. Taper /build worlds pour voir l'aide.");
        return true;
    }
}
