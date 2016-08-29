package me.mcmainiac.KingUndercover;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("unused")
class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Game g = Main.game;

        if (!g.getState().equals(GameState.PREMATCH))
            e.getPlayer().kickPlayer("§cEntschuldige, aber das Spiel hat bereits begonnen!");

        Bukkit.broadcastMessage("§6" + e.getPlayer().getDisplayName() + " §7hat das Spiel betreten!");

        if (Bukkit.getOnlinePlayers().size() == Game.minplayers)
            g.start();
        else
            Bukkit.broadcastMessage("§a" + Bukkit.getOnlinePlayers().size() + "§7/§6" + Game.minplayers + " §aSpieler");
    }

    @EventHandler
    public void onPlayerAttackPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
            return;

        Player attacker = (Player) e.getDamager();
        Player defender = (Player) e.getEntity();

        if (defender.isDead() || defender.getHealth() == 0.0) {
            Bukkit.broadcastMessage("§6" + attacker.getDisplayName() + "§7 killed §8" + defender.getDisplayName());
        }
    }
}
