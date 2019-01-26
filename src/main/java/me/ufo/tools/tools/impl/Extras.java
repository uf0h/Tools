package me.ufo.tools.tools.impl;

public class Extras {

    // DEFAULT FUNCTIONALITY
    /*public HashMap<Material, Double> sellableItems = new HashMap<>();
    private Tools INSTANCE = Tools.getInstance();

    public void build() {
        List<String> sellableitemsList = INSTANCE.getConfig().getStringList("SELLABLEITEMS");

        sellableitemsList.forEach(sellableItem -> {
            Material material = Material.getMaterial(sellableItem.substring(0, sellableItem.indexOf(",")));
            double cost = Double.valueOf(sellableItem.substring(sellableItem.indexOf(",") + 1)) * INSTANCE.getConfig().getDouble("TOOLS.SELLWAND.multiplier");

            sellableItems.put(material, cost);
        });

    }

    public HashMap<Material, Double> getSellableItems() {
        return sellableItems;
    }*/

}
