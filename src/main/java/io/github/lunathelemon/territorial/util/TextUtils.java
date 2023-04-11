package io.github.lunathelemon.territorial.util;

import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.List;

public class TextUtils {

    private static final String[] DYE_COLOUR_TO_TEXT_FORMAT = { "§f", "§6", "§5", "§b", "§e", "§a", "§d", "§8", "§7", "§3", "§5", "§9", "§7", "§2", "§4", "§8" };

    public static Text spacer() {
        return Text.of(" ");
    }

    public static String getTextColourFormatting(DyeColor dyeColour) {
        return DYE_COLOUR_TO_TEXT_FORMAT[dyeColour.getId()];
    }

    public static class ToolTip {

        public static void addMultilineText(List<Text> tooltip, String translatableTextId, int numCycles) {
            addMultilineText(tooltip, translatableTextId, numCycles, 0);
        }
        public static void addMultilineText(List<Text> tooltip, String translatableTextId, int numCycles, int startIndex) {
            for (int i = startIndex; i < (startIndex + numCycles); i++) {
                tooltip.add(Text.translatable(translatableTextId + "_" + i));
            }
        }
    }
}
