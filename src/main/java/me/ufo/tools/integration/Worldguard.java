package me.ufo.tools.integration;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.ufo.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Worldguard {

    public static WorldGuardPlugin worldguard = null;

    public static boolean playerCanPlaceHere(Player player, Block block) {
        return worldguard.canBuild(player, block);
    }

    public void setup() {
        if (!setupWorldguard()) {
            Tools.getInstance().getLogger().info("WORLDGUARD DEPENDENCY NOT FOUND.");
            Bukkit.getPluginManager().disablePlugin(Tools.getInstance());
        } else {
            worldguard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
            Tools.getInstance().getLogger().info("WORLDGUARD DEPENDENCY FOUND.");
        }
    }

    private boolean setupWorldguard() {
        return Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null;
    }

}
