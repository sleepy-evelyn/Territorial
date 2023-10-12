package io.github.sleepy_evelyn.territorial.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class CorruptionEvents {

    public static final Event<CorruptedBlock> BLOCK_CORRUPTED = EventFactory.createArrayBacked(
            CorruptedBlock.class, (callbacks) -> (world, pos, state, blockEntity) -> {
                for(CorruptedBlock callback : callbacks)
                    callback.onCorruptedBlock(world, pos, state, blockEntity);
            });


    @FunctionalInterface
    public interface CorruptedBlock {
        void onCorruptedBlock(World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
    }

    private CorruptionEvents() {}
}
