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
package cc.acquized.ultimatereport.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager {

    private static Connector connector = new Connector();

    public static void update(String qry) {
        connector.update(qry);
    }

    public static ResultSet query(String qry) {
        return connector.query(qry);
    }

    public static Connection getConnection() {
        try {
            return connector.getConnection();
        } catch (SQLException ex) {
            return null;
        }
    }

    public static void connect() {
        connector.connect();
        update("CREATE TABLE IF NOT EXISTS `ultimatereport` (repDate DATETIME, reporter VARCHAR(64), target VARCHAR(64), reason VARCHAR(64));");
        update("CREATE TABLE IF NOT EXISTS `waitingqueue` (repDate DATETIME, reporter VARCHAR(64), target VARCHAR(64), reason VARCHAR(64));");
    }

    public static void disconnect() { connector.disconnect(); }

    /*public static void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }
    Useless because Hikari automaticly closes them after a time. */

}
