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
package cc.acquized.ultimatereport.config;

import cc.acquized.ultimatereport.UltimateReport;
import cc.acquized.ultimatereport.libs.acquized.config.Parser;
import cc.acquized.ultimatereport.libs.acquized.config.UltimateConfig;
import cc.acquized.ultimatereport.libs.acquized.config.Value;

import java.io.File;

public class Config extends UltimateConfig {

    @Value(path = "UltimateReport.Prefix", color = true)
    public String prefix = "§7[§3UltimateReport§7] §r";

    @Value(path = "UltimateReport.Locale")
    public String locale = "en";

    @Value(path = "UltimateReport.Cooldown")
    public int cooldown = 60;

    @Value(path = "UltimateReport.ConvertServer")
    public String server = "eu";

    @Value(path = "UltimateReport.ClickableMsgs")
    public boolean clickableMsgs = true;

    @Value(path = "UltimateReport.Version")
    public String version = UltimateReport.getInstance().getDescription().getVersion();

    // -------------------------------

    @Value(path = "Logging.File")
    public boolean fileLogging = false;

    @Value(path = "Logging.Console")
    public boolean consoleLogging = false;

    @Value(path = "Logging.MySQL")
    public boolean sqlLogging = false;

    // -------------------------------

    @Value(path = "MySQL.Adress")
    public String adress = "127.0.0.1";

    @Value(path = "MySQL.Port")
    public int port = 3306;

    @Value(path = "MySQL.Database")
    public String database = "UltimateReport";

    @Value(path = "MySQL.Username")
    public String username = "root";

    @Value(path = "MySQL.Password")
    public String password = "passw0rd";

    // -------------------------------

    private static Config instance;

    public Config() {
        super(new File(UltimateReport.getInstance().getDataFolder(), "config.yml"));
        instance = this;
        Parser.parse(this, getConfig());
        if(!version.equalsIgnoreCase(UltimateReport.getInstance().getDescription().getVersion())) {
            regenerate();
        }
    }

    public static Config getInstance() {
        return instance;
    }

}
