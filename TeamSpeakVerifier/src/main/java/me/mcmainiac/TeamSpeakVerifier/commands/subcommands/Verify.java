package me.mcmainiac.TeamSpeakVerifier.commands.subcommands;

import me.mcmainiac.TeamSpeakVerifier.helpers.Messages;
import net.md_5.bungee.api.CommandSender;

public class Verify {
    public static void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            Messages.send(sender, "minecraft.messages.toofewargs");
            return;
        }

        // TODO: Verify code the user has entered
    }
}
