package me.ufo.tools.tools.impl;

import me.ufo.tools.Tools;
import me.ufo.tools.tools.Tool;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.items.ItemBuilder;
import me.ufo.tools.util.items.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ToolItems {

    private Tools INSTANCE = Tools.getInstance();
    private HashMap<ToolType, Tool> tools = new HashMap<>();

    private ItemStack item;

    public void build() {
        ConfigurationSection TOOLS = INSTANCE.getConfig().getConfigurationSection("TOOLS");

        TOOLS.getKeys(false).forEach(key -> {
            String name = INSTANCE.getConfig().getString("TOOLS." + key + ".name");
            Material material = Material.getMaterial(INSTANCE.getConfig().getString("TOOLS." + key + ".material"));
            List<String> enchantments = INSTANCE.getConfig().getStringList("TOOLS." + key + ".enchantments");

            item = new ItemStack(material);

            if (enchantments != null) {
                enchantments.forEach(enchantment -> item.addEnchantment(Enchantment.getByName(enchantment.substring(0, enchantment.indexOf(","))), Integer.valueOf(enchantment.substring(enchantment.indexOf(",") + 1))));
            }

            item = new NBTItem(new ItemBuilder(item)
                    .setName(Style.translate(name))
                    .setLore(Style.translateLines(INSTANCE.getConfig().getStringList("TOOLS." + key + ".lore"))))
                    .setString("tooltype", key)
                    .setInt(key, 0)
                    .setDouble("unique", Math.random()).build();

            tools.put(ToolType.valueOf(key), new Tool(name, item, ToolType.valueOf(key)));
        });

    }

    public HashMap<ToolType, Tool> getAllTools() {
        return tools;
    }

}
