package me.mcmainiac.Forum;

import me.mcmainiac.Forum.commands.*;
import me.mcmainiac.Forum.helpers.Config;
import me.mcmainiac.Forum.helpers.Log;
import me.mcmainiac.Forum.db.MySQLDB;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;

public class Main extends Plugin {
    public static MySQLDB db;
    public static Main instance;

    @Override
    public void onEnable() {
        try {
            // set instance
            instance = this;

            // register commands
            getProxy().getPluginManager().registerCommand(this, new Forum());

            // init config
            Config.init(this);

            // check for debug mode
            if (Config.getBoolean("debug"))
                Log.info("Debug mode enabled!");

            // load db
            db = new MySQLDB(
                    Config.getString("mysql.host"),
                    Config.getInt("mysql.port"),
                    Config.getString("mysql.username"),
                    Config.getString("mysql.password"),
                    Config.getString("mysql.name")
            );

            // connect; if any errors occur, they will be caught
            db.connect();

            // no exception means we are connected
            Log.info("Successfully connected to database.");

            // register Listener
            getProxy().getPluginManager().registerListener(this, new Listener());

            // since every exception has been caught, this plugin is good to go!
            Log.info("Plugin enabled!");
        } catch (SQLException e) {
            Log.severe("Could not connect to database: " + e.getLocalizedMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Log.severe("If this is the first time this plugin starts, configure your config.yml!");
            this.forceDisable();
        } catch (ClassNotFoundException e) {
            Log.severe("MySQL Connector/J not found!");
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            this.forceDisable();
        } catch (Config.Exception e) {
            Log.severe("Configuration exception occurred: " + e.getLocalizedMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            this.forceDisable();
        }
    }

    public void forceDisable() {
        Log.severe(getDescription().getName() + " got force-disabled!");
        this.getProxy().getPluginManager().unregisterCommands(this);
        this.onDisable();
    }

    @Override
    public void onDisable() {
        try {
            db.disconnect();
        } catch (SQLException e) {
            Log.severe("An SQL exception occured while disconnecting from your database!");
            if (Config.getBoolean("debug"))
                e.printStackTrace();
        }
    }
}
