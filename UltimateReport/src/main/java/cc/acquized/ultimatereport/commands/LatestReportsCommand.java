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

import cc.acquized.ultimatereport.UltimateReport;
import cc.acquized.ultimatereport.i18n.I18n;
import cc.acquized.ultimatereport.utils.ConverterCache;
import cc.acquized.ultimatereport.utils.Report;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LatestReportsCommand extends Command {

    public LatestReportsCommand() {
        super("latest", null, "latestreports", "reports");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("ultimatereport.commands.latest")) {
                int amount;
                if(args.length == 0) {
                    amount = 5;
                } else if(args.length == 1) {
                    try {
                        amount = Integer.parseInt(args[0]);
                    } catch (NumberFormatException ex) {
                        p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Latest.Invalid")));
                        return;
                    }
                } else {
                    p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Latest.Syntax")));
                    return;
                }
                p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Latest.Waiting")));
                Report[] reports = UltimateReport.getInstance().getAPI().getLatestReports(amount);
                p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Latest.Header").replaceAll("%amount%", String.valueOf(amount))));
                for(Report r : reports) {
                    String target = ConverterCache.convertToUsername(r.getTarget());
                    String reporter = ConverterCache.convertToUsername(r.getReporter());
                    p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Latest.Report").replaceAll("%target%", target).replaceAll("%reporter%", reporter).replaceAll("%reason%", r.getReason())));
                }
            } else {
                p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.NoPermissions")));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.NoConsole")));
        }
    }

}
