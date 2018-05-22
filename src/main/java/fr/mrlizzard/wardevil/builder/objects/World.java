package fr.mrlizzard.wardevil.builder.objects;

import com.google.gson.annotations.Expose;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.managers.WorldManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.*;

public class World implements Runnable {

    private WardBuilder                     instance;
    private Integer                         connected;
    private Integer                         taskId;

    @Expose private List<String>            signs;
    @Expose private String                  name;
    @Expose private Boolean                 disabled;
    @Expose private Boolean                 safe;
    @Expose private Map<UUID, String>       specators;
    @Expose private Map<UUID, String>       builders;

    public World(WardBuilder instance, String name) {
        this.instance = instance;
        this.name = name;
        this.signs = new ArrayList<>();
        this.disabled = false;
        this.specators = new HashMap<>();
        this.builders = new HashMap<>();
        this.safe = false;

        this.startTask(instance, instance.getWorldManager());
    }

    public void startTask(WardBuilder plugin, WorldManager manager) {
        if (instance == null)
            instance = plugin;

        if (!manager.getTasks().containsKey(this)) {
            taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 20);
            manager.getTasks().put(this, taskId);
        }
    }

    @Override
    public void run() {
        connected = 0;

        for (Player player : instance.getServer().getOnlinePlayers())
            if (player.getWorld().getName().equalsIgnoreCase(name))
                connected++;

        if (connected == 0 && instance.getServer().getWorld(name) != null) {
            instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), "mv unload " + name);
            instance.getServer().broadcastMessage("§6Activation du mode écomie pour le monde §e" + name);
        }

        signs.forEach(strLoc -> {
            Boolean canUpdate = false;
            Location location = null;
            String[] split = StringUtils.split(strLoc, ",");
            org.bukkit.World world = instance.getServer().getWorld(split[0]);

            if (world != null) {
                location = new Location(world, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
            }

            if (location != null && location.getBlock().getType().equals(Material.WALL_SIGN)) {
                for (Player player : instance.getServer().getOnlinePlayers())
                    if (player.getWorld().getName().equalsIgnoreCase(location.getWorld().getName())
                            && player.getLocation().distance(location) <= 64)
                        canUpdate = true;

                if (canUpdate) {
                    Sign sign = ((Sign) location.getBlock().getState());

                    sign.setLine(0, ((isProtected()) ? "§8-- §cSAFE §8--" : "§8-----------"));
                    sign.setLine(1, ((isDisabled()) ? ChatColor.RED : ChatColor.GREEN) + "§l" + name);
                    sign.setLine(2, "§d" + connected + "/" + 30);
                    sign.setLine(3, "§8-----------");
                    sign.update();
                }
            }
        });
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean isProtected() {
        return safe;
    }

    public void setProtected(Boolean safe) {
        this.safe = safe;
    }

    public String getName() {
        return name;
    }

    public List<String> getSigns() {
        return signs;
    }

    public Map<UUID, String> getBuilders() {
        return builders;
    }

    public Map<UUID, String> getSpecators() {
        return specators;
    }

    public Integer getTaskId() {
        return taskId;
    }

}
