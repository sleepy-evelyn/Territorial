package io.github.sleepy_evelyn.territorial.access;

import net.minecraft.util.math.BlockPos;

public interface StatusEffectInstanceAccess {
    BlockPos territorial$getLastPosApplied();
    void territorial$setLastPosApplied(BlockPos pos);
}
