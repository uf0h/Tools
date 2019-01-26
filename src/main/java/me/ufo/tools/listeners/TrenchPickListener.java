package me.ufo.tools.listeners;

import me.ufo.tools.Tools;
import me.ufo.tools.integration.Factions;
import me.ufo.tools.integration.Worldguard;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class TrenchPickListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()) {
            if (new NBTItem(event.getPlayer().getItemInHand()).hasKey(ToolType.TRENCHPICK.toString())) {
                event.setCancelled(true);

                process(event.getBlock().getLocation(), event.getPlayer());
            }
        }
    }

    private void process(Location location, Player player) {
        int RADIUS = 2;
        for (int x = location.getBlockX() - RADIUS; x <= location.getBlockX() + RADIUS; x++) {
            for (int z = location.getBlockZ() - RADIUS; z <= location.getBlockZ() + RADIUS; z++) {
                final Location toBeChecked = new Location(location.getWorld(), x, location.getY(), z);
                if (Factions.playerCanPlaceHere(player, toBeChecked.getBlock()) &&
                        Worldguard.playerCanPlaceHere(player, toBeChecked.getBlock())) {

                    for (int y = location.getBlockY() - RADIUS; y <= location.getBlockY() + RADIUS; y++) {
                        final Location block = new Location(location.getWorld(), x, y, z);

                        if (isOutsideOfBorder(block.getBlock())) continue;
                        if (isNotBreakable(block.getBlock().getType())) continue;

                        Tools.getInstance().getFastBlockUpdate().run(block, Material.AIR, false);
                    }
                }
            }
        }
    }

    private boolean isNotBreakable(Material material) {
        switch (material) {
            case AIR:
            case BEDROCK:
            case BEACON:
            case MOB_SPAWNER:
            case CHEST:
            case TRAPPED_CHEST:
            case HOPPER:
                return true;
        }
        return false;
    }

    private boolean isOutsideOfBorder(Block block) {
        final Location loc = block.getLocation();
        final WorldBorder border = block.getWorld().getWorldBorder();
        final double size = border.getSize() / 2;
        final Location center = border.getCenter();
        final double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();
        return ((x >= size || (-x) > size) || (z >= size || (-z) > size));
    }

}
