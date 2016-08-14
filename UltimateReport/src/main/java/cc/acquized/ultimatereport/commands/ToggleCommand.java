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
package cc.acquized.ultimatereport.commands;

import cc.acquized.ultimatereport.hub.NotificationHub;
import cc.acquized.ultimatereport.i18n.I18n;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("togglemsg", null, "togglereports");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("ultimatereport.commands.togglemsg")) {
                if(args.length == 0) {
                    if(!NotificationHub.noNotifications.contains(p)) {
                        NotificationHub.noNotifications.add(p);
                        p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Toggle.Off")));
                    } else {
                        NotificationHub.noNotifications.remove(p);
                        p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Toggle.On")));
                    }
                } else {
                    p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Toggle.Syntax")));
                }
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.NoPermissions")));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.NoConsole")));
        }
    }

}
