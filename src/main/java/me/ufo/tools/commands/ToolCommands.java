package me.ufo.tools.commands;

import me.ufo.tools.tools.Tool;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.command.Command;
import me.ufo.tools.util.command.param.Parameter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ToolCommands {

    @Command(names = "tools", permissionNode = "tools.admin")
    public static void tools(CommandSender sender) {
        sender.sendMessage(new String[] {
                Style.getBorderLine(),
                Style.translate("&e/tools list &7- &dList all tools."),
                Style.translate("&e/tools give <tool> <target> &7- &dGive tool to player."),
                Style.getBorderLine()
        });
    }

    @Command(names = "tools list", permissionNode = "tools.admin")
    public static void tools_list(CommandSender sender) {
        sender.sendMessage(new String[] {
                Style.getBorderLine(),
                Style.translate("&e" + Arrays.toString(ToolType.values())),
                Style.getBorderLine()
        });
    }

    @Command(names = "tools give", permissionNode = "tools.admin")
    public static void tools_give(CommandSender sender, @Parameter(name = "tool") Tool tool, @Parameter(name = "target") Player target) {
        target.getInventory().addItem(tool.getItemStack().clone());

        if (sender instanceof Player)
            sender.sendMessage(Style.translate("&e" + target.getName() + " &dhas been given a " + tool.getName() + "&d."));
        target.sendMessage(Style.translate("&dYou have been given a " + tool.getName() + "&d."));
    }

}
