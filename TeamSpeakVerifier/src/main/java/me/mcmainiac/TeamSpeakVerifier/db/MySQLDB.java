package me.mcmainiac.TeamSpeakVerifier.db;

import me.mcmainiac.TeamSpeakVerifier.helpers.Config;
import me.mcmainiac.TeamSpeakVerifier.helpers.Log;

import java.sql.*;
import java.util.HashMap;

public class MySQLDB {
    private String host = "localhost",
            username = "root",
            password = "",
            name = "";
    private int port = 3600;

    private boolean connected = false,
            reset = true;

    private String selectors = "",
            lastQuery = "";

    private Connection con;

    public MySQLDB(String host, int port, String username, String password, String name) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    private Class<?> checkDriver() throws ClassNotFoundException {
        return Class.forName("com.mysql.jdbc.Driver");
    }

    public void connect() throws SQLException, ClassNotFoundException {
        if (!connected) {
            this.checkDriver();

            con = this.getConnection();

            connected = true;
        }
    }

    public void disconnect() throws SQLException {
        if (connected) {
            con.close();

            connected = false;
        }
    }

    public void where(String col, Object value) throws SQLException {
        if (!reset)
            this.reset();

        selectors += " AND `" + col + "` = '" + value.toString() + "'";
    }

    public void orWhere(String col, Object value)  throws  SQLException {
        if (!reset)
            this.reset();

        selectors += " OR `" + col + "` = '" + value.toString() + "'";
    }

    public ResultSet getOne(String table) throws SQLException {
        String sql = "SELECT * FROM `" + table + "` WHERE ";

        selectors = selectors.substring(1); // truncate first space
        sql += selectors.substring(selectors.indexOf(' ')); // truncate first AND / OR

        sql += " LIMIT 1"; // just one row

        reset = false;
        lastQuery = sql;

        if (Config.getBoolean("debug"))
            Log.info("Executing query: \"" + sql + "\"");
        Statement stmt = con.createStatement();
        return stmt.executeQuery(sql);
    }

    public int update(String table, HashMap<String, Object> values) throws SQLException {
        String sql = "UPDATE `" + table + "` SET ";

        for (String key : values.keySet())
            sql += key + " = '" + values.get(key) + "' ";

        if (selectors.length() > 0) {
            sql += "WHERE ";
            selectors = selectors.substring(1); // truncate first space
            sql += selectors.substring(selectors.indexOf(' ')); // truncate first AND / OR
        }

        reset = false;
        lastQuery = sql;
        if (Config.getBoolean("debug"))
            Log.info("Executing query: \"" + sql + "\"");
        Statement stmt = con.createStatement();
        return stmt.executeUpdate(sql);
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public boolean isConnected() {
        return this.connected;
    }

    private void reset() throws SQLException {
        selectors = "";
        reset = true;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name, username, password);
    }
}
