package io.github.lunathelemon.territorial.mixin.common;

import io.github.lunathelemon.territorial.api.event.CorruptionEvents;
import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import io.github.lunathelemon.territorial.util.BeaconUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {

    @Inject(
            method = "tick", at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void tick(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity, CallbackInfo ci,
                             int x, int y, int z, BlockPos posLocal, BeaconBlockEntity.BeamSegment segment, int yTopPos, final int levelLocal) {
        if (world.getTime() % 80L == 0L && levelLocal == 0) {
            int finalLevel = BeaconUtils.updateLevel(world, pos, List.of(TerritorialBlocks.OMNISCIENT_OBSIDIAN));
            if(finalLevel > 0) {
                CorruptionEvents.BLOCK_CORRUPTED.invoker().onCorruptedBlock(world, pos, state, blockEntity);
            }
        }
    }
}


