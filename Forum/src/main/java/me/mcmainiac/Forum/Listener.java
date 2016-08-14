package me.mcmainiac.Forum;

import me.mcmainiac.Forum.helpers.Config;
import me.mcmainiac.Forum.helpers.Log;
import me.mcmainiac.Forum.helpers.Messages;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Listener implements net.md_5.bungee.api.plugin.Listener {
    @EventHandler
    public void onPlayerJoinEvent(final PostLoginEvent event) {
        if (Config.getBoolean("debug"))
            Log.info("Checking for a forum account: " + event.getPlayer().getName());

        ProxyServer.getInstance().getScheduler().runAsync(Main.instance, new Runnable() {
            @Override
            public void run() {
                ProxiedPlayer p = event.getPlayer();
                try {
                    Main.db.where("username", p.getName());
                    ResultSet rs = Main.db.getOne("phpbb_users");

                    // if the row is not set, the player has no forum account
                    if (!rs.next()) {
                        Messages.send(p, "activate.notregistered");
                        if (Config.getBoolean("debug"))
                            Log.info("Player " + p.getName() + " doesn't have a forum account.");
                    } else {
                        if (rs.getInt("user_type") == 1) {
                            Messages.send(p, "help");
                            if (Config.getBoolean("debug"))
                                Log.info("Player " + p.getName() + " hasn't yet activated their forum account.");
                        } else {
                            if (Config.getBoolean("debug"))
                                Log.info("Player " + p.getName() + " has an activated forum account.");
                        }
                    }

                } catch (SQLException e) {
                    Log.severe("SQLException occured while checking if " + p.getName() + " has a forum account!");
                    if (Config.getBoolean("debug"))
                        e.printStackTrace();
                }
            }
        });
    }
}
