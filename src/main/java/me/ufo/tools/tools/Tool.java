package me.ufo.tools.tools;

import org.bukkit.inventory.ItemStack;

public class Tool {

    private String name;
    private ItemStack itemStack;
    private ToolType toolType;

    public Tool(String name, ItemStack itemStack, ToolType toolType) {
        this.name = name;
        this.itemStack = itemStack;
        this.toolType = toolType;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ToolType getToolType() {
        return toolType;
    }

}
