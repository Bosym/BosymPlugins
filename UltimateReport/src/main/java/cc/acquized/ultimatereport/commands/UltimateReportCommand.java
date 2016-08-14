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

import cc.acquized.ultimatereport.config.Config;
import cc.acquized.ultimatereport.i18n.I18n;
import cc.acquized.ultimatereport.logger.Logger;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

@SuppressWarnings("ConstantConditions")
public class UltimateReportCommand extends Command {

    public UltimateReportCommand() {
        super("ultimatereport", null, "ultimatereporter");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("ultimatereport.commands.main")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    try {
                        Config.getInstance().reload();
                    } catch (Exception ex) {
                        sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Main.Failure")));
                        ex.printStackTrace();
                        return;
                    }
                    sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Main.Success")));
                } else if(args[0].equalsIgnoreCase("debug")) {
                    if(!(sender instanceof ProxiedPlayer)) {
                        Logger.debug = !Logger.debug;
                        sender.sendMessage(Logger.debug ? TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Main.On")) : TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Main.Off")));
                    } else {
                        sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.OnlyConsole")));
                    }
                } else {
                    sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Main.Syntax")));
                }
            } else {
                sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.Command.Main.Syntax")));
            }
        } else {
            sender.sendMessage(TextComponent.fromLegacyText(I18n.getMessage("UltimateReport.General.NoPermissions")));
        }
    }

}
