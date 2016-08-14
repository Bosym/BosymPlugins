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

import cc.acquized.ultimatereport.api.AccessableAPI;
import cc.acquized.ultimatereport.api.events.PlayerReportEvent;
import cc.acquized.ultimatereport.utils.ConverterCache;
import cc.acquized.ultimatereport.utils.Report;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ReportAPIExample implements AccessableAPI, Listener {

    @EventHandler
    public void onReport(PlayerReportEvent e) {
        Report r = e.getReport(); // Gets the Report from the Event
        if(ConverterCache.convertToUsername(r.getTarget()).equals("Acquized")) { // Checks if the Target is Acquized. It converts the UUID to a username using the built-in Converter Cache
            // Do something or
            e.setCancelled(true); // cancel the Event
        }
    }

}
