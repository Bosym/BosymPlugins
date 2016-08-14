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
package cc.acquized.ultimatereport.i18n;

import cc.acquized.ultimatereport.UltimateReport;
import cc.acquized.ultimatereport.config.Config;
import net.md_5.bungee.api.ChatColor;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    public static ResourceBundle bundle;
    public static List<String> supported = Arrays.asList("en", "de");

    public static void loadLocales() {
        I18nFiles.copy();
        try {
            ClassLoader loader = new URLClassLoader(new URL[]{ I18nFiles.getDirectory().toURI().toURL() });
            bundle = ResourceBundle.getBundle("messages", new Locale(Config.getInstance().locale), loader);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    public static String getMessage(String path) {
        try {
            return ChatColor.translateAlternateColorCodes('&', UltimateReport.pr + bundle.getString(path));
        } catch (NullPointerException ex) {
            // key could not be found
            return null;
        }
    }

}
