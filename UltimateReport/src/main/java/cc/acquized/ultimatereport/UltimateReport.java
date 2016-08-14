/* Copyright 2016 Acquized
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.acquized.ultimatereport;

import cc.acquized.ultimatereport.api.UltimateReportAPI;
import cc.acquized.ultimatereport.commands.LatestReportsCommand;
import cc.acquized.ultimatereport.commands.ReportCommand;
import cc.acquized.ultimatereport.commands.ToggleCommand;
import cc.acquized.ultimatereport.commands.UltimateReportCommand;
import cc.acquized.ultimatereport.config.Config;
import cc.acquized.ultimatereport.hub.NotificationHub;
import cc.acquized.ultimatereport.i18n.I18n;
import cc.acquized.ultimatereport.libs.mcuuid.handlers.BungeeHandler;
import cc.acquized.ultimatereport.sql.SQLManager;
import net.mcapi.uuid.ServerRegion;
import net.mcapi.uuid.UUIDAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

public class UltimateReport extends Plugin {

    public static String pr = "&7[&3UltimateReport&7] &r";
    private UltimateReportAPI api;
    private static UltimateReport instance;

    @Override
    public void onEnable() {
        instance = this;
        new Config().load();
        pr = Config.getInstance().prefix;
        I18n.loadLocales();
        SQLManager.connect();
        api = new UltimateReportAPI();
        registerListeners();
        registerCommands();
        UUIDAPI.setHandler(new BungeeHandler(this));
        UUIDAPI.setRegion(ServerRegion.valueOf(Config.getInstance().server.toUpperCase()));
    }

    @Override
    public void onDisable() {
        SQLManager.disconnect();
        instance = null;
    }

    private void registerCommands() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerCommand(this, new ReportCommand());
        pm.registerCommand(this, new UltimateReportCommand());
        pm.registerCommand(this, new LatestReportsCommand());
        pm.registerCommand(this, new ToggleCommand());
    }

    private void registerListeners() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerListener(this, new Listener() {
            @EventHandler
            public void onQuit(PlayerDisconnectEvent e) {
                ProxiedPlayer p = e.getPlayer();
                if(NotificationHub.noNotifications.contains(p)) {
                    NotificationHub.noNotifications.remove(p);
                }
            }
        });

    }

    /**
     * Gets the UltimateReport API Class
     * @return the UltimateReport API
     */
    @NotNull
    public UltimateReportAPI getAPI() {
        return api;
    }

    @NotNull
    public static UltimateReport getInstance() {
        return instance;
    }

}
