package me.ufo.tools.util.items;

import org.bukkit.inventory.ItemStack;

public class NBTItem extends NBTObject<NBTItem> {

    private ItemStack item;
    private Object nmsItem;

    public NBTItem(ItemBuilder builder) {
        this(builder.build());
    }

    public NBTItem(ItemStack item) {
        this.item = item;
        this.nmsItem = Ref.NBT.toNMSItem(item);
        this.NBT = Ref.NBT.getNBTTag(nmsItem);
    }

    public ItemStack getItem() {
        Ref.NBT.applyNBT(nmsItem, NBT);
        item = Ref.NBT.toBukkitItem(nmsItem);
        return item;
    }

    public ItemStack build() {
        return getItem();
    }

}
