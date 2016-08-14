package me.mcmainiac.TeamSpeakVerifier;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import me.mcmainiac.TeamSpeakVerifier.db.MySQLDB;
import me.mcmainiac.TeamSpeakVerifier.helpers.Config;
import me.mcmainiac.TeamSpeakVerifier.helpers.Log;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.logging.Level;

public class Main extends Plugin {
    private static MySQLDB db;
    private static Main instance;
    private static TS3Bot bot;

    @Override
    public void onEnable() {
        try {
            instance = this;

            Config.init(this);

            if (Config.getBoolean("debug"))
                Log.info("Debug mode enabled!");

            db = new MySQLDB(
                    Config.getString("database.host"),
                    Config.getInt("database.port"),
                    Config.getString("database.username"),
                    Config.getString("database.password"),
                    Config.getString("database.name")
            );

            db.connect();

            bot = new TS3Bot();
        } catch (SQLException e) {
            Log.severe("Could not connect to database:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Log.info("If this is the first time you start this plugin, don't worry, just edit the config.yml");
            Main.forceDisable();
        } catch (ClassNotFoundException e) {
            Log.severe("MySQL Connector-J not found! Please install it and try again!");
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Main.forceDisable();
        } catch (Config.Exception e) {
            Log.severe("You have an error in your config.yml:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Main.forceDisable();
        }
    }

    public static void forceDisable() {
        Log.severe(instance.getDescription().getName() + " got force-disabled!");
        instance.getProxy().getPluginManager().unregisterCommands(instance);
        instance.onDisable();
    }

    @Override
    public void onDisable() {
        try {
            db.disconnect();
        } catch (SQLException e) {
            Log.severe("An error occurred while disconnecting from your database:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
        }
    }

    public static TS3Config getTS3Config() {
        final TS3Config config = new TS3Config();

        config.setHost(Config.getString("teamspeak.host"));

        if (Config.getBoolean("debug"))
            config.setDebugLevel(Level.ALL); // default is Level.WARNING

        config.setQueryPort(Config.getInt("teamspeak.port"));

        return config;
    }

    public static TS3Query getTS3Query(TS3Config config) {
        return (new TS3Query(config)).connect();
    }

    public static TS3Api getTS3Api(TS3Query query) {
        TS3Api api = query.getApi();

        api.login(
                Config.getString("teamspeak.username"),
                Config.getString("teamspeak.password")
        );

        api.selectVirtualServerById(1);

        api.setNickname(Config.getString("teamspeak.nickname"));

        return api;
    }

    public static TS3ApiAsync getTS3ApiAsync(TS3Query query) {
        TS3ApiAsync api = query.getAsyncApi();


        api.login(
                Config.getString("teamspeak.username"),
                Config.getString("teamspeak.password")
        );

        api.selectVirtualServerById(1);

        api.setNickname(Config.getString("teamspeak.nickname"));

        return api;
    }
}
