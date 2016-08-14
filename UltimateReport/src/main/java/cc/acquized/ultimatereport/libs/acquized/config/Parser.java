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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Open Source Java Config API using Annotations
 * @author Acquized
 */
public class Parser {

    @SuppressWarnings("unchecked")
    public static void parse(Object loader, Configuration config) {
        try {
            Class<?> clazz = loader.getClass();
            BasePath basePath = clazz.getAnnotation(BasePath.class);
            for (Field f : clazz.getDeclaredFields()) {
                Value annotation = f.getAnnotation(Value.class);
                if(annotation != null) {
                    f.setAccessible(true);
                    boolean isStatic = Modifier.isStatic(f.getModifiers()); // TODO: Why did i add a static checker?
                    Object value = config.get(basePath != null ? basePath.value() + "." + annotation.path(): annotation.path());
                    if(value != null) {
                        if(value instanceof String) {
                            value = annotation.color() ? ChatColor.translateAlternateColorCodes('&', (String) value) : value;
                        } else if(value instanceof List) {
                            int count = 0;
                            for(Object s : (List) value) {
                                if(s instanceof String) {
                                    s = annotation.color() ? ChatColor.translateAlternateColorCodes('&', (String) s) : value;
                                    ((List) value).set(count, s);
                                    count++;
                                }
                            }

                        }
                        if(!Modifier.isFinal(f.getModifiers())) {
                            f.set(loader, value);
                        }
                    } else {
                        config.set(basePath != null ? basePath.value() + "." + annotation.path() : annotation.path(), f.get(loader));
                    }
                }
            }
        } catch (IllegalAccessException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}