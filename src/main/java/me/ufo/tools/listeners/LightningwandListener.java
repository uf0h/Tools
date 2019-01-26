package me.ufo.tools.listeners;

import me.ufo.tools.Tools;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.TimeUtil;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

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
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().hasItemMeta()) {
            if (new NBTItem(event.getPlayer().getItemInHand()).hasKey(ToolType.LIGHTNINGWAND.toString())) {
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
