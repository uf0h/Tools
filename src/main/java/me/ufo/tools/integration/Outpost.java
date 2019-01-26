package me.ufo.tools.integration;

import com.massivecraft.factions.entity.Faction;
import me.aceix8.outposts.AceOutposts;
import me.aceix8.outposts.api.OutpostAPI;
import me.ufo.tools.Tools;
import org.bukkit.Bukkit;

public class Outpost {

    private final Tools plugin = Tools.getInstance();

    private static OutpostAPI outpostAPI = null;

    public static boolean isFactionControllingOutpost(Faction faction) {
        return outpostAPI.isFactionControllingAnOutpost(faction);
    }

    public void setup() {
        if (!setupOutposts()) {
            this.plugin.getLogger().info("ACEOUTPOSTS DEPENDENCY NOT FOUND.");
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        } else {
            this.plugin.getLogger().info("ACEOUTPOSTS DEPENDENCY FOUND.");
        }
    }

    private boolean setupOutposts() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Ace-Outposts") == null) {
            return false;
        }

        outpostAPI = AceOutposts.getInstance().getApi();
        return true;
    }
}
