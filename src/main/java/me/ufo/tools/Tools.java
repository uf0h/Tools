package me.ufo.tools;

import me.ufo.tools.fastblockupdate.FastBlockUpdate;
import me.ufo.tools.fastblockupdate.impl.FastBlockUpdate_1_8_R3;
import me.ufo.tools.integration.Econ;
import me.ufo.tools.integration.Factions;
import me.ufo.tools.integration.Mcmmo;
import me.ufo.tools.integration.Worldguard;
import me.ufo.tools.tools.impl.Extras;
import me.ufo.tools.tools.impl.ToolItems;
import me.ufo.tools.util.command.CommandHandler;
import me.ufo.tools.util.listener.ListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Tools extends JavaPlugin {

    private static Tools instance;

    private ToolItems toolItems;
    private Extras extras;

    private FastBlockUpdate fastBlockUpdate;

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

        Worldguard worldguard = new Worldguard();
        worldguard.setup();

        Mcmmo mcmmo = new Mcmmo();
        mcmmo.setup();

        this.saveDefaultConfig();

        CommandHandler.init();
        CommandHandler.loadCommandsFromPackage(this, "me.ufo.tools.commands");

        ListenerHandler.loadListenersFromPackage(this, "me.ufo.tools.listeners");

        toolItems = new ToolItems();
        toolItems.build();

        extras = new Extras();
        extras.build();

        fastBlockUpdate = new FastBlockUpdate_1_8_R3();

        this.getLogger().info("Successfully loaded. Took (" + (System.currentTimeMillis() - startTime) + "ms).");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public ToolItems getToolItems() {
        return toolItems;
    }

    public Extras getExtras() {
        return extras;
    }

    public FastBlockUpdate getFastBlockUpdate() {
        return fastBlockUpdate;
    }

}
