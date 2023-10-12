package io.github.sleepy_evelyn.territorial.util;

import com.mojang.brigadier.context.CommandContext;
import io.github.sleepy_evelyn.territorial.Territorial;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

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

    public static class CommandResponse {

        @Nullable
        private final ServerPlayerEntity player;
        private final MinecraftServer server;

        public CommandResponse(MinecraftServer server, @Nullable ServerPlayerEntity player) {
            this.player = player;
            this.server = server;
        }

        public CommandResponse(CommandContext<ServerCommandSource> ctx) {
            this.player = CommandUtils.getPlayerOrNull(ctx);
            this.server = ctx.getSource().getServer();
        }

        public void send(MutableText playerText, String consoleText) {
            this.send(playerText, consoleText, false, false);
        }

        public void send(MutableText playerText, String consoleText, boolean isWarning, boolean isAsync) {
            if(player == null)
                if(isWarning)
                    Territorial.LOGGER.warn(consoleText);
                else
                    Territorial.LOGGER.info(consoleText);
            else {
                if(isWarning)
                    playerText = playerText.formatted(Formatting.RED);

                final var finalPlayerText = playerText;
                if (isAsync)
                    server.execute(() -> player.sendMessage(finalPlayerText, false));
                else
                    player.sendMessage(finalPlayerText, false);
            }
        }
    }
}
