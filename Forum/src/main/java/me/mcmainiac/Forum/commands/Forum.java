package me.mcmainiac.Forum.commands;

import me.mcmainiac.Forum.commands.subcommands.Activate;
import me.mcmainiac.Forum.helpers.Messages;
import me.mcmainiac.Forum.helpers.Permissions;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Forum extends Command {
    public Forum() {
        super("forum");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.COMMAND_FORUM)) {
            Messages.send(sender, "nopermission.forum");
            return;
        }

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "activate":
                    Activate.inherit(sender, args);
                    break;
                default:
                    Messages.send(sender, "help");
            }
        }
    }
}
