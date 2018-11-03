package me.ufo.tools.util;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Style {

    public static final String BLANK_LINE = "§a §b §c §d §e §f §0 §r";
    public static final String RESET = ChatColor.RESET.toString();
    private static final FontRenderer FONT_RENDERER = new FontRenderer();
    private static final String MAX_LENGTH = "11111111111111111111111111111111111111111111111111111";
    private static final Pattern ONLY_NUMBERS = Pattern.compile("((([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.?\\d?\\d?)?$)");

    private Style() {
        throw new RuntimeException("Cannot instantiate utility class.");
    }

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translateLines(List<String> in) {
        return in.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
    }

    public static String getBorderLine() {
        int chatWidth = FONT_RENDERER.getWidth(MAX_LENGTH) / 10 * 9;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            sb.append("-");

            if (FONT_RENDERER.getWidth(sb.toString()) >= chatWidth) {
                break;
            }
        }

        return ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + sb.toString();
    }

}
