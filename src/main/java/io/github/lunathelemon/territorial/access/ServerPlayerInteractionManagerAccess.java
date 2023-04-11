package io.github.lunathelemon.territorial.access;

import net.minecraft.util.math.BlockPos;

public interface ServerPlayerInteractionManagerAccess {
    void territorial$setTargetedBlockPos(BlockPos targetedBlockPos);
    BlockPos territorial$getTargetedBlockPos();
}
