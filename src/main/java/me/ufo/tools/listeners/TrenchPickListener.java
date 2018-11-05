package me.ufo.tools.listeners;

import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
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

            }
        }
    }

}
