package me.mcmainiac.Forum.helpers;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
    private static Logger log = Logger.getLogger("Minecraft");

    private static final String prefix = "[Forum]: ";

    static {
        log.setUseParentHandlers(false);
        log.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                System.out.println(prefix + record.getMessage());
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        });
    }

    public static void info(String msg) {
        log.info(msg);
    }

    public static void severe(String msg) {
        log.severe(msg);
    }
}
