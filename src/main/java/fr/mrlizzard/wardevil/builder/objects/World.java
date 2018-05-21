package fr.mrlizzard.wardevil.builder.objects;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class World implements Runnable {

    private WardBuilder             instance;
    private String                  name;
    private Integer                 connected;

    private List<String>            signs;

    public World(WardBuilder instance, String name) {
        this.instance = instance;
        this.name = name;
        this.signs = new ArrayList<>();
        this.connected = 0;

        this.startTask();
    }

    public void startTask() {
        Integer task;

        if (!instance.getWorldManager().getTasks().containsKey(this)) {
            task = instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, this, 0, 1);
            instance.getWorldManager().getTasks().put(this, task);
        }
    }

    @Override
    public void run() {
        for (Player player : instance.getServer().getOnlinePlayers())
            if (player.getWorld().getName().equalsIgnoreCase(name))
                connected++;

        if (connected == 0 && instance.getServer().getWorld(name) != null)
            instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), "mvunload " + name);

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

                    sign.setLine(0, "§8-----------");
                    sign.setLine(1, "§9§l" + name);
                    sign.setLine(2, connected + " / " + 30);
                    sign.setLine(3, "§8-----------");
                    sign.update();
                }
            }

            connected = 0;
        });
    }

}
