package io.github.sleepy_evelyn.territorial.block;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.util.TickCounter;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public class EclipseRoseBushBlock extends TallFlowerBlock implements EclipseBlock {

    private final TickCounter DISPLAY_TICKER = new TickCounter(20);

    public EclipseRoseBushBlock() {
        super(FabricBlockSettings.create()
                .noCollision()
                .breakInstantly()
                .nonOpaque()
				.mapColor(MapColor.BLACK)
				.notSolid()
                .sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        applyBlindnessEffect(world, entity, 100);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
        if(state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            int maxReach = Territorial.getConfig().getEclipseRoseMaxReach();
            eclipseDisplayTick(state, world, pos, random, DISPLAY_TICKER, 300, maxReach);
        }
    }
}
