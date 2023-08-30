package io.github.lunathelemon.territorial.util;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CommandUtils {
    public static ServerPlayerEntity getPlayerOrNull(CommandContext<ServerCommandSource> ctx) {
        return (ctx.getSource().getEntity() instanceof ServerPlayerEntity player) ? player : null;
    }
}
