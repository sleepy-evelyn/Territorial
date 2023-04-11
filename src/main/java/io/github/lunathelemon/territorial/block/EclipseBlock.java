package io.github.lunathelemon.territorial.block;

import io.github.lunathelemon.territorial.entity.effect.TerritorialStatusEffects;
import io.github.lunathelemon.territorial.networking.c2s.AddEclipseEffectPacket;
import io.github.lunathelemon.territorial.util.TickCounter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

/**
 * Interface that allows a block to trigger an Eclipse.
 * An Eclipse involves switching the players time on the client only from day > night and applying blindness on collision.
 */
public interface EclipseBlock {

    /**
     * Random display tick that displays an Eclipse particle (squid ink) above the block and consequently triggers an Eclipse for the player.
     * You can override behaviour but the block must send an AddEclipseEffectPacket to the server at some point.
     *
     * @param ticker Custom tick counter. See "utils/TickCounter" for an implementation
     * @param totalDuration How long the effect should last in milliseconds
     * @param maxReach How far away from the block can a player be for it to trigger the effect
     */
    @Environment(EnvType.CLIENT)
    default void eclipseDisplayTick(BlockState state, World world, BlockPos pos, Random random, TickCounter ticker, int totalDuration, int maxReach) {
        if (random.nextBoolean() && ticker.test()) { // Random tick
            var block = state.getBlock();
            var voxelShape = block.getOutlineShape(state, world, pos, ShapeContext.absent());
            var vec3d = voxelShape.getBoundingBox().getCenter();
            double x = pos.getX() + vec3d.x, z = pos.getZ() + vec3d.z;

            // Generate Squid Ink particles directly above the bounding box for the block
            for(int i=0 ; i < 5; i++) {
                double xPos = x + random.nextDouble() / 5.0D;
                double zPos = z + random.nextDouble() / 5.0D;
                double yPos = pos.getY() + ((voxelShape.getMax(Direction.Axis.Y) + 1.5D) - random.nextDouble());
                world.addParticle(ParticleTypes.SQUID_INK, xPos,yPos, zPos, 0.0D, 0.5D, 0.0D);
            }

            var player = MinecraftClient.getInstance().player;
            var effect = player.getStatusEffect(TerritorialStatusEffects.ECLIPSE_EFFECT); // Check player's status effects
            var duration = effect != null ? effect.getDuration() : 0; // Get the current duration since the effect has been applied
            if(duration < totalDuration * 0.9D) { // Prevent packet spam
                if(!player.isCreative() && !player.isSpectator() && player.world.getDimension().hasSkyLight()) {
                    if(player.getBlockPos().getSquaredDistance(pos) <= maxReach * maxReach)
                        new AddEclipseEffectPacket(totalDuration).send(); // Notify the server that an eclipse effect should be applied for this player
                }
            }
            world.playSound(player, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 0.3F, 0.05F);
            world.playSound(player, pos, SoundEvents.BLOCK_MOSS_BREAK, SoundCategory.BLOCKS, 0.3F, 0.2F);
        }
        else ticker.increment();
    }

    /**
     * Applies a blindness effect to any entity that collides with the blocks bounding box
     */
    default void applyBlindnessEffect(World world, Entity entity, int duration) {
        if (world.isClient && world.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity instanceof LivingEntity livingEntity) {
                if(livingEntity instanceof PlayerEntity playerEntity) {
                    if(playerEntity.isCreative()) return;
                }
                var blindnessEffectInstance = new StatusEffectInstance(StatusEffects.BLINDNESS, duration, 0);
                livingEntity.addStatusEffect(blindnessEffectInstance);
            }
        }
    }
}
