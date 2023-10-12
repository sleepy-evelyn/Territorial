package io.github.sleepy_evelyn.territorial.mixin.common;

import io.github.sleepy_evelyn.territorial.block.EclipseRoseBushBlock;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin extends Entity {

    @Shadow
    private int ambientTick;

    public LightningEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method="tick", at=@At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(ambientTick >= 0) {
            if(!getWorld().isClient) {
                var blockPos = getBlockPos();
                var blockState = getWorld().getBlockState(blockPos);
                var block = blockState.getBlock();

                // Replace a With Rose or Rose Bush block with an Eclipse Rose variant upon lightning strike
                if(block == Blocks.ROSE_BUSH) {
                    var blockHalf = blockState.get(Properties.DOUBLE_BLOCK_HALF);
                    if(blockHalf == DoubleBlockHalf.UPPER) blockPos = blockPos.down();
                    EclipseRoseBushBlock.placeAt(getWorld(), TerritorialBlocks.ECLIPSE_ROSE_BUSH.getDefaultState(), blockPos, (1 << 1) | (1 << 4));
                }
                else if(block == Blocks.WITHER_ROSE) {
					getWorld().setBlockState(blockPos, TerritorialBlocks.ECLIPSE_ROSE.getDefaultState(), (1 << 1) | (1 << 4));
                }
            }
        }
    }
}
