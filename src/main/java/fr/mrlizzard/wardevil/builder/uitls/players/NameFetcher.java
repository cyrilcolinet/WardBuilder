package fr.mrlizzard.wardevil.builder.uitls.players;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

class NameFetcher {

    public static List<String> nameHistoryFromUuid(UUID uuid) {
        URLConnection connection;
        String text;
        JsonArray list;
        JsonParser parser = new JsonParser();
        List<String> names;

        try {
            connection = new URL("https://api.mojang.com/user/profiles/" +
                    uuid.toString().replace("-", "").toLowerCase() + "/players").openConnection();
            text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
            list = (JsonArray) parser.parse(text);
            names = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                names.add(((JsonObject) list.get(i)).get("name").getAsString());
            }

            return names;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}