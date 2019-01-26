package me.ufo.tools.listeners;

import me.ufo.collectors.collector.CollectionType;
import me.ufo.collectors.collector.Collector;
import me.ufo.tools.Tools;
import me.ufo.tools.integration.Factions;
import me.ufo.tools.integration.Worldguard;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HarvesterHoeListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()) {
            if (new NBTItem(event.getPlayer().getItemInHand()).hasKey(ToolType.HARVESTERHOE.toString())) {
                event.setCancelled(true);
                if (event.getBlock().getType() == Material.SUGAR_CANE_BLOCK) {
                    final Player player = event.getPlayer();

                    if (!Factions.playerCanPlaceHere(player, event.getBlock()) ||
                            !Worldguard.playerCanPlaceHere(player, event.getBlock())) {
                        event.setCancelled(true);
                        return;
                    }

                    if (Collector.chunkHasCollector(event.getBlock().getLocation())) {
                        final Collector collector = Collector.get(event.getBlock().getLocation());
                        int amountOfCane = 0;

                        Block next = event.getBlock();
                        while (next != null && next.getType() == Material.SUGAR_CANE_BLOCK) {
                            Tools.getInstance().getFastBlockUpdate().run(next.getLocation(), Material.AIR, false);
                            amountOfCane += 2;
                            next = next.getRelative(BlockFace.UP);
                        }
                        collector.increment(CollectionType.SUGAR_CANE, amountOfCane);
                    } else {
                        // DEFAULT FUNCTIONALITY
                        final Location loc = player.getLocation().clone();
                        loc.setPitch(0);

                        final List<Block> line = new ArrayList<>();
                        for (int i = 1; i <= 6; i++) {
                            if (!line.contains(loc.clone().add(loc.getDirection().normalize().multiply(i).toLocation(loc.getWorld())).getBlock())) {
                                line.add(loc.clone().add(loc.getDirection().normalize().multiply(i).toLocation(loc.getWorld())).getBlock());
                            }
                        }

                        //Gets the sugarcane on the side of the main line of sight
                        for (Block b : new ArrayList<>(line)) {
                            Block b1 = b.getRelative(BlockFace.WEST);
                            Block b2 = b.getRelative(BlockFace.EAST);

                            if ((player.getLocation().getYaw() > -135 && player.getLocation().getYaw() < -45) ||
                                    (player.getLocation().getYaw() < 135 && player.getLocation().getYaw() > 45)) {
                                b1 = b.getRelative(BlockFace.NORTH);
                                b2 = b.getRelative(BlockFace.SOUTH);
                            }

                            if (b1.getType() == Material.SUGAR_CANE_BLOCK) {
                                line.add(b1);
                            } else if (b2.getType() == Material.SUGAR_CANE_BLOCK) {
                                line.add(b2);
                            }
                        }

                        final List<Block> toAdd = new ArrayList<>();

                        for (int i = 0; i < 3; i++) {
                            for (Block b : new ArrayList<>(line)) {
                                Block up = b.getLocation().clone().add(0, i + 1, 0).getBlock();

                                if (up.getType() == Material.SUGAR_CANE_BLOCK) {
                                    toAdd.add(up);
                                }
                            }
                        }

                        toAdd.forEach(bl -> {
                            if (!line.contains(bl)) {
                                line.add(bl);
                            }
                        });


                        // Removes non-sugarcane
                        for (Block b : new ArrayList<>(line)) {
                            if (b.getType() != Material.SUGAR_CANE_BLOCK) {
                                line.remove(b);
                            }
                        }

                        final int amount = breakCane(line);

                        toAdd.clear();
                        line.clear();

                        if (amount > 0) {
                            final ItemStack sugarcane = new ItemStack(Material.SUGAR_CANE, amount);

                            if (player.getInventory().firstEmpty() == -1) {
                                player.getWorld().dropItem(player.getLocation(), sugarcane);
                                player.sendMessage(Style.translate("&cYour inventory is full, dropping sugarcane."));
                            } else {
                                player.getInventory().addItem(sugarcane);
                            }
                        }
                    }
                }
            }
        }
    }

    private int breakCane(List<Block> line) {
        final TreeMap<Double, Block> tree = new TreeMap<>(Collections.reverseOrder());
        int amountBroken = 0;
        for (Block block : line) {
            if (block.getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) {
                amountBroken++;

                tree.put(block.getY() + new Random().nextDouble(), block);
            }
        }

        for (double d : tree.keySet()) {
            final Block bl = tree.get(d);
            bl.getLocation().getWorld().playEffect(bl.getLocation().add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER, 0);
            Tools.getInstance().getFastBlockUpdate().run(bl.getLocation(), Material.AIR, false);
        }

        tree.clear();

        return amountBroken;
    }

}
