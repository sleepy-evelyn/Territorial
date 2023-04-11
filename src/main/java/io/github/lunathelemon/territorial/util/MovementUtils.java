package io.github.lunathelemon.territorial.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
public class MovementUtils {

    public static boolean isMoving(PlayerEntity player) {
        var velocity = player.getVelocity();
        return (velocity.x != 0 || velocity.y != 0 || velocity.z != 0);
    }

    public static void randomTeleport(ServerWorld world, ServerPlayerEntity player) {
        randomTeleport(world, player, 0, 16, true, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
    }

    public static void randomTeleport(ServerWorld world, ServerPlayerEntity player, int minRange, int maxRange, boolean particleEffects, @Nullable SoundEvent soundEvent) {
        double xTpPos, yTpPos, zTpPos;

        for(int i = 0; i < 16; ++i) {
            xTpPos = player.getX() + (player.getRandom().nextDouble() - 0.5D) * (maxRange - minRange);
            yTpPos = MathHelper.clamp(player.getY() + (player.getRandom().nextInt(16) - 8), world.getBottomY(), (world.getBottomY() + world.getLogicalHeight() - 1));
            zTpPos = player.getZ() + (player.getRandom().nextDouble() - 0.5D) * (maxRange - minRange);

            if (player.hasVehicle()) player.stopRiding();
            if (player.teleport(xTpPos, yTpPos, zTpPos, particleEffects)) {
                if (soundEvent != null) {
                    world.playSound(null, xTpPos, yTpPos, zTpPos, soundEvent, SoundCategory.PLAYERS, 1F, 1F);
                    player.playSound(soundEvent, 1F, 1F);
                }
                break;
            }
        }
    }
}
