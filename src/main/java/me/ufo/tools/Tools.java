package me.ufo.tools;

import com.sun.org.apache.bcel.internal.generic.RET;
import me.ufo.tools.integration.Econ;
import me.ufo.tools.integration.Factions;
import me.ufo.tools.obj.impl.Objects;
import me.ufo.tools.tools.impl.ToolItems;
import me.ufo.tools.util.command.CommandHandler;
import me.ufo.tools.util.listener.ListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Tools extends JavaPlugin {

    private static Tools instance;

    private ToolItems toolItems;

    private Objects objects;

    public static Tools getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        instance = this;

        Econ econ = new Econ();
        econ.setup();

        Factions factions = new Factions();
        factions.setup();

        this.saveDefaultConfig();

        CommandHandler.init();
        CommandHandler.loadCommandsFromPackage(this, "me.ufo.tools.commands");

        ListenerHandler.loadListenersFromPackage(this, "me.ufo.tools.listeners");

        toolItems = new ToolItems();
        toolItems.build();

        objects = new Objects();
        objects.build();

        this.getLogger().info("Successfully loaded. Took (" + (System.currentTimeMillis() - startTime) + "ms).");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public ToolItems getToolItems() {
        return toolItems;
    }

    public Objects getObjects() {
        return objects;
    }

}
