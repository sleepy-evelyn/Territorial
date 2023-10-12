package io.github.sleepy_evelyn.territorial.util;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

public class NetworkingUtils {

    public static void markDirtyAndSync(BlockEntity be, World world) {
        be.markDirty();
        world.updateListeners(be.getPos(), be.getCachedState(), be.getCachedState(), Block.NOTIFY_LISTENERS);
    }
}
