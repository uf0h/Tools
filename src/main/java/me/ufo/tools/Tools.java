package me.ufo.tools;

import me.ufo.tools.integration.Factions;
import me.ufo.tools.tools.impl.ToolItems;
import me.ufo.tools.util.command.CommandHandler;
import me.ufo.tools.util.listener.ListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Tools extends JavaPlugin {

    private static Tools instance;

    private ToolItems toolItems;

    public static Tools getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        instance = this;

        Factions factions = new Factions();
        factions.setup();

        this.saveDefaultConfig();

        CommandHandler.init();
        CommandHandler.loadCommandsFromPackage(this, "me.ufo.tools.commands");

        ListenerHandler.loadListenersFromPackage(this, "me.ufo.tools.listeners");

        toolItems = new ToolItems();
        toolItems.build();

        this.getLogger().info("Successfully loaded. Took (" + (System.currentTimeMillis() - startTime) + "ms).");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public ToolItems getToolItems() {
        return toolItems;
    }

}
