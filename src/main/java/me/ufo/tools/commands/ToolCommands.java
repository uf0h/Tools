package me.ufo.tools.commands;

import me.ufo.tools.Tools;
import me.ufo.tools.tools.Tool;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.Style;
import me.ufo.tools.util.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ToolCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (!sender.hasPermission("tools.admin")) return false;

        if (args.length == 0) {
            sender.sendMessage(new String[]{
                    Style.getBorderLine(),
                    Style.translate("&e/tools list &7- &dList all tools."),
                    Style.translate("&e/tools give <target> <tool> <amount> &7- &dGive tool to player."),
                    Style.getBorderLine()
            });
            return false;
        }

        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(new String[]{
                    Style.getBorderLine(),
                    Style.translate("&e" + Arrays.toString(ToolType.values())),
                    Style.getBorderLine()
            });

            return false;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 3) {
                sender.sendMessage(Style.translate("&cCorrect Usage: /tools give <target> <tool> <amount>"));
                return false;
            }

            Player target = Tools.getInstance().getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "No player with the name " + args[1] + " found.");
                return false;
            }

            Tool tool;
            try {
                tool = Tools.getInstance().getToolItems().getAllTools().get(ToolType.valueOf(args[2].toUpperCase()));
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "No tool with the name " + args[2].toUpperCase() + " found.");
                return false;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "That is not a valid number.");
                return false;
            }

            target.getInventory().addItem(new ItemBuilder(tool.getItemStack().clone()).setAmount(amount).build());
            if (sender instanceof Player) {
                if (sender != target) {
                    sender.sendMessage(Style.translate("&e" + target.getName() + " &dhas been given " + amount + " " + tool.getName() + "&d."));
                }
            }
            target.sendMessage(Style.translate("&dYou have been given " + amount + " " + tool.getName() + "&d."));

            return false;
        }

        return false;
    }

}
