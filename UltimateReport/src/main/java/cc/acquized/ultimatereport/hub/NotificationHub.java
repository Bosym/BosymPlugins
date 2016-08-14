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
package cc.acquized.ultimatereport.hub;

import cc.acquized.ultimatereport.config.Config;
import cc.acquized.ultimatereport.i18n.I18n;
import cc.acquized.ultimatereport.logger.Logger;
import cc.acquized.ultimatereport.sql.SQLManager;
import cc.acquized.ultimatereport.utils.ConverterCache;
import cc.acquized.ultimatereport.utils.Report;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class NotificationHub {

    public static List<ProxiedPlayer> noNotifications = new ArrayList<>();

    public static void proceedReport(Report r) {
        boolean staffOnline = false;

        String reporter = ConverterCache.convertToUsername(r.getReporter());
        String target = ConverterCache.convertToUsername(r.getTarget());
        String msg = I18n.getMessage("UltimateReport.Notification.Staff").replaceAll("%target%", target).replaceAll("%reporter%", reporter).replaceAll("%reason%", r.getReason()).replaceAll("%server%",
                ProxyServer.getInstance().getPlayer(target).getServer().getInfo().getName());

        for(ProxiedPlayer o : ProxyServer.getInstance().getPlayers()) {
            if(o.hasPermission("ultimatereport.reports.notify")) {
                if((!staffOnline) && (!noNotifications.contains(o))) {
                    staffOnline = true;
                }
            }
        }

        if(staffOnline) {
            for(ProxiedPlayer o : ProxyServer.getInstance().getPlayers()) {
                if(o.hasPermission("ultimatereport.reports.notify")) {
                    if(Config.getInstance().clickableMsgs) {
                        o.sendMessage(new ComponentBuilder(msg).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "server " + ProxyServer.getInstance().getPlayer(target).getServer().getInfo().getName()))
                        .create());
                    } else {
                        o.sendMessage(TextComponent.fromLegacyText(msg));
                    }
                }
            }
        } else {
            SQLManager.update("INSERT INTO `waitingqueue`(repDate, reporter, target, reason) VALUES (NOW(), '" + r.getReporter().toString().replaceAll("-", "") + "', '" + r.getTarget().toString().replaceAll("-", "") + "', '" + r.getReason() + "');");
        }

        if(Config.getInstance().fileLogging) {
            Logger.logFile(I18n.getMessage("UltimateReport.Notification.File").replaceAll("%time%", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())).replaceAll("%target%", target).replaceAll("%reporter%", reporter).replaceAll("%reason%", r.getReason()).replaceAll("%server%", ProxyServer.getInstance().getPlayer(target).getServer().getInfo().getName()));
        }

        if(Config.getInstance().consoleLogging) {
            ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Notification.Console").replaceAll("%target%", target).replaceAll("%reporter%", reporter).replaceAll("%reason%", r.getReason()).replaceAll("%server%", ProxyServer.getInstance().getPlayer(target).getServer().getInfo().getName())));
        }

        if(Config.getInstance().sqlLogging) {
            SQLManager.update("INSERT INTO `ultimatereport`(repDate, reporter, target, reason) VALUES (NOW(), '" + r.getReporter().toString().replaceAll("-", "") + "', '" + r.getTarget().toString().replaceAll("-", "") + "', '" + r.getReason() + "');");
        }

    }





}
