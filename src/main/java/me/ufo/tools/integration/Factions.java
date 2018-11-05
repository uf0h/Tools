package me.ufo.tools.integration;

import com.massivecraft.factions.listeners.FactionsBlockListener;
import me.ufo.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Factions {

    public static boolean playerCanPlaceHere(Player player, Block block, String action) {
        return FactionsBlockListener.playerCanBuildDestroyBlock(player, block.getLocation(), action, true);
    }

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
