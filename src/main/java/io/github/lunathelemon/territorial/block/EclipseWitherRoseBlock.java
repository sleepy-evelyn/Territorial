package io.github.lunathelemon.territorial.block;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.util.TickCounter;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.WitherRoseBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class EclipseWitherRoseBlock extends WitherRoseBlock implements EclipseBlock {

    private final TickCounter DISPLAY_TICKER = new TickCounter(12);

    public EclipseWitherRoseBlock() {
        super(StatusEffects.WITHER, FabricBlockSettings.of(Material.PLANT)
                .noCollision()
                .breakInstantly()
                .nonOpaque()
                .sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        applyBlindnessEffect(world, entity, 300);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        int maxReach = Territorial.getConfig().getEclipseRoseMaxReach();
        eclipseDisplayTick(state, world, pos, random, DISPLAY_TICKER, 600, maxReach);
    }
}