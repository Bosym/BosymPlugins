package me.mcmainiac.TeamSpeakVerifier;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import me.mcmainiac.TeamSpeakVerifier.db.MySQLDB;
import me.mcmainiac.TeamSpeakVerifier.helpers.Config;
import me.mcmainiac.TeamSpeakVerifier.helpers.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.HashMap;

public class TS3Bot {
    private TS3Query query = null;

    private HashMap<String, VerifyWorker> codes = new HashMap<>();

    TS3Bot() {
        try {
            query = Main.getTS3Query(Main.getTS3Config());
        } catch (Exception e) {
            Log.severe("Failed to connect to teamspeak server!");
            if (Config.getBoolean("debug"))
                e.printStackTrace();
            Main.forceDisable();
            return;
        }

        final TS3Api api = Main.getTS3Api(query);
        api.registerEvent(TS3EventType.SERVER);
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onClientJoin(ClientJoinEvent e) {
                final Integer id = e.getClientId();
                final VerifyWorker worker = new VerifyWorker(query, id);
                Thread t = new Thread(worker);
                t.start();
            }
        });
    }

    public boolean verifyCode(ProxiedPlayer p, String code) {
        if (this.codes.containsKey(code))
            return false;

        VerifyWorker worker = this.codes.get(code);

        if (worker.verify(code)) {
            try {
                MySQLDB db = Main.getDb();

                HashMap<String, Object> values = new HashMap<>();
                values.put(Config.getString("database.table.mcuuidcol"), p.getUniqueId().toString());
                values.put(Config.getString("database.table.tsuidcol"), worker.getClientInfo().getUniqueIdentifier());

                if (db != null) {
                    db.insert(Config.getString("database.table.name"), values);
                } else {
                    throw new NullPointerException("Database is not set!");
                }
            } catch (SQLException e) {
                Log.severe("An sql exception occurred while verifying a code:");
                Log.severe(e.getMessage());
                if (Config.getBoolean("debug"))
                    e.printStackTrace();
                return false;
            } catch (NullPointerException e) {
                Log.severe("A NullPointerException occured!");
                if (Config.getBoolean("debug"))
                    e.printStackTrace();
                return false;
            }

            this.codes.remove(code);
            return true;
        } else
            return false;
    }

    void disconnect() {
        if (query != null)
            query.exit();
    }

    void addCode(String code, VerifyWorker worker) {
        this.codes.put(code, worker);
    }
}
