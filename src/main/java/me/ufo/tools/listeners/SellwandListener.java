package me.ufo.tools.listeners;

import me.ufo.tools.Tools;
import me.ufo.tools.integration.Econ;
import me.ufo.tools.integration.Factions;
import me.ufo.tools.integration.Worldguard;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
import me.ufo.tools.util.items.Ref;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class SellwandListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item != null && item.hasItemMeta()) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey(ToolType.SELLWAND.toString())) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    event.setCancelled(true);
                    if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {

                        if (!Factions.playerCanPlaceHere(event.getPlayer(), event.getClickedBlock(), "use")) return;
                        if (!Worldguard.playerCanPlaceHere(event.getPlayer(), event.getClickedBlock())) return;

                        Chest chest = (Chest) event.getClickedBlock().getState();
                        Inventory inventory = chest.getInventory();

                        double cost = 0.0;

                        for (int i = 0; i < inventory.getSize(); i++) {
                            if (inventory.getItem(i) == null) continue;

                            ItemStack itemStack = inventory.getItem(i);
                            if (isSellable(itemStack.getType())) {
                                cost += getSellableItemCost(itemStack.getType()) * itemStack.getAmount();
                                inventory.setItem(i, new ItemStack(Material.AIR));
                            }
                        }

                        if (cost > 0) {
                            Econ.depositAmountToPlayer(event.getPlayer(), cost);
                            Ref.sendActionBar(event.getPlayer(), "&a&l+$" + new DecimalFormat(".##").format(cost));
                        }

                    }
                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    switch (event.getClickedBlock().getType()) {
                        case GRASS:
                        case DIRT:
                            event.setCancelled(true);
                            break;
                    }
                }
            }
        }
    }

    private boolean isSellable(Material material) {
        return Tools.getInstance().getExtras().getSellableItems().containsKey(material);
    }

    private Double getSellableItemCost(Material material) {
        return Tools.getInstance().getExtras().getSellableItems().get(material);
    }

}
