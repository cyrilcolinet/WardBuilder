package fr.mrlizzard.wardevil.builder.uitls.players;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import fr.mrlizzard.wardevil.builder.WardBuilder;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

class UUIDFetcher implements Callable<Map<String, UUID>> {

    private static double   PROFILES_PER_REQUEST = 100;
    private static String   PROFILE_URL = "https://api.mojang.com/profiles/minecraft";

    private List<String>    names;
    private boolean         rateLimiting;

    public UUIDFetcher(List<String> names, boolean rateLimiting) {
        this.names = ImmutableList.copyOf(names);
        this.rateLimiting = rateLimiting;
    }

    public UUIDFetcher(List<String> names) {
        this(names, true);
    }

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        OutputStream stream = connection.getOutputStream();

        stream.write(body.getBytes());
        stream.flush();
        stream.close();
    }

    private static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(PROFILE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        return connection;
    }

    public static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" +
                id.substring(16, 20) + "-" + id.substring(20, 32));
    }

    public Map<String, UUID> call() throws Exception {
        Map<String, UUID> uuidMap = new HashMap<>();
        Integer requests = ((int) Math.ceil(names.size() / PROFILES_PER_REQUEST));
        HttpURLConnection connection;
        String body;
        Profile[] array;

        for (int i = 0; i < requests; i++) {
            connection = createConnection();
            body = WardBuilder.getInstance().getGson().toJson(
                    names.subList(i * 100, Math.min((i + 1) * 100, names.size())));
            writeBody(connection, body);
            array = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), Profile[].class);

            for (Profile profile : array) {
                UUID uuid = UUIDFetcher.getUUID(profile.id);
                uuidMap.put(profile.name, uuid);
            }

            if (rateLimiting && i != requests - 1) {
                Thread.sleep(100L);
            }
        }

        return uuidMap;
    }

    private class Profile {

        String id;
        String name;

    }
}
