package me.mcmainiac.TeamSpeakVerifier;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import me.mcmainiac.TeamSpeakVerifier.commands.Teamspeak;
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

            getProxy().getPluginManager().registerCommand(this, new Teamspeak());

            if (Config.getBoolean("debug"))
                Log.info("Debug mode enabled!");

            Main.getDb(); // initialize db

            bot = new TS3Bot();
        } catch (SQLException e) {
            Log.severe("Could not connect to database:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Log.info("If this is the first time you start this plugin, don't worry, just edit the config.yml");
            Main.forceDisable();
        } catch (Config.Exception e) {
            Log.severe("You have an error in your config.yml:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Main.forceDisable();
        } catch (Exception e) {
            Log.severe("An exception occured:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Main.forceDisable();
        }
    }

    public static void forceDisable() {
        Log.severe("Disabling " + instance.getDescription().getName() + "...");
        instance.getProxy().getPluginManager().unregisterCommands(instance);
        instance.onDisable();
    }

    @Override
    public void onDisable() {
        try {
            if (db != null)
                db.disconnect();

            if (bot != null)
                bot.disconnect();
        } catch (SQLException e) {
            Log.severe("An error occurred while disconnecting from your database:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
        } catch (Exception e) {
            Log.severe("An error occurred while disabling " + instance.getDescription().getName() + ":");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
        }
    }

    public static TS3Config getTS3Config() {
        final TS3Config config = new TS3Config();

        if (Config.getBoolean("debug"))
            config.setDebugLevel(Level.FINE); // default level is warning

        String host = Config.getString("teamspeak.host");
        int port = Config.getInt("teamspeak.port");

        if (Config.getBoolean("debug"))
            Log.info("Connecting to '" + host + ":" + port + "'");

        config.setHost(host);
        config.setQueryPort(port);

        return config;
    }

    public static TS3Query getTS3Query(TS3Config config) {
        TS3Query q = new TS3Query(config);
        return q.connect();
    }

    public static TS3Api getTS3Api(TS3Query query) {
        query.connect();

        TS3Api api = query.getApi();

        String username = Config.getString("teamspeak.username"),
                nickname = Config.getString("teamspeak.nickname");

        if (Config.getBoolean("debug")) {
            Log.info("Using username: '" + username + "' to log in");
            Log.info("Using '" + nickname + "' as nickname");
        }

        api.login(
                username,
                Config.getString("teamspeak.password")
        );

        api.selectVirtualServerById(1);

        api.setNickname(nickname);

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

    public static TS3Bot getBot() {
        return bot;
    }

    public static MySQLDB getDb() throws SQLException {
        try {
            if (db == null)
                db = new MySQLDB(
                        Config.getString("database.host"),
                        Config.getInt("database.port"),
                        Config.getString("database.username"),
                        Config.getString("database.password"),
                        Config.getString("database.name")
                );

            if (!db.isConnected())
                db.connect();

            return db;
        } catch (ClassNotFoundException e) {
            Log.severe("MySQL Connector-J not found! Please install it and try again!");
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Main.forceDisable();
        }
        return null;
    }
}
