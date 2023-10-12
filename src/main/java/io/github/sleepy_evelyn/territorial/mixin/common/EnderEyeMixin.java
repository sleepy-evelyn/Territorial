package io.github.sleepy_evelyn.territorial.mixin.common;

import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeMixin extends Item {

    public EnderEyeMixin(Settings settings) {
        super(settings);
    }

    @Inject(method="use", at=@At("HEAD"), cancellable = true)
    private void useOnPeekingPlinth(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        var itemStack = user.getStackInHand(hand);
        var hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);

        // Return early instead of throwing the ender pearl
        if (hitResult.getType() == HitResult.Type.BLOCK && world.getBlockState(hitResult.getBlockPos()).isOf(TerritorialBlocks.PLINTH_OF_PEEKING)) {
            if(!user.isSneaking())
                cir.setReturnValue(TypedActionResult.success(itemStack));
            else
                cir.setReturnValue(TypedActionResult.pass(itemStack));
        }

    }
}
