package me.mcmainiac.KingUndercover;

import me.mcmainiac.ArenaManager.api.ArenaType;
import me.mcmainiac.ArenaManager.api.MiniGamePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Main extends MiniGamePlugin {
    @Override
    public void onEnable() {
        name = this.getDescription().getName();
        type = ArenaType.TEAM24;

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        this.getCommand("").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                return false;
            }
        });

        new Game(this);
    }
}
