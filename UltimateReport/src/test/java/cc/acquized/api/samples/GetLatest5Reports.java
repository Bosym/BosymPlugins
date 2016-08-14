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
package cc.acquized.api.samples;

import cc.acquized.ultimatereport.UltimateReport;
import cc.acquized.ultimatereport.api.AccessableAPI;
import cc.acquized.ultimatereport.utils.Report;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Example to get the latest 5 Reports
 */
public class GetLatest5Reports implements AccessableAPI {

    public static void getLatestFiveReports() {
        Report[] reports = UltimateReport.getInstance().getAPI().getLatestReports(5);
        for(Report r : reports) {
            ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText("A Report m"));
        }
    }

}
