package me.mcmainiac.TeamSpeakVerifier;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import me.mcmainiac.TeamSpeakVerifier.db.MySQLDB;
import me.mcmainiac.TeamSpeakVerifier.helpers.Config;
import me.mcmainiac.TeamSpeakVerifier.helpers.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.HashMap;

public class TS3Bot {
    private final TS3Query query;

    private HashMap<Integer, VerifyWorker> workers = new HashMap<>();
    private HashMap<String, VerifyWorker> codes = new HashMap<>();

    public TS3Bot() {
        query = Main.getTS3Query(Main.getTS3Config());

        final TS3Api api = Main.getTS3Api(query);
        api.registerEvent(TS3EventType.SERVER);
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onClientJoin(ClientJoinEvent e) {
                super.onClientJoin(e);

                final Integer id = e.getClientId();
                final VerifyWorker worker = new VerifyWorker(query, id);

                workers.put(id, worker);

                Thread t = new Thread(worker);
                t.start();
            }

            @Override
            public void onClientLeave(ClientLeaveEvent e) {
                super.onClientLeave(e);

                workers.remove(e.getClientId());
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

                db.insert(Config.getString("database.table.name"), values);
            } catch (SQLException e) {
                Log.severe("An sql exception occurred while verifying a code:");
                Log.severe(e.getMessage());
                if (Config.getBoolean("debug"))
                    e.printStackTrace();
                return false;
            }

            this.codes.remove(code);
            return true;
        } else
            return false;
    }

    public void addCode(String code, VerifyWorker worker) {
        this.codes.put(code, worker);
    }
}
