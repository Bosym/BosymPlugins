package me.mcmainiac.KingUndercover;

import me.mcmainiac.ArenaManager.api.MiniGamePlugin;
import org.bukkit.entity.Player;

import java.util.HashSet;

class Game {
    private MiniGamePlugin plugin;
    private HashSet<Player> players = new HashSet<Player>();

    public Game(MiniGamePlugin plugin) {
        this.plugin = plugin;
    }
}
