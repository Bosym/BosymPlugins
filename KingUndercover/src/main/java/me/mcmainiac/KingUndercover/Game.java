package me.mcmainiac.KingUndercover;

import be.maximvdw.titlemotd.ui.Title;
import me.mcmainiac.ArenaManager.api.MiniGamePlugin;
import me.mcmainiac.ArenaManager.api.utils.GameCountdown;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.logging.Logger;

class Game {
    private MiniGamePlugin plugin;
    private HashSet<Player> players = new HashSet<Player>();
    private GameState state = GameState.PREMATCH;
    private Logger log;
    private MessageProccessor msg = new MessageProccessor();
    private GameCountdown cd = new GameCountdown();

    public Game(MiniGamePlugin plugin) {
        this.plugin = plugin;
        this.log = plugin.log;

        this.cd.setStart(10);
        this.cd.setTickAction(new GameCountdown.TickAction() {
            public void run() {
                if (this.getCurrent() > 0) {
                    for (Player p : players)
                        p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(3, Note.Tone.C));
                    Title t = new Title(String.valueOf(this.getCurrent()) + "s", "bis zum Start...");
                    t.setTitleColor(ChatColor.GREEN);
                    t.setSubtitleColor(ChatColor.GOLD);
                    t.broadcast();
                } else {
                    this.run();
                }
            }
        });
    }

    public void start() {
        state = GameState.STARTING;
        Thread t = new Thread(this.cd);
        t.start();
    }

    private void run() {
        state = GameState.RUNNING;
        Title t = new Title("Das Spiel beginnt!", "Besiege das gegnerische Team, indem du ihren König tötest und dann alle anderen tötest!");
        t.setTitleColor(ChatColor.GOLD);
        t.setSubtitleColor(ChatColor.DARK_GRAY);
        t.broadcast();
    }

    public void end() {
        switch (state) {
            case PREMATCH:
                log.info("Game start aborted.");
                state = GameState.POSTMATCH;
                break;
            case STARTING:
                cd.stop();
                log.info("Game countdown stopped and game start aborted.");
                state = GameState.POSTMATCH;
                break;
            case RUNNING:
                state = GameState.POSTMATCH;
                log.info("Game has been ended.");
                break;
            case POSTMATCH:
                log.info("Game has already ended.");
                break;
        }
    }
}
