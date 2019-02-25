package me.ufo.tools.listeners;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import me.ufo.collectors.collector.CollectionType;
import me.ufo.collectors.collector.Collector;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class TntwandListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()) {
            if (new NBTItem(event.getPlayer().getItemInHand()).hasKey(ToolType.TNTWAND.toString())) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
                        final MPlayer mPlayer = MPlayer.get(event.getPlayer());
                        final Faction faction = MPlayer.get(event.getPlayer()).getFaction();

                        if (faction == null) return;

                        if (faction.isNone()) {
                            event.getPlayer().sendMessage(ChatColor.RED.toString() + "You must be in a faction with a tntbank to withdraw tnt from chests.");
                            return;
                        }

                        if (faction.getAmountInTntBank() == MPlayer.get(event.getPlayer()).getFaction().getTntBankLimit()) {
                            event.getPlayer().sendMessage(ChatColor.RED.toString() + "You have reached tnt bank capacity.");
                            return;
                        }

                        final Chest chest = (Chest) event.getClickedBlock().getState();
                        if (!chest.getBlockInventory().contains(Material.TNT)) {
                            event.getPlayer().sendMessage(ChatColor.RED.toString() + "There is no tnt in this chest.");
                            return;
                        }

                        final Inventory inventory = chest.getInventory();
                        final ItemStack[] itemStacks = inventory.getContents();
                        if (itemStacks == null) return;

                        int amountOfTnt = 0;

                        for (int i = 0; i < itemStacks.length; i++) {
                            if (itemStacks[i] == null) continue;

                            if (itemStacks[i].getType() == Material.TNT) {
                                int amountInStack = itemStacks[i].getAmount();
                                if (faction.getAmountInTntBank() + (amountOfTnt + amountInStack) > faction.getTntBankLimit()) {
                                    final int amountThatCanBeFilled = (amountOfTnt + amountInStack) - ((faction.getAmountInTntBank() + (amountOfTnt + amountInStack)) - faction.getTntBankLimit());
                                    final int difference = amountInStack - amountThatCanBeFilled;
                                    amountOfTnt += amountThatCanBeFilled;
                                    inventory.clear(i);
                                    inventory.addItem(new ItemStack(Material.TNT, difference));
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "There is only enough capacity in your faction tntbank to add " + ChatColor.YELLOW.toString() + amountOfTnt + ChatColor.RED.toString() + " tnt.");
                                    break;
                                } else {
                                    amountOfTnt += amountInStack;
                                    inventory.clear(i);
                                }
                            }
                        }

                        faction.addAmountToTntBank(amountOfTnt);
                        event.getPlayer().sendMessage(ChatColor.GREEN.toString() + "You " + ChatColor.YELLOW.toString() + "have added " + ChatColor.AQUA.toString() + amountOfTnt + ChatColor.YELLOW.toString() + " tnt to your bank.");
                        return;
                    }

                    if (event.getClickedBlock().getType() == Material.BEACON) {
                        if (Collector.isCollector(event.getClickedBlock().getLocation())) {
                            final Collector collector = Collector.get(event.getClickedBlock().getLocation());
                            if (collector != null) {
                                final Faction faction = MPlayer.get(event.getPlayer()).getFaction();

                                if (faction == null) return;

                                if (faction.isNone()) {
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "You must be in a faction with a tntbank to withdraw tnt from this collector.");
                                    return;
                                }

                                if (faction.getAmountInTntBank() == MPlayer.get(event.getPlayer()).getFaction().getTntBankLimit()) {
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "You have reached tnt bank capacity.");
                                    return;
                                }

                                int amountOfTnt = collector.getAmounts().entrySet().stream()
                                        .filter(entry -> entry.getKey() == CollectionType.CREEPER)
                                        .mapToInt(Map.Entry::getValue).sum();

                                if (amountOfTnt == 0) {
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "This collector has no tnt to withdraw into your faction bank.");
                                    return;
                                }

                                int amountThatCanBeFilled = amountOfTnt - ((faction.getAmountInTntBank() + amountOfTnt) - faction.getTntBankLimit());
                                if (faction.getAmountInTntBank() + amountOfTnt > faction.getTntBankLimit()) {
                                    amountOfTnt = amountThatCanBeFilled;
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "There is only enough capacity in your faction tntbank to add " + ChatColor.YELLOW.toString() + amountOfTnt + ChatColor.RED.toString() + " tnt.");
                                }

                                event.getPlayer().sendMessage(ChatColor.GREEN.toString() + "You " + ChatColor.YELLOW.toString() + "have added " + ChatColor.AQUA.toString() + amountOfTnt + ChatColor.YELLOW.toString() + " tnt to your bank.");
                                faction.addAmountToTntBank(amountOfTnt);

                                collector.decrement(CollectionType.CREEPER, amountOfTnt);
                            }
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

}
