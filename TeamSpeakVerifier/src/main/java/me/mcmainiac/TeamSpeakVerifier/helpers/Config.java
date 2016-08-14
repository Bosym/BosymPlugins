package me.mcmainiac.TeamSpeakVerifier.helpers;

import com.google.common.io.ByteStreams;
import me.mcmainiac.TeamSpeakVerifier.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class Config {
    private static Configuration config;

    public static void init(Main m) throws Exception {
        if (!m.getDataFolder().exists()) {
            if (!m.getDataFolder().mkdir()) {
                Log.severe("Failed to create plugin data folder!");
                Main.forceDisable();
            }
        }

        File configfile = new File(m.getDataFolder(), "config.yml");
        if (!configfile.exists()) {
            try {
                if (configfile.createNewFile()) {
                    try (InputStream is = m.getResourceAsStream("config.yml");
                         OutputStream os = new FileOutputStream(configfile)) {
                        ByteStreams.copy(is, os);
                    }
                } else {
                    Log.severe("Failed to create config.yml in plugin folder!");
                    Main.forceDisable();
                }
            } catch (IOException e) {
                throw new Exception("Could not create config.yml!", e);
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
        } catch (IOException e) {
            throw new Exception("Could not load config.yml!", e);
        }
    }

    public static String getString(String path) {
        return config.getString(path);
    }

    public static int getInt(String path) {
        return config.getInt(path);
    }

    public static boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public static Object get(String path) {
        return config.get(path);
    }

    public static class Exception extends java.lang.Exception {
        Exception(String message, Throwable previous) { super(message, previous); }
    }
}
