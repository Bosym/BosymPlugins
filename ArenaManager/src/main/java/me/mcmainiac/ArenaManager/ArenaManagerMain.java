package me.mcmainiac.ArenaManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ArenaManagerMain extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        log.info("Enabled");
    }

    @Override
    public void onDisable() {
        log.info("Disabled");
    }
}
