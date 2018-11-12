package me.ufo.tools.listeners;

import me.ufo.tools.integration.Factions;
import me.ufo.tools.integration.Worldguard;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CraftwandListener implements Listener {

    private final Set<Material> craftables = Stream.of(
            Material.IRON_INGOT,
            Material.GOLD_INGOT,
            Material.REDSTONE,
            Material.SULPHUR,
            Material.DIAMOND,
            Material.EMERALD
    ).collect(Collectors.toCollection(HashSet::new));

    // Cache of craftables in players inventory.
    private HashMap<Material, Integer> inventoryCache;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item != null && item.hasItemMeta()) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey(ToolType.CRAFTWAND.toString())) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    event.setCancelled(true);
                    if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {

                        if (!Factions.playerCanPlaceHere(event.getPlayer(), event.getClickedBlock(), "use")) return;
                        if (!Worldguard.playerCanPlaceHere(event.getPlayer(), event.getClickedBlock())) return;

                        Chest chest = (Chest) event.getClickedBlock().getState();
                        Inventory inventory = chest.getInventory();

                        inventoryCache = new HashMap<>();

                        for (int i = 0; i < inventory.getSize(); i++) {
                            if (inventory.getItem(i) == null) continue;

                            ItemStack itemStack = inventory.getItem(i);
                            if (canBeCrafted(itemStack.getType())) {
                                if (containsMaterialInCache(itemStack.getType())) {
                                    int currentAmountOfItems = inventoryCache.get(itemStack.getType());
                                    inventoryCache.put(itemStack.getType(), currentAmountOfItems + itemStack.getAmount());
                                } else {
                                    inventoryCache.put(itemStack.getType(), itemStack.getAmount());
                                }
                            }

                            if (canBeCrafted(itemStack.getType()) && containsMaterialInCache(itemStack.getType())) {
                                inventory.setItem(i, new ItemStack(Material.AIR));
                            }
                        }

                        if (!inventoryCache.isEmpty()) {
                            inventoryCache.forEach((itemStack, amount) -> {
                                Material block;
                                int blocks = amount / 9;
                                int remainder = amount % 9;

                                switch (itemStack) {
                                    case IRON_INGOT:
                                        block = Material.IRON_BLOCK;
                                        break;
                                    case GOLD_INGOT:
                                        block = Material.GOLD_BLOCK;
                                        break;
                                    case REDSTONE:
                                        block = Material.REDSTONE_BLOCK;
                                        break;
                                    case SULPHUR:
                                        block = Material.TNT;
                                        break;
                                    case DIAMOND:
                                        block = Material.DIAMOND_BLOCK;
                                        break;
                                    case EMERALD:
                                        block = Material.EMERALD_BLOCK;
                                        break;
                                    default:
                                        return;
                                }

                                if (blocks != 0)
                                    inventory.addItem(new ItemStack(block, blocks));

                                if (remainder != 0)
                                    inventory.addItem(new ItemStack(itemStack, remainder));
                            });
                        }

                        inventoryCache = null;
                    }
                }
            }
        }
    }

    private boolean canBeCrafted(Material material) {
        return craftables.contains(material);
    }

    private boolean containsMaterialInCache(Material material) {
        return inventoryCache.containsKey(material);
    }

}
