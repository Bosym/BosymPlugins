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
package cc.acquized.ultimatereport.utils;

import net.mcapi.uuid.UUIDAPI;

import java.util.UUID;

public class ConverterCache {

    // This uses McUUID Lib - without this lib, the file size would shrink to 50kb lol
    public static UUID convertToUUID(String username) {
        /*
        try {
            URL url = new URL("https://" + Main.getConfig().getConfig().getString("UltimateReport.ConvertServer").toLowerCase() + ".mc-api.net/v3/uuid/" + username + "/csv");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String[] args = reader.readLine().split(",");
            return UUID.fromString(args[3]); // Maybe i should use args[2] (UUID without dashes), i don't know how UUID#fromString works.
        } catch (IOException ex) {
            ex.printStackTrace();
            return UUID.randomUUID(); // Currently only returns a pseudo UUID
        }
        */
        return UUIDAPI.getUUID(username); // Using McUUID Lib
    }

    // This uses McUUID Lib - without this lib, the file size would shrink to 50kb lol
    public static String convertToUsername(UUID uuid) {
        /*
        try {
            URL url = new URL("https://" + Main.getConfig().getConfig().getString("UltimateReport.ConvertServer").toLowerCase() + ".mc-api.net/v3/name/" + uuid.toString() + "/csv");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String[] args = reader.readLine().split(",");
            return args[1];
        } catch (IOException ex) {
            ex.printStackTrace();
            return Arrays.toString(new Byte[2147483647]); // Currently only returns a pseudo String
        }
        */
        return UUIDAPI.getUsername(uuid.toString().replaceAll("-", "")); // Using McUUID Lib
    }

}
