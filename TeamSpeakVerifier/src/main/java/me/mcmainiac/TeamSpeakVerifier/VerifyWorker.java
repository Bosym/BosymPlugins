package me.mcmainiac.TeamSpeakVerifier;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import me.mcmainiac.TeamSpeakVerifier.helpers.Config;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VerifyWorker implements Runnable {
    private final int clientid;
    private final TS3Query query;

    VerifyWorker(TS3Query _query, int _clientid) {
        this.query = _query;
        this.clientid = _clientid;
    }

    @Override
    public void run() {
        final TS3ApiAsync api = Main.getTS3ApiAsync(query);

        api.sendTextMessage(TextMessageTargetMode.CLIENT, clientid, Config.getString("teamspeak.messages.onjoin"));
        api.registerEvent(TS3EventType.TEXT_PRIVATE);
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                super.onTextMessage(e);

                if (!e.getTargetMode().equals(TextMessageTargetMode.CLIENT))
                    return;

                if (!e.getMessage().startsWith(Config.getString("teamspeak.command"))) {
                    api.sendTextMessage(TextMessageTargetMode.CLIENT, clientid, Config.getString("teamspeak.messages.oninvalidmessage"));
                    return;
                }

                String[] args = e.getMessage().split(" ");

                if (args.length != 2) {
                    api.sendTextMessage(TextMessageTargetMode.CLIENT, clientid, Config.getString("teamspeak.messages.ontoofewarguments"));
                    return;
                }

                if (ProxyServer.getInstance().getPlayer(args[1]).isConnected()) {
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[1]);

                    // TODO: create and store code
                    // TODO: send created code with message path: "minecraft.messages.invite"
                    // TODO: enter keep alive cycle and wait for code (until timeout) then send timeout message
                } else {
                    api.sendTextMessage(TextMessageTargetMode.CLIENT, clientid, Config.getString("teamspeak.messages.mcusernotonline"));
                    return;
                }
            }
        });
    }
}
