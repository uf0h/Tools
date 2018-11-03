package me.ufo.tools.integration;

import me.ufo.tools.Tools;
import org.bukkit.Bukkit;

public class Factions {

    public void setup() {
        if (!setupFactions()) {
            Tools.getInstance().getLogger().info("FACTIONSUUID DEPENDENCY NOT FOUND.");
            Bukkit.getPluginManager().disablePlugin(Tools.getInstance());
        } else {
            Tools.getInstance().getLogger().info("FACTIONSUUID DEPENDENCY FOUND.");
        }
    }

    private boolean setupFactions() {
        return Bukkit.getServer().getPluginManager().getPlugin("Factions") != null;
    }

}
