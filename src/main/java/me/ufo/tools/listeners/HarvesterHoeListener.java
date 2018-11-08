package me.ufo.tools.listeners;

import me.ufo.tools.integration.Factions;
import me.ufo.tools.integration.Worldguard;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HarvesterHoeListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        ItemStack item = event.getPlayer().getItemInHand();
        if (item != null && item.hasItemMeta()) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey(ToolType.HARVESTERHOE.toString())) {
                event.setCancelled(true);
                if (event.getBlock().getType() == Material.SUGAR_CANE_BLOCK) {
                    Block block = event.getBlock();
                    Player player = event.getPlayer();
                    Location loc = player.getLocation().clone();
                    loc.setPitch(0);

                    if (!Factions.playerCanPlaceHere(player, block, "break")) return;
                    if (!Worldguard.playerCanPlaceHere(player, block)) return;

                    List<Block> line = new ArrayList<>();
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

                    List<Block> toAdd = new ArrayList<>();

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

                    int amount = breakCane(line);

                    if (amount > 0) {
                        ItemStack sugarcane = new ItemStack(Material.SUGAR_CANE, amount);
                        player.getInventory().addItem(sugarcane);
                    }
                }
            }
        }
    }

    private int breakCane(List<Block> line) {
        TreeMap<Double, Block> tree = new TreeMap<>(Collections.reverseOrder());
        int amountBroken = 0;
        for(Block block : line) {
            if(block.getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK) {
                amountBroken++;

                tree.put(block.getY() + new Random().nextDouble(), block);

            }
        }

        for(double d : tree.keySet()) {
            Block bl = tree.get(d);
            bl.getLocation().getWorld().playEffect(bl.getLocation().add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER, 0);
            bl.setType(Material.AIR);
        }

        return amountBroken;
    }

}
