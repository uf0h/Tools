package me.ufo.tools.obj;

import org.bukkit.Material;

public class SellableItem {

    public Material material;
    public double cost;

    public SellableItem(Material material, double cost) {
        this.material = material;
        this.cost = cost;
    }

    public Material getMaterial() {
        return material;
    }

    public double getCost() {
        return cost;
    }

}
