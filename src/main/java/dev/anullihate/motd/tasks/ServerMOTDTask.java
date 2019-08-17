package dev.anullihate.motd.tasks;

import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import dev.anullihate.motd.ConfigReader;
import dev.anullihate.motd.Motd;

import java.util.List;
import java.util.Random;

public class ServerMOTDTask extends PluginTask<Motd> {

    private int lastRandomNumber;

    public ServerMOTDTask(Motd owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        Motd plugin = this.getOwner();
        List<String> allMessages = plugin.configReader.getServerMOTDMessages();

        int max = allMessages.size();
        Random rand = new Random();
        int randomNumber = rand.nextInt(max);
        while(randomNumber == lastRandomNumber) {
            randomNumber = rand.nextInt(max);
        }
        lastRandomNumber = randomNumber;
        plugin.getServer().getNetwork().setName(TextFormat.colorize('&', allMessages.get(randomNumber)));
    }
}
