package fr.mrlizzard.wardevil.builder.objects.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {

    public static List<Location> getLocationList(Plugin plugin, String name) {
        List<Location> spawns = new ArrayList<>();
        List<String> configSpawns = plugin.getConfig().getStringList(name);

        for (String loc : configSpawns) {
            String[] wxyz = loc.split(",");
            World w = Bukkit.getWorld(wxyz[0]);
            double x = Double.parseDouble(wxyz[1]);
            double y = Double.parseDouble(wxyz[2]);
            double z = Double.parseDouble(wxyz[3]);
            int pitch = Integer.parseInt(wxyz[4]);
            int yaw = Integer.parseInt(wxyz[5]);
            Location location = new Location(w, x, y, z);
            location.setPitch(pitch);
            location.setYaw(yaw);
            spawns.add(location);
        }

        return spawns;
    }

    public static Location convertStringToBlockLocation(String string) {
        if (string != null) {
            String[] wxyz = string.split(",");
            World w = Bukkit.getWorld(wxyz[0]);
            int x = Integer.parseInt(wxyz[1]);
            int y = Integer.parseInt(wxyz[2]);
            int z = Integer.parseInt(wxyz[3]);

            return new Location(w, x, y, z);
        }

        return null;
    }

    public static Location convertStringToLocation(String string) {
        if (string == null)
            return null;

        String[] wxyzPitchYaw = string.split(",");
        World w = Bukkit.getWorld(wxyzPitchYaw[0]);
        double x = Double.parseDouble(wxyzPitchYaw[1]);
        double y = Double.parseDouble(wxyzPitchYaw[2]);
        double z = Double.parseDouble(wxyzPitchYaw[3]);
        int pitch = Integer.parseInt(wxyzPitchYaw[4]);
        int yaw = Integer.parseInt(wxyzPitchYaw[5]);
        Location location = new Location(w, x, y, z);
        location.setPitch(pitch);
        location.setYaw(yaw);

        return location;
    }

    public static String convertLocationBlockToString(Location location) {
        String world = location.getWorld().getName();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return world + "," + x + "," + y + "," + z;
    }

    public static String convertLocationToString(Location location) {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        int pitch = (int)location.getPitch();
        int yaw = (int)location.getYaw();

        return world + "," + x + "," + y + "," + z + "," + pitch + "," + yaw;
    }

}
