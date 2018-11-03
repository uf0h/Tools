package me.ufo.tools.commands;

import me.ufo.tools.tools.Tool;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.command.Command;
import me.ufo.tools.util.command.param.Parameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToolCommands {

    @Command(names = "tools")
    public static void tools(CommandSender sender) {
        // help message
    }

    @Command(names = "tools give")
    public static void tools(CommandSender sender, @Parameter(name = "tool") Tool tool, @Parameter(name = "target") Player target) {
        target.getInventory().addItem(tool.getItemStack().clone());

        sender.sendMessage(Style.translate("&e" + target.getName() + " &dhas been given a " + tool.getName() + "&d."));
        target.sendMessage(Style.translate("&dYou have been given a " + tool.getName() + "&d."));
    }

}
