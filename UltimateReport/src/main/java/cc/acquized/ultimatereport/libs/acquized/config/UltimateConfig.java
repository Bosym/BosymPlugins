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
package cc.acquized.ultimatereport.libs.acquized.config;

import cc.acquized.ultimatereport.UltimateReport;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class UltimateConfig {

    private final File file;
    private Configuration config;

    public UltimateConfig(File file) {
        this.file = file;
        load();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void load() {
        try {
            if(!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()) {
                Files.copy(UltimateReport.getInstance().getResourceAsStream(file.getName()), file.toPath());
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload() {
        config = null;
        load();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void regenerate() {
        file.delete();
        load();
    }

    public File getFile() {
        return file;
    }

    public Configuration getConfig() {
        return config;
    }

}