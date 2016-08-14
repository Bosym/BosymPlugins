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

import cc.acquized.ultimatereport.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("deprecation")
public class Connector {

    private final String adress;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private HikariDataSource dataSource;

    public Connector() {
        adress = Config.getInstance().adress;
        port = Config.getInstance().port;
        database = Config.getInstance().database;
        username = Config.getInstance().username;
        password = Config.getInstance().password;
    }

    public void connect() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:mysql://" + adress + ":" + port + "/" + database);
        cfg.setDriverClassName("com.mysql.jdbc.Driver");
        cfg.setUsername(username);
        cfg.setPassword(password);
        cfg.setMinimumIdle(5);
        cfg.setMaximumPoolSize(50);
        cfg.setConnectionTimeout(3000);
        dataSource = new HikariDataSource(cfg);
    }

    public void disconnect() {
        if((dataSource != null) && (!dataSource.isClosed())) {
            dataSource.close();
        }
    }

    public void update(String qry) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(qry);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet query(String qry) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(qry);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
