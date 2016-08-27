package me.mcmainiac.ArenaManager.api;

public class ArenaManager {
    private final MiniGameArena arena;

    public ArenaManager(MiniGameArena arena) {
        this.arena = arena;
    }

    public MiniGameArena getArena() {
        return this.arena;
    }
}
