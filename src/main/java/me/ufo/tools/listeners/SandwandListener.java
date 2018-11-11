package me.ufo.tools.listeners;

import me.ufo.tools.Tools;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.TimeUtil;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Consumer;

public class SandwandListener implements Listener {

    private HashMap<UUID, Long> sandwandTimestamps = new HashMap<>();

    private int COOLDOWN;
    private String COOLDOWN_MESSAGE;

    public SandwandListener() throws NullPointerException {
        COOLDOWN = Tools.getInstance().getConfig().getInt("TOOLS.SANDWAND.cooldown");
        COOLDOWN_MESSAGE = Style.translate(Tools.getInstance().getConfig().getString("COOLDOWN_MESSAGE"));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item != null && item.hasItemMeta()) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey(ToolType.SANDWAND.toString())) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    if (event.getClickedBlock() == null) return;

                    final Player player = event.getPlayer();

                    if (sandwandTimestamps.containsKey(player.getUniqueId())) {
                        final long cooldown = COOLDOWN;
                        final long left = sandwandTimestamps.get(player.getUniqueId()) + cooldown - (System.currentTimeMillis());

                        if (left > 1000) {
                            player.sendMessage(COOLDOWN_MESSAGE.replace("%time%", TimeUtil.millisToRoundedTime(left)));
                        } else {
                            sandwandTimestamps.remove(player.getUniqueId());
                        }

                    } else {
                        sandwandTimestamps.put(player.getUniqueId(), System.currentTimeMillis());

                        final Queue<Block> blocks = new LinkedList<>();
                        getSandBlocks(event.getClickedBlock(), blocks::add);

                        Bukkit.getServer().getScheduler().runTask(Tools.getInstance(), () -> blocks.forEach(block -> block.setType(Material.AIR)));
                    }
                }
            }
        }
    }

    private void getSandBlocks(Block block, Consumer<Block> consumer) {
        Block toBeChecked;
        for (int y = 256; y > 0; y--) {
            toBeChecked = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
            if (toBeChecked.getType().hasGravity()) {
                consumer.accept(toBeChecked);
            }
        }
    }

}
