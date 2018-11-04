package me.ufo.tools.obj.impl;

import me.ufo.tools.Tools;
import me.ufo.tools.obj.SellableItem;
import org.bukkit.Material;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Objects {

    private Tools INSTANCE = Tools.getInstance();
    public Set<SellableItem> sellableItems = new HashSet<>();

    public void build() {
        List<String> sellableitemsList = INSTANCE.getConfig().getStringList("SELLABLEITEMS");
        DecimalFormat decimalFormat = new DecimalFormat(".##");

        sellableitemsList.forEach(sellableItem -> {
            Material material = Material.getMaterial(sellableItem.substring(0, sellableItem.indexOf(",")));
            double cost = Double.valueOf(sellableItem.substring(sellableItem.indexOf(",") + 1)) * INSTANCE.getConfig().getDouble("TOOLS.SELLWAND.multiplier");

            sellableItems.add(new SellableItem(material, cost));
        });

    }

    public Set<SellableItem> getSellableItems() {
        return sellableItems;
    }

}
