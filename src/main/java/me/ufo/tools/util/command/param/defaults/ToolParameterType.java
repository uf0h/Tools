package me.ufo.tools.util.command.param.defaults;

import me.ufo.tools.Tools;
import me.ufo.tools.tools.Tool;
import me.ufo.tools.tools.ToolType;
import me.ufo.tools.util.command.param.ParameterType;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ToolParameterType implements ParameterType<Tool> {

    public Tool transform(CommandSender sender, String source) {
        try {
            return Tools.getInstance().getToolItems().getAllTools().get(ToolType.valueOf(source.toUpperCase()));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "No tool with the name " + source + " found.");
            return (null);
        }
    }

    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        final List<String> completions = new ArrayList<>();

        for (ToolType toolType : ToolType.values()) {
            if (StringUtils.startsWithIgnoreCase(toolType.name(), source)) {
                completions.add(toolType.name());
            }
        }

        return completions;
    }


}
