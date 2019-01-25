package me.ufo.tools.integration;

import com.massivecraft.factions.engine.EnginePermBuild;
import com.massivecraft.massivecore.ps.PS;
import me.ufo.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Factions {

    public static boolean playerCanPlaceHere(Player player, Block block) {
        return EnginePermBuild.canPlayerBuildAt(player, PS.valueOf(block), true);
    }

    public void setup() {
        if (!setupFactions()) {
            Tools.getInstance().getLogger().info("FACTIONS DEPENDENCY NOT FOUND.");
            Bukkit.getPluginManager().disablePlugin(Tools.getInstance());
        } else {
            Tools.getInstance().getLogger().info("FACTIONS DEPENDENCY FOUND.");
        }
    }

    private boolean setupFactions() {
        return Bukkit.getServer().getPluginManager().getPlugin("Factions") != null;
    }

}
