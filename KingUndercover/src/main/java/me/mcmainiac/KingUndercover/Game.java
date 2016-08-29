package me.mcmainiac.KingUndercover;

import be.maximvdw.titlemotd.ui.Title;
import me.mcmainiac.ArenaManager.api.MiniGamePlugin;
import me.mcmainiac.ArenaManager.api.utils.GameCountdown;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.logging.Logger;

class Game {
    public static final int minplayers = 2;

    private GameState state = GameState.PREMATCH;
    private Logger log;
    private MiniGamePlugin plugin;
    private MessageProccessor msg = new MessageProccessor();
    private GameCountdown cd = new GameCountdown();
    private HashMap<Player, PlayerState> players = new HashMap<Player, PlayerState>();

    public Game(MiniGamePlugin plugin) {
        this.plugin = plugin;
        this.log = plugin.log;

        this.cd.setStart(5);
        this.cd.setSleepTime(2000);
        this.cd.setDelay(5000);
        this.cd.setTickAction(new GameCountdown.TickAction() {
            public void run() {
                if (this.getCurrent() == 5) {
                    Bukkit.broadcastMessage("§aDas Spiel beginnt in");
                    Bukkit.broadcastMessage("§65");
                } else if (this.getCurrent() > 1) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.C));
                    /*Title t = new Title("§6§l" + String.valueOf(this.getCurrent()), "§abis zum Start...");
                    t.broadcast();*/
                    Bukkit.broadcastMessage("§6" + this.getCurrent());
                } else {
                    Game.this.run();
                }
            }
        });
    }

    public void start() {
        state = GameState.STARTING;
        Thread t = new Thread(this.cd);
        t.start();
    }

    public void addPlayer(Player p) {
        if (!state.equals(GameState.PREMATCH))
            return;
    }

    public void removePlayer(Player p) {
        if (!state.equals(GameState.PREMATCH))
            return;

        players.remove(p);
    }

    private void run() {
        state = GameState.RUNNING;
        for (Player p : Bukkit.getOnlinePlayers())
            p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
        Title t = new Title("§6Das Spiel beginnt!", "§7Besiege den gegnerischen König!");
        t.broadcast();
    }

    public GameState getState() {
        return this.state;
    }

    public void end() {
        switch (state) {
            case PREMATCH:
                log.info("Game start aborted.");
                state = GameState.POSTMATCH;

                Bukkit.broadcastMessage("§cDas Spiel wurde abgebrochen!");
                break;
            case STARTING:
                cd.stop();
                log.info("Game countdown stopped and game start aborted.");
                state = GameState.POSTMATCH;

                Bukkit.broadcastMessage("§cDer Spielstart wurde abgebrochen!");
                break;
            case RUNNING:
                state = GameState.POSTMATCH;
                log.info("Game has been ended.");

                Bukkit.broadcastMessage("§cDas Spiel wurde abgebrochen!");
                break;
            case POSTMATCH:
                log.info("Game has already ended.");

                Bukkit.broadcastMessage("§cDas Spiel ist zu ende!");
                break;
        }

        Bukkit.broadcastMessage("§8Du wirst in 10s zurück zum Lobby Server geschickt.");
        Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers())
                    p.performCommand("server lobby");

                Game.this.exit();
            }
        }, 10000);
    }

    public void exit() {
        Bukkit.getServer().shutdown();
    }
}
