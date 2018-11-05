package me.ufo.tools.listeners;

import me.ufo.tools.Tools;
import me.ufo.tools.integration.Factions;
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
import org.bukkit.inventory.ItemStack;

public class TrenchPickListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item != null && item.hasItemMeta()) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey(ToolType.TRENCHPICK.toString())) {
                event.setCancelled(true);

                process(event.getBlock().getLocation(), event.getPlayer());

            }
        }
    }

    private void process(Location location, Player player) {
        int RADIUS = 2;
        for (int x = location.getBlockX() - RADIUS; x <= location.getBlockX() + RADIUS; x++) {
            for (int z = location.getBlockZ() - RADIUS; z <= location.getBlockZ() + RADIUS; z++) {
                if (Factions.playerCanPlaceHere(player, new Location(location.getWorld(), x, location.getY(), z).getBlock(), "break")) {
                    for (int y = location.getBlockY() - RADIUS; y <= location.getBlockY() + RADIUS; y++) {
                        Location block = new Location(location.getWorld(), x, y, z);

                        if (isOutsideOfBorder(block.getBlock())) continue;
                        if (isNotBreakable(block.getBlock().getType())) continue;

                        Tools.getInstance().getFastBlockUpdate().run(block, Material.AIR);
                    }
                }
            }
        }
    }

    private boolean isNotBreakable(Material material) {
        switch (material) {
            case AIR:
            case BEDROCK:
            case MOB_SPAWNER:
            case CHEST:
            case TRAPPED_CHEST:
            case HOPPER:
            case DIAMOND_ORE:
            case GOLD_ORE:
                return true;
        }
        return false;
    }

    private boolean isOutsideOfBorder(Block block) {
        Location loc = block.getLocation();
        WorldBorder border = block.getWorld().getWorldBorder();
        double size = border.getSize() / 2;
        Location center = border.getCenter();
        double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();
        return ((x >= size || (-x) > size) || (z >= size || (-z) > size));
    }

}
