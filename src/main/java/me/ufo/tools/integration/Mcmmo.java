package me.ufo.tools.integration;

import com.gmail.nossr50.api.ExperienceAPI;
import me.ufo.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Mcmmo {

    public static void addXPToPlayer(Player player, String skill, int amount, String reason) {
        ExperienceAPI.addRawXP(player, skill, (float) amount, reason);
    }

    public void setup() {
        if (!setupMcmmo()) {
            Tools.getInstance().getLogger().info("MCMMO DEPENDENCY NOT FOUND.");
            Bukkit.getPluginManager().disablePlugin(Tools.getInstance());
        } else {
            Tools.getInstance().getLogger().info("MCMMO DEPENDENCY FOUND.");
        }
    }

    private boolean setupMcmmo() {
        return Bukkit.getServer().getPluginManager().getPlugin("McMMO") != null;
    }

}
