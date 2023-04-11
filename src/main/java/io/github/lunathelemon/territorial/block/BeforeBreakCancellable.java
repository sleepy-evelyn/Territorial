package io.github.lunathelemon.territorial.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface BeforeBreakCancellable {
    // Triggered before a block is broken
    boolean beforeBreak(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player);
}
