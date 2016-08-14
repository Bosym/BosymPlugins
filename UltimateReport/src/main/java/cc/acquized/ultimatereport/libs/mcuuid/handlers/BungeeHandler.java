package cc.acquized.ultimatereport.libs.mcuuid.handlers;

import java.util.UUID;
import java.util.concurrent.*;

import net.mcapi.uuid.UUIDAPI;
import net.mcapi.uuid.UUIDHandler;
import net.mcapi.uuid.queries.APIQuery;
import net.mcapi.uuid.utils.UUIDUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * A {@link ProxyServer BungeeCord} handler
 * <p>
 * Checks offline player and online
 * players before sending a query
 *
 * <p>
 * Licensed under GPL v2 as found <a href="https://github.com/MC-API/uuid-java/blob/master/LICENSE">here</a>
 *
 * @author njb_said
 */
public class BungeeHandler extends UUIDHandler {

    private Plugin plugin;

    public BungeeHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public UUID getUUID(String username) {
        username = username.toLowerCase();
        if(ProxyServer.getInstance().getPlayer(username) != null) {
            return ProxyServer.getInstance().getPlayer(username).getUniqueId();
        }

        if(uuid_cache.containsKey(username)) {
            return uuid_cache.get(username);
        }

        final String u = username;
        FutureTask<UUID> task = new FutureTask<>(new Callable<UUID>() {
            public UUID call() throws Exception {
                APIQuery query = new APIQuery(u, "full_uuid", "uuid");

                try {
                    UUID uuid = UUID.fromString(query.request());
                    uuid_cache.put(u, uuid, 30, TimeUnit.MINUTES);
                    return uuid;
                } catch (Exception ex) {
                    System.err.println("[MC-API] Could not lookup '" + u + "', returning null..");
                    System.err.println("[MC-API] Server: " + UUIDAPI.getRegion().toString() + " (" + UUIDAPI.getRegion().buildURL() + ")");
                    System.err.println("[MC-API] Error: " + ex.getLocalizedMessage());
                    return null;
                }
            }
        });
        ProxyServer.getInstance().getScheduler().runAsync(plugin, task);

        try {
            return task.get();
        } catch(InterruptedException | ExecutionException ex) {
            System.err.println("[MC-API] Could not lookup '" + u + "', returning null..");
            System.err.println("[MC-API] Server: " + UUIDAPI.getRegion().toString() + " (" + UUIDAPI.getRegion().buildURL() + ")");
            System.err.println("[MC-API] Error: " + ex.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public String getUUIDString(String username) {
        UUID UUID = getUUID(username);
        String uuid = UUID == null ? null : UUID.toString().replace("-", "");

        return uuid;
    }

    @Override
    public String getUsername(final UUID uuid) {
        if(ProxyServer.getInstance().getPlayer(uuid) != null) {
            return ProxyServer.getInstance().getPlayer(uuid).getName();
        }

        if(name_cache.containsKey(uuid)) {
            return name_cache.get(uuid);
        }

        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            public String call() throws Exception {
                APIQuery query = new APIQuery(uuid.toString().replace("-", ""), "name");

                try {
                    String username = query.request();
                    name_cache.put(uuid, username, 1, TimeUnit.HOURS);
                    return username;
                } catch (Exception ex) {
                    System.err.println("[MC-API] Could not lookup uuid '" + uuid.toString().replace("-", "") + "', returning null..");
                    System.err.println("[MC-API] Server: " + UUIDAPI.getRegion().toString() + " (" + UUIDAPI.getRegion().buildURL() + ")");
                    System.err.println("[MC-API] Error: " + ex.getLocalizedMessage());
                    return null;
                }
            }
        });
        ProxyServer.getInstance().getScheduler().runAsync(plugin, task);

        try {
            return task.get();
        } catch(InterruptedException | ExecutionException ex) {
            System.err.println("[MC-API] Could not lookup uuid '" + uuid.toString().replace("-", "") + "', returning null..");
            System.err.println("[MC-API] Server: " + UUIDAPI.getRegion().toString() + " (" + UUIDAPI.getRegion().buildURL() + ")");
            System.err.println("[MC-API] Error: " + ex.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public String getUsername(String uuid) {
        return getUsername(UUID.fromString(UUIDUtils.convertUUIDToJava(uuid)));
    }

}