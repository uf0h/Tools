package me.ufo.tools.listeners;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import me.ufo.collectors.collector.CollectionType;
import me.ufo.collectors.collector.Collector;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class TntwandListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()) {
            if (new NBTItem(event.getPlayer().getItemInHand()).hasKey(ToolType.TNTWAND.toString())) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    if (event.getClickedBlock().getType() == Material.BEACON) {
                        if (Collector.isCollector(event.getClickedBlock().getLocation())) {
                            final Collector collector = Collector.get(event.getClickedBlock().getLocation());
                            if (collector != null) {
                                final Faction faction = MPlayer.get(event.getPlayer()).getFaction();

                                if (faction == null) return;

                                if (faction.isNone()) {
                                    event.getPlayer().sendMessage(ChatColor.RED.toString() + "You must be in a faction with a tntbank withdraw tnt from this collector.");
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
