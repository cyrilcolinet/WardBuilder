package fr.mrlizzard.wardevil.builder.managers;

import com.google.gson.reflect.TypeToken;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.objects.World;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class WorldManager {

    private WardBuilder                 instance;

    private Map<String, World>          worlds;
    private Map<World, Integer>         tasks;

    public WorldManager(WardBuilder instance) {
        this.instance = instance;
        this.worlds = new HashMap<>();
        this.tasks = new HashMap<>();

        this.configure();
    }

    private void configure() {
        File file = new File(instance.getDataFolder(), "worlds.json");
        FileReader reader;
        Type collectionType = new TypeToken(){}.getType();

        try {
            if (!file.exists()) {
                this.createWorldsConfigFile(file);
                return;
            }

            reader = new FileReader(file);
            worlds = instance.getGson().fromJson(reader, collectionType);
            worlds.values().forEach(world -> world.startTask());
        } catch (Exception err) {
            instance.getLog().error(err.getMessage());
            err.printStackTrace();
        }
    }

    private void createWorldsConfigFile(File file) throws Exception {
        String json;
        FileWriter writer;
        BufferedWriter bufferedWriter;

        file.createNewFile();
        file.setWritable(true);

        writer = new FileWriter(file.getAbsoluteFile());
        bufferedWriter = new BufferedWriter(writer);
        json = instance.getGson().toJson(worlds);

        bufferedWriter.write(json);
        bufferedWriter.close();
        writer.close();
    }

    public void killWorldTasks() {
        instance.getLog().info("Killing all world tasks...");

        tasks.values().forEach(task -> {
            instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> {
                instance.getServer().getScheduler().cancelTask(task);
            }, 600);
        });
    }

    public Map<World, Integer> getTasks() {
        return tasks;
    }

}
