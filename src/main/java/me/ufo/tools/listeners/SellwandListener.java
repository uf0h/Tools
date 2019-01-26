package me.ufo.tools.listeners;

import com.massivecraft.factions.entity.MPlayer;
import me.ufo.collectors.collector.CollectionType;
import me.ufo.collectors.collector.Collector;
import me.ufo.tools.integration.Econ;
import me.ufo.tools.integration.Outpost;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SellwandListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()) {
            if (new NBTItem(event.getPlayer().getItemInHand()).hasKey(ToolType.SELLWAND.toString())) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    if (event.getClickedBlock().getType() == Material.BEACON) {
                        if (Collector.isCollector(event.getClickedBlock().getLocation())) {
                            final Collector collector = Collector.get(event.getClickedBlock().getLocation());
                            if (collector != null) {
                                double totalValue = collector.getAmounts().entrySet().stream()
                                        .filter(entry -> entry.getKey() != CollectionType.CREEPER && entry.getValue() > 0)
                                        .mapToDouble(entry -> (entry.getValue() * entry.getKey().getSellPrice())).sum();

                                if (totalValue == 0) {
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "This collector has a total value of " + ChatColor.GREEN.toString() + "$" + totalValue + ChatColor.RED.toString() + ".");
                                    return;
                                }

                                if (Outpost.isFactionControllingOutpost(MPlayer.get(event.getPlayer()).getFaction())) {
                                    totalValue *= 2;
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "You will receive " + ChatColor.GREEN.toString() + "x2" + ChatColor.RED.toString() + " value as you are controlling outpost.");
                                }

                                collector.getAmounts().entrySet().stream()
                                        .filter(entry -> entry.getKey() != CollectionType.CREEPER && entry.getValue() > 0)
                                        .forEach(entry -> entry.setValue(0));

                                if (Econ.depositAmountToPlayer(event.getPlayer(), totalValue)) {
                                    event.getPlayer().sendMessage(ChatColor.GREEN.toString() + "+$" + totalValue + ChatColor.RED.toString() + " from selling everything in this collector.");
                                }
                            }
                        }
                    }

                    // DEFAULT FUNCTIONALITY
                    /*if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {

                        if (!Factions.playerCanPlaceHere(event.getPlayer(), event.getClickedBlock())) return;
                        if (!Worldguard.playerCanPlaceHere(event.getPlayer(), event.getClickedBlock())) return;

                        final Chest chest = (Chest) event.getClickedBlock().getState();
                        final Inventory inventory = chest.getInventory();

                        double cost = 0.0;

                        for (int i = 0; i < inventory.getSize(); i++) {
                            if (inventory.getItem(i) == null) continue;

                            final ItemStack itemStack = inventory.getItem(i);
                            if (isSellable(itemStack.getType())) {
                                cost += getSellableItemCost(itemStack.getType()) * itemStack.getAmount();
                                inventory.setItem(i, new ItemStack(Material.AIR));
                            }
                        }

                        if (cost > 0) {
                            Econ.depositAmountToPlayer(event.getPlayer(), cost);
                            Ref.sendActionBar(event.getPlayer(), "&a&l+$" + new DecimalFormat(".##").format(cost));
                        }

                    }*/
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

    /*private boolean isSellable(Material material) {
        return Tools.getInstance().getExtras().getSellableItems().containsKey(material);
    }

    private Double getSellableItemCost(Material material) {
        return Tools.getInstance().getExtras().getSellableItems().get(material);
    }*/

}
