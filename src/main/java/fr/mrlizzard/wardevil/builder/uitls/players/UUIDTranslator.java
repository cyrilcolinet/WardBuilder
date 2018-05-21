package fr.mrlizzard.wardevil.builder.uitls.players;

import com.google.gson.Gson;
import fr.mrlizzard.wardevil.builder.WardBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class UUIDTranslator  {

    private WardBuilder                         instance;
    private Pattern                             UUID_PATTERN;
    private Pattern                             MOJANGIAN_UUID_PATTERN ;
    private Map<String, CachedUUIDEntry>        nameToUuidMap;
    private Map<UUID, CachedUUIDEntry>          uuidToNameMap;

    public UUIDTranslator(WardBuilder instance) {
        this.instance = instance;
        this.UUID_PATTERN = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");
        this.MOJANGIAN_UUID_PATTERN = Pattern.compile("[a-fA-F0-9]{32}");
        this.nameToUuidMap = new ConcurrentHashMap<>(128, 0.5f, 4);
        this.uuidToNameMap = new ConcurrentHashMap<>(128, 0.5f, 4);
    }

    private void addToMaps(String name, UUID uuid) {
        Calendar calendar = Calendar.getInstance();
        CachedUUIDEntry entry;

        calendar.add(Calendar.DAY_OF_WEEK, 3);

        entry = new CachedUUIDEntry(name, uuid, calendar);
        nameToUuidMap.put(name.toLowerCase(), entry);
        uuidToNameMap.put(uuid, entry);
    }

    public void persistInfo(String name, UUID uuid, Jedis jedis) {
        addToMaps(name, uuid);
        jedis.hset("uuid-cache", name.toLowerCase(), instance.getGson().toJson(uuidToNameMap.get(uuid)));
        jedis.hset("uuid-cache", uuid.toString(), instance.getGson().toJson(uuidToNameMap.get(uuid)));
    }

    public UUID getUUID(String name, boolean allowMojangCheck) {
        CachedUUIDEntry cachedUUIDEntry;
        String stored;
        CachedUUIDEntry entry;
        Map<String, UUID> uuidMap1;

        if (instance.getServer().getPlayer(name) != null)
            return instance.getServer().getPlayer(name).getUniqueId();

        cachedUUIDEntry = nameToUuidMap.get(name.toLowerCase());
        if (cachedUUIDEntry != null) {
            if (!cachedUUIDEntry.expired())
                return cachedUUIDEntry.getUuid();
            else
                nameToUuidMap.remove(name);
        }

        if (UUID_PATTERN.matcher(name).find()) {
            return UUID.fromString(name);
        }

        if (MOJANGIAN_UUID_PATTERN.matcher(name).find()) {
            return UUIDFetcher.getUUID(name);
        }

        try (Jedis jedis = instance.getConnector().getCacheRessource()) {
            stored = jedis.hget("uuid-cache", name.toLowerCase());
            if (stored != null) {
                entry = instance.getGson().fromJson(stored, CachedUUIDEntry.class);
                if (entry.expired()) {
                    jedis.hdel("uuid-cache", name.toLowerCase());
                } else {
                    jedis.close();
                    nameToUuidMap.put(name.toLowerCase(), entry);
                    uuidToNameMap.put(
                            entry.getUuid(),
                            entry);
                    return entry.getUuid();
                }
            }

            if (!allowMojangCheck)
                return null;

            try {
                uuidMap1 = new UUIDFetcher(Collections.singletonList(name)).call();
            } catch (Exception e) {
                instance.getLog().error("Unable to fetch UUID from Mojang for " + name);
                return null;
            }
            for (Map.Entry<String, UUID> entry2 : uuidMap1.entrySet()) {
                if (entry2.getKey().equalsIgnoreCase(name)) {
                    persistInfo(entry2.getKey(), entry2.getValue(), jedis);
                    jedis.close();
                    return entry2.getValue();
                }
            }
        } catch (JedisException e) {
            instance.getLog().error("Unable to fetch UUID for " + name);
        }

        return null;
    }

    public String getName(UUID uuid, boolean allowMojangCheck) {
        CachedUUIDEntry cachedUUIDEntry;
        CachedUUIDEntry entry;
        String name;

        if (instance.getServer().getPlayer(uuid) != null)
            return instance.getServer().getPlayer(uuid).getName();

        cachedUUIDEntry = uuidToNameMap.get(uuid);
        if (cachedUUIDEntry != null) {
            if (!cachedUUIDEntry.expired())
                return cachedUUIDEntry.getName();
            else
                uuidToNameMap.remove(uuid);
        }

        try (Jedis jedis = instance.getConnector().getCacheRessource()) {
            String stored = jedis.hget("uuid-cache", uuid.toString());
            if (stored != null) {
                entry = instance.getGson().fromJson(stored, CachedUUIDEntry.class);

                if (entry.expired()) {
                    jedis.hdel("uuid-cache", uuid.toString());
                } else {
                    jedis.close();
                    nameToUuidMap.put(entry.getName().toLowerCase(), entry);
                    uuidToNameMap.put(uuid, entry);
                    return entry.getName();
                }
            }

            if (!allowMojangCheck)
                return null;

            try {
                name = NameFetcher.nameHistoryFromUuid(uuid).get(0);
            } catch (Exception e) {
                instance.getLog().error("Unable to fetch name from Mojang for " + uuid);
                return null;
            }

            if (name != null) {
                persistInfo(name, uuid, jedis);
                jedis.close();
                return name;
            }

            return null;
        } catch (JedisException e) {
            instance.getLog().error("Unable to fetch name for " + uuid);
            return null;
        }
    }

    private static class CachedUUIDEntry {

        private String        name;
        private UUID          uuid;
        private Calendar      expiry;

        public boolean expired() {
            return Calendar.getInstance().after(expiry);
        }

        public CachedUUIDEntry(String name, UUID uuid, Calendar expiry) {
            this.name = name;
            this.uuid = uuid;
            this.expiry = expiry;
        }

        public String getName() {
            return name;
        }

        public UUID getUuid() {
            return uuid;
        }

        public Calendar getExpiry() {
            return expiry;
        }

    }
}
