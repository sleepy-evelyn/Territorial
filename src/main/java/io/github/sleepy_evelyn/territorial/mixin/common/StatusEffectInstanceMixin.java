package io.github.sleepy_evelyn.territorial.mixin.common;

import io.github.sleepy_evelyn.territorial.access.StatusEffectInstanceAccess;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceAccess {

    @Unique
	private static BlockPos territorial$lastPosApplied;

    @Unique
    public void territorial$setLastPosApplied(BlockPos pos) { territorial$lastPosApplied = pos;}
    @Unique
    public BlockPos territorial$getLastPosApplied() { return territorial$lastPosApplied; }

    @Inject(at = @At("HEAD"), method = "writeNbt")
    public void writeNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> cir) {
        if(territorial$lastPosApplied != null) {
            tag.put("last_pos_applied", NbtHelper.fromBlockPos(territorial$lastPosApplied));
        }
    }

    @Inject(at = @At("HEAD"), method = "fromNbt(Lnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;")
    private static void fromNbt(StatusEffect statusEffect, NbtCompound tag, CallbackInfoReturnable<StatusEffectInstance> ci) {
        if (tag.contains("last_pos_applied")) {
			territorial$lastPosApplied = NbtHelper.toBlockPos(tag.getCompound("last_pos_applied"));
        }
    }
}
