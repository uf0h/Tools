package me.ufo.tools.tools.impl;

import me.ufo.tools.Tools;
import me.ufo.tools.tools.Tool;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.items.ItemBuilder;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ToolItems {

    private Tools INSTANCE = Tools.getInstance();
    private HashMap<ToolType, Tool> tools = new HashMap<>();

    public void build() {
        ConfigurationSection TOOLS = INSTANCE.getConfig().getConfigurationSection("TOOLS");

        TOOLS.getKeys(false).forEach(key -> {
            String name = INSTANCE.getConfig().getString("TOOLS." + key + ".name");
            Material material = Material.getMaterial(INSTANCE.getConfig().getString("TOOLS." + key + ".material"));

            ItemStack item =
                    new NBTItem(
                            new ItemBuilder(new ItemStack(material))
                                    .setName(Style.translate(name))
                                    .setLore(Style.translateLines(INSTANCE.getConfig().getStringList("TOOLS." + key + ".lore"))))
                            .setInt(key.toUpperCase(), 0).setDouble("unique", Math.random()).build();

            tools.put(ToolType.valueOf(key.toUpperCase()), new Tool(name, item, ToolType.valueOf(key.toUpperCase())));
        });

    }

    public HashMap<ToolType, Tool> getAllTools() {
        return tools;
    }

}
