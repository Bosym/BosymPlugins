package me.mcmainiac.TeamSpeakVerifier.helpers;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class Messages {
    public static void send(CommandSender receiver, String path) {
        receiver.sendMessage(new TextComponent(Config.getString(path)));
    }

    public static void sendRaw(CommandSender receiver, String message) {
        receiver.sendMessage(new TextComponent(message));
    }
}
