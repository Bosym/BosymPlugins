package me.mcmainiac.TeamSpeakVerifier;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import me.mcmainiac.TeamSpeakVerifier.helpers.Config;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class VerifyWorker implements Runnable {
    private final int clientid;
    private final TS3Query query;
    private String code;
    private boolean run = true;

    VerifyWorker(TS3Query _query, int _clientid) {
        this.query = _query;
        this.clientid = _clientid;
    }

    @Override
    public void run() {
        if (!run) return;

        run = false;

        final TS3ApiAsync api = Main.getTS3ApiAsync(query);

        api.sendTextMessage(TextMessageTargetMode.CLIENT, clientid, Config.getString("teamspeak.messages.onjoin"));
        api.registerEvent(TS3EventType.TEXT_PRIVATE);
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
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

                    code = UUID.randomUUID().toString().substring(0, 5);

                    String message = Config.getString("minecraft.messages.invite");
                    message.replace("/(\\%CODE\\%)/", code);
                    p.sendMessage(new TextComponent(message));

                    Main.getBot().addCode(code, VerifyWorker.this);
                } else {
                    api.sendTextMessage(TextMessageTargetMode.CLIENT, clientid, Config.getString("teamspeak.messages.mcusernotonline"));
                    return;
                }
            }
        });
    }

    public boolean verify(String code) {
        if (code.equals(this.code))
            return true;
        else
            return false;
    }

    public ClientInfo getClientInfo() {
        final TS3Api api = Main.getTS3Api(query);

        return api.getClientInfo(clientid);
    }
}
