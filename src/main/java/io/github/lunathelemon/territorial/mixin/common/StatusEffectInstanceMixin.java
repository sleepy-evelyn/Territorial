package io.github.lunathelemon.territorial.mixin.common;

import io.github.lunathelemon.territorial.access.StatusEffectInstanceAccess;
import io.github.lunathelemon.territorial.util.NbtUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceAccess {

    private static BlockPos lastPosApplied;

    @Unique
    public void territorial$setLastPosApplied(BlockPos pos) { lastPosApplied = pos;}
    @Unique
    public BlockPos territorial$getLastPosApplied() { return lastPosApplied; }

    @Inject(at = @At("HEAD"), method = "writeNbt")
    public void writeNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> cir) {
        if(lastPosApplied != null) {
            tag.putIntArray("last_pos_applied", NbtUtils.serializeVec3i(lastPosApplied));
        }
    }

    @Inject(at = @At("HEAD"), method = "fromNbt(Lnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;")
    private static void fromNbt(StatusEffect statusEffect, NbtCompound tag, CallbackInfoReturnable<StatusEffectInstance> ci) {
        if (tag.contains("last_pos_applied")) {
            lastPosApplied = new BlockPos(NbtUtils.deserializeVec3i(tag.getIntArray("last_pos_applied")));
        }
    }
}
