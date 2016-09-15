package me.mcmainiac.TeamSpeakVerifier.helpers;

import java.util.logging.Logger;

public class Log {
    private static final Logger log = Logger.getLogger("Minecraft");

    private static final String prefix = "[TeamSpeakVerifier]: ";

    public static void info(String msg) {
        log.info(prefix + msg);
    }

    public static void severe(String msg) {
        log.severe(prefix + msg);
    }
}
