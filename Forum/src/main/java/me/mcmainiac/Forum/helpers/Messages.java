package me.mcmainiac.Forum.helpers;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;

public class Messages {
    private static final String prefix = "§8[§5Forum§8] §f";
    private static HashMap<String, String[]> messages = new HashMap<>();
    static {
        messages.put("help", new String[]{
                "§cBitte benute §a/forum activate",
                "§cum deinen Forenaccount zu aktivieren."
        });

        messages.put("activate.success", new String[]{
                "§aDein Forenaccount wurde erfolgreich aktiviert!"
        });
        messages.put("activate.failed", new String[]{
                "§cEs ist ein Fehler aufgetreten!",
                "§cDein Forenaccount wurde §nnicht §caktiviert!"
        });
        messages.put("activate.already", new String[] {
                "§cDein Forenaccount ist bereits aktiviert!"
        });
        messages.put("activate.notfound", new String[]{
                "§cDein Forenaccount konnte nicht gefunden werden!",
                "§cDer Benutzername muss dein Minecraft Benutzername sein!"
        });
        messages.put("activate.notregistered", new String[]{
                "Bitte besuche §a§nhttps://bosym.de/",
                "um einen Forenaccount zu erstellen!",
                "Bitte benutze deinen §aMinecraft-Benutzernamen",
                "als §6Forum-Benutzernamen§f!"
        });

        messages.put("nopermission.forum", new String[]{
                "§cDu hast keine Berechtigung dazu!"
        });
        messages.put("nopermission.forum.activate", new String[]{
                "§cDu hast keine Berechtigung dazu!"
        });
    }

    public static void send(CommandSender receiver, String path) {
        for (String message : messages.get(path))
            receiver.sendMessage(new TextComponent(prefix + message));
    }

    public static void sendRaw(CommandSender receiver, String message) {
        receiver.sendMessage(new TextComponent(prefix + message));
    }
}
