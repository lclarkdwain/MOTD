package dev.anullihate.motd.tasks;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import dev.anullihate.motd.ConfigReader;
import dev.anullihate.motd.Motd;

import java.util.List;
import java.util.Random;

public class ServerMOTDTask extends PluginTask<Motd> {

    private int seq = 0;

    public ServerMOTDTask(Motd owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        Motd plugin = this.getOwner();
        List<String> allMessages = plugin.configReader.getServerMOTDMessages();

        while(true) {
            if (this.seq < allMessages.size()) {
                plugin.getServer().getNetwork().setName(TextFormat.colorize('&', allMessages.get(this.seq)));
                this.seq++;
                break;
            } else {
                this.seq = 0;
            }
        }


    }
}
