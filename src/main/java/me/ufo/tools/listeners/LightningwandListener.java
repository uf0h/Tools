package me.ufo.tools.listeners;

import me.ufo.tools.Tools;
import me.ufo.tools.integration.Factions;
import me.ufo.tools.integration.Worldguard;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.TimeUtil;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LightningwandListener implements Listener {

    private HashMap<UUID, Long> lightningTimestamps = new HashMap<>();

    private int COOLDOWN;
    private String COOLDOWN_MESSAGE;

    public LightningwandListener() throws NullPointerException {
        COOLDOWN = Tools.getInstance().getConfig().getInt("TOOLS.LIGHTNINGWAND.cooldown");
        COOLDOWN_MESSAGE = Style.translate(Tools.getInstance().getConfig().getString("COOLDOWN_MESSAGE"));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item != null && item.hasItemMeta()) {
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasKey(ToolType.LIGHTNINGWAND.toString())) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR ||
                        event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);

                    final Player player = event.getPlayer();

                    if (lightningTimestamps.containsKey(player.getUniqueId())) {
                        final long cooldown = COOLDOWN;
                        final long left = lightningTimestamps.get(player.getUniqueId()) + cooldown - (System.currentTimeMillis());

                        if (left > 1000) {
                            player.sendMessage(COOLDOWN_MESSAGE.replace("%time%", TimeUtil.millisToRoundedTime(left)));
                        } else {
                            lightningTimestamps.remove(player.getUniqueId());
                        }

                    } else {
                        lightningTimestamps.put(player.getUniqueId(), System.currentTimeMillis());

                        player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 10).getLocation());
                    }
                }
            }
        }
    }

}
