package me.mcmainiac.TeamSpeakVerifier.commands.subcommands;

import me.mcmainiac.TeamSpeakVerifier.Main;
import me.mcmainiac.TeamSpeakVerifier.helpers.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Verify {
    public static void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            Messages.send(sender, "minecraft.messages.toofewargs");
            return;
        }

        if (!(sender instanceof ProxiedPlayer)) {
            Messages.sendRaw(sender, "Sorry, but this is a player only command!");
            return;
        }

        String code = args[2];

        if (Main.getBot().verifyCode((ProxiedPlayer) sender, code)) {
            Messages.send(sender, "minecraft.messages.success");
        } else {
            Messages.send(sender, "minecraft.messages.failed");
        }
    }
}
