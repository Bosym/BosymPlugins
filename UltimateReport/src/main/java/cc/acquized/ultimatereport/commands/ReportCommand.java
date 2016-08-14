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
import cc.acquized.ultimatereport.config.Config;
import cc.acquized.ultimatereport.hub.NotificationHub;
import cc.acquized.ultimatereport.i18n.I18n;
import cc.acquized.ultimatereport.utils.Report;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class ReportCommand extends Command {

    private List<ProxiedPlayer> inCooldown = new ArrayList<>();

    public ReportCommand() {
        super("report", null, "ticket", "reportuser");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            final ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("ultimatereport.commands.report")) {
                if(args.length >= 1) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    if(target != null) {
                        if(!target.getName().equals(p.getName())) { // Using equals and not equalsIgnoreCase for cracked servers
                            if(!inCooldown.contains(p)) {
                                if(!target.hasPermission("ultimatereport.bypass.report")) {
                                    String reason = args[1];
                                    for(int i = 2; i < args.length; i++) {
                                        reason = reason + " " + args[i];
                                    }
                                    Report r = new Report(p, target, reason);
                                    try {
                                        NotificationHub.proceedReport(r);
                                    } catch (Exception ex) {
                                        p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Report.Failure")));
                                        ex.printStackTrace();
                                        return;
                                    }
                                    if(!p.hasPermission("ultimatereport.bypass.cooldown")) {
                                        inCooldown.add(p);
                                        ProxyServer.getInstance().getScheduler().schedule(UltimateReport.getInstance(), new Runnable() {
                                            @Override
                                            public void run() {
                                                inCooldown.remove(p);
                                            }
                                        }, Config.getInstance().cooldown, TimeUnit.SECONDS);
                                    }
                                    p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Report.Success")));
                                } else {
                                    p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Report.NoBypass")));
                                }
                            } else {
                                p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Report.Cooldown")));
                            }
                        } else {
                            p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Report.ForeverAlone")));
                        }
                    } else {
                        p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Report.UserOffline")));
                    }
                } else {
                    p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Report.Syntax")));
                }
            } else {
                p.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.NoPermissions")));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.NoConsole")));
        }
    }

}
