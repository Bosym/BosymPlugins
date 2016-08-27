package me.mcmainiac.KingUndercover;

import me.mcmainiac.ArenaManager.api.ArenaType;
import me.mcmainiac.ArenaManager.api.MiniGame;
import me.mcmainiac.ArenaManager.api.MiniGamePlugin;

@MiniGame(
        name = "KingUndercover",
        type = ArenaType.TEAM24
)
public class Main extends MiniGamePlugin {
    @Override
    public void onEnable() {
        log.info("Enabled");
    }
}
