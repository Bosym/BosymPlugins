package me.mcmainiac.TeamSpeakVerifier.commands;

import me.mcmainiac.TeamSpeakVerifier.commands.subcommands.Verify;
import me.mcmainiac.TeamSpeakVerifier.helpers.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Teamspeak extends Command {
    public Teamspeak() {
        super("teamspeak");
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Messages.send(sender, "minecraft.messages.toofewargs");
            return;
        }

        if (args[0].equalsIgnoreCase("verify"))
            Verify.execute(sender, args);
    }
}
