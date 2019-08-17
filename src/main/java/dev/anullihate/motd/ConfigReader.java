package dev.anullihate.motd;

import cn.nukkit.utils.Config;

import java.util.List;

public class ConfigReader {
    private final Motd plugin;
    private static Config config;

    public ConfigReader(Motd plugin) {
        this.plugin = plugin;
        config = plugin.pluginConfig;
    }

    // Server MOTD

    public boolean isServerMOTDEnabled() {
        return config.getBoolean("Server-MOTD.enabled");
    }

    public int getServerMOTDChangeInterval() {
        return config.getInt("Server-MOTD.change-interval");
    }

    public List<String> getServerMOTDMessages() {
        return config.getStringList("Server-MOTD.messages");
    }

    // Join MOTD

    public boolean isJoinMOTDEnabled() {
        return config.getBoolean("Join-MOTD.enabled");
    }

    public String getJoinMOTDType() {
        return config.getString("Join-MOTD.type");
    }

    public String getJoinMOTDMessage() {
        return config.getString("Join-MOTD.message");
    }

    public String getJoinMOTDSubMessage() {
        return config.getString("Join-MOTD.sub-message");
    }
}
