package me.mcmainiac.KingUndercover;

import me.mcmainiac.ArenaManager.api.MiniGamePlugin;
import me.mcmainiac.ArenaManager.api.utils.ArenaType;
import org.bukkit.Bukkit;

public class Main extends MiniGamePlugin {
    public static Game game;

    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        type = ArenaType.TEAM24;

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        game = new Game(this);
    }

    @Override
    public void onDisable() {
        game.exit();
    }
}
