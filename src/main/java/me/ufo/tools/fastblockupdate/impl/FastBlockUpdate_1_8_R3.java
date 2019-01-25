package me.ufo.tools.fastblockupdate.impl;

import me.ufo.tools.Tools;
import me.ufo.tools.fastblockupdate.FastBlockUpdate;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class FastBlockUpdate_1_8_R3 implements FastBlockUpdate {

    @Override
    public void run(Location location, Material material, boolean triggerUpdate) {
        Tools.getInstance().getServer().getScheduler().runTaskAsynchronously(Tools.getInstance(), () -> {
            World w = ((CraftWorld) location.getWorld()).getHandle();
            BlockPosition bp = new BlockPosition(location.getX(), location.getY(), location.getZ());
            IBlockData ibd = net.minecraft.server.v1_8_R3.Block.getByCombinedId(material.getId());
            Bukkit.getScheduler().runTask(Tools.getInstance(), () -> {
                if (triggerUpdate) {
                    w.setTypeUpdate(bp, ibd);
                } else {
                    w.setTypeAndData(bp, ibd, 2);
                }
            });
        });
    }

}