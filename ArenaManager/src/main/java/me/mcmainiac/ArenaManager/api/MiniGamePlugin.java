package me.mcmainiac.ArenaManager.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public abstract class MiniGamePlugin extends JavaPlugin {
    protected final static String name = MiniGamePlugin.class.getAnnotation(MiniGame.class).name();
    protected final static ArenaType type = MiniGamePlugin.class.getAnnotation(MiniGame.class).type();

    protected static Logger log = Logger.getLogger("Minigame");

    static {
        log.setUseParentHandlers(false);
        log.addHandler(new Handler() {
            private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

            @Override
            public void publish(LogRecord record) {
                StringBuilder sb = new StringBuilder();

                sb.append("[" + name + "]: ");
                sb.append(record.getMessage());

                System.out.println(sb.toString());
            }

            @Override
            public void flush() {}

            @Override
            public void close() throws SecurityException {}
        });
    }
}
