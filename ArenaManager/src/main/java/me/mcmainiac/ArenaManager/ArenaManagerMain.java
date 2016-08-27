package me.mcmainiac.ArenaManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ArenaManagerMain extends JavaPlugin {
    private static final Logger log = Logger.getLogger("ArenaManager");

    static {
        log.setUseParentHandlers(false);
        log.addHandler(new Handler() {
            private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

            @Override
            public void publish(LogRecord record) {
                StringBuilder sb = new StringBuilder();
                sb.append("[ArenaManager]: ");
                sb.append(record.getMessage());

                System.out.println(sb.toString());
            }

            @Override
            public void flush() {}

            @Override
            public void close() throws SecurityException {}
        });
    }

    @Override
    public void onEnable() {
        log.info("Enabled");
    }

    @Override
    public void onDisable() {
        log.info("Disabled");
    }
}
