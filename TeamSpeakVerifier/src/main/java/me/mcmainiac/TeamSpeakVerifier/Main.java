package me.mcmainiac.TeamSpeakVerifier;

import me.mcmainiac.TeamSpeakVerifier.db.MySQLDB;
import me.mcmainiac.TeamSpeakVerifier.helpers.Config;
import me.mcmainiac.TeamSpeakVerifier.helpers.Log;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;

/**
 * Created by Ricardo on 14.08.2016.
 */
public class Main extends Plugin {
    public static MySQLDB db;
    private static Main instance;

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
        } catch (SQLException e) {
            Log.severe("Could not connect to database:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Log.info("If this is the first time you start this plugin, don't worry, just edit the config.yml");
            this.forceDisable();
        } catch (ClassNotFoundException e) {
            Log.severe("MySQL Connector-J not found! Please install it and try again!");
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            this.forceDisable();
        } catch (Config.Exception e) {
            Log.severe("You have an error in your config.yml:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            this.forceDisable();
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
            Log.severe("An error occured while disconnecting from your database:");
            Log.severe(e.getMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
        }
    }
}
