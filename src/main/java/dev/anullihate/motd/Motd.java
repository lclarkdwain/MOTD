package dev.anullihate.motd;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import dev.anullihate.motd.tasks.ServerMOTDTask;

public class Motd extends PluginBase implements Listener {

    public PlaceholderAPI placeholderAPI;

    public Config pluginConfig;
    public ConfigReader configReader;

    public void onEnable() {
        saveDefaultConfig();
        pluginConfig = getConfig();

        configReader = new ConfigReader(this);

        if (pluginConfig.getBoolean("Join-MOTD.use-placeholder")) {
            if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
                this.getServer().getLogger().critical("using placeholder is Enabled but no PlaceholderAPI found!");
                this.getServer().getPluginManager().disablePlugin(this);
            } else {
                placeholderAPI = PlaceholderAPI.getInstance();
            }
        }

        if (this.isEnabled()) {
            if (configReader.isServerMOTDEnabled()) {
                this.getServer().getScheduler().scheduleRepeatingTask(new ServerMOTDTask(this), configReader.getServerMOTDChangeInterval() * 20);
            }

            this.getServer().getPluginManager().registerEvents(this, this);
        }
    }

    @EventHandler
    public void onDataPacketReceive(DataPacketReceiveEvent event) {
        Player player = event.getPlayer();
        if (event.getPacket() instanceof SetLocalPlayerAsInitializedPacket && configReader.isJoinMOTDEnabled()) {
            if (configReader.getJoinMOTDType().equals("single-world")) {
                (new NukkitRunnable() {
                    public void run() {
                        messageSender(player, configReader.getJoinMOTDMessage(), configReader.getJoinMOTDSubMessage());
                    }
                }).runTaskLater(this, 40);
            } else {
                String initialWorld = getServer().getProperty("level-name").toString();
                String message = pluginConfig.getString("Join-MOTD.multi-world." + initialWorld + ".message");
                String subMessage = pluginConfig.getString("Join-MOTD.multi-world." + initialWorld + ".sub-message");
                (new NukkitRunnable() {
                    public void run() {
                        messageSender(player, message, subMessage);
                    }
                }).runTaskLater(this, 40);
            }

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityLevelChange(EntityLevelChangeEvent event) {
        Entity entity = event.getEntity();
        Level originLevel = event.getOrigin();
        Level targetLevel = event.getTarget();
        if (configReader.isJoinMOTDEnabled() && configReader.getJoinMOTDType().equals("multi-world")) {
            if (!(entity instanceof Player)) return;
            if (originLevel.equals(targetLevel)) return;

            Player player = (Player) entity;

            String message = pluginConfig.getString("Join-MOTD.multi-world." + targetLevel.getName() + ".message");
            String subMessage = pluginConfig.getString("Join-MOTD.multi-world." + targetLevel.getName() + ".sub-message");

            messageSender(player, message, subMessage);
        }
    }

    public void messageSender(Player player, String message, String subMessage) {
        if (placeholderAPI != null) {
            player.sendTitle(TextFormat.colorize('&', placeholderAPI.translateString(message, player)));
            player.setSubtitle(TextFormat.colorize('&', placeholderAPI.translateString(subMessage, player)));
        } else {
            player.sendTitle(TextFormat.colorize('&', message));
            player.setSubtitle(TextFormat.colorize('&', subMessage));
        }
    }
}
