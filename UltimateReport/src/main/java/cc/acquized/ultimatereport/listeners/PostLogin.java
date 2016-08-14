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
package cc.acquized.ultimatereport.listeners;

import cc.acquized.ultimatereport.UltimateReport;
import cc.acquized.ultimatereport.hub.NotificationHub;
import cc.acquized.ultimatereport.i18n.I18n;
import cc.acquized.ultimatereport.utils.Report;
import net.mcapi.uuid.UUIDAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLogin implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if((p.hasPermission("ultimatereport.offline.notify")) && (!NotificationHub.noNotifications.contains(p)) && (UltimateReport.getInstance().getAPI().getWaitingReports().length > 0)) {
            p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Notification.Join")));
            for(Report r : UltimateReport.getInstance().getAPI().getWaitingReports()) {
                p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Notification.Offline").replaceAll("%target%",
                UUIDAPI.getUsername(r.getTarget())).replaceAll("%reporter%", UUIDAPI.getUsername(r.getReporter())).replaceAll("%reason%", r.getReason())));
            }
        }
    }

}
