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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class I18nFiles {

    private static File directory = new File(UltimateReport.getInstance().getDataFolder() + File.separator + "locale");

    public static void copy() {
        try {
            if(!directory.isDirectory()) {
                directory.mkdirs();
            }
            for(String s : I18n.supported) {
                File f = new File(directory, "messages_" + s + ".properties");
                if(!f.exists()) {
                    Files.copy(UltimateReport.getInstance().getResourceAsStream(f.getName()), f.toPath());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static File getDirectory() {
        return directory;
    }

}
