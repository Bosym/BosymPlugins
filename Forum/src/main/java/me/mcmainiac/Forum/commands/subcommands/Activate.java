package me.mcmainiac.Forum.commands.subcommands;

import me.mcmainiac.Forum.Main;
import me.mcmainiac.Forum.helpers.Config;
import me.mcmainiac.Forum.helpers.Log;
import me.mcmainiac.Forum.helpers.Messages;
import me.mcmainiac.Forum.helpers.Permissions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.util.HashMap;

public class Activate extends Command {
    private Activate() {
        super("forum");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender.hasPermission(Permissions.COMMAND_FORUM_ACTIVATE))) {
            Messages.send(sender, "nopermission.forum.activate");
            return;
        }

        try {
            Main.db.connect();
            Main.db.where("username", sender.getName());
            ResultSet rs = Main.db.getOne("phpbb_users");

            // check if username exists in table
            if (rs.next()) {
                // check if the user account is inactive
                if (rs.getInt("user_type") == 1) {
                    // select cols to update
                    HashMap<String, Object> cols = new HashMap<>();
                    // 0 for active account / 1 for inactive account / 2 for bot account / 3 for system admin
                    cols.put("user_type", 0);
                    int affectedrows = Main.db.update("phpbb_users", cols);

                    if (affectedrows == 1)
                        Messages.send(sender, "activate.success");
                    else
                        Messages.send(sender, "activate.failed");
                } else {
                    Messages.send(sender, "activate.already");
                }
            } else {
                Messages.send(sender, "activate.notfound");
            }
        } catch (Exception e) {
            Log.severe(sender.getName() + " caused an error to occurr:");
            Log.severe(e.getLocalizedMessage());
            if (Config.getBoolean("debug"))
                e.printStackTrace();
        }
    }

    public static void inherit(CommandSender sender, String[] args) {
        new Activate().execute(sender, args);
    }
}
