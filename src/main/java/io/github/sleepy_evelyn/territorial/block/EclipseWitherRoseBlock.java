package io.github.sleepy_evelyn.territorial.block;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.util.TickCounter;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.WitherRoseBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public class EclipseWitherRoseBlock extends WitherRoseBlock implements EclipseBlock {

    private final TickCounter DISPLAY_TICKER = new TickCounter(12);

    public EclipseWitherRoseBlock() {
		super(StatusEffects.WITHER, FabricBlockSettings.create()
			.noCollision()
			.breakInstantly()
			.nonOpaque()
			.notSolid()
			.sounds(BlockSoundGroup.GRASS)
		);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        applyBlindnessEffect(world, entity, 300);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
        super.randomDisplayTick(state, world, pos, random);
        int maxReach = Territorial.getConfig().getEclipseRoseMaxReach();
        eclipseDisplayTick(state, world, pos, random, DISPLAY_TICKER, 600, maxReach);
    }
}
