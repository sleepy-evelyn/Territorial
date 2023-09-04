package io.github.lunathelemon.territorial.util;

import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeaconUtils {
    private static final List<Block> corruptedBeaconBaseBlocks;
    private static final Pair<Block, Float> corruptedBeaconMainBlockProportion;

    private static final int[] beaconSizes = { 9, 34, 83, 164 };

    public static int updateCorruptedBeaconLevel(final World world, final BlockPos pos) {
        return updateLevel(world, pos, corruptedBeaconBaseBlocks, corruptedBeaconMainBlockProportion);
    }

    public static int updateLevel(final World world, final BlockPos pos, final List<Block> beaconBaseBlocks, final Pair<Block, Float> mainBlockProportion) {
        final int bottomY = world.getBottomY();
        final Block mainBlock = mainBlockProportion.getLeft();

        int level = 0, mainBlockCount = 0, y, xPos, zPos;
        Block currentBlock;

        // Decrement the y value to check the blocks below
        for(int yInc = 1; yInc <= 4; level = yInc++) {
            y = pos.getY() - yInc;
            // If we reach the bottom of the world exist early
            if(y < bottomY)
                break;
            else {
                xPos = pos.getX();
                zPos = pos.getZ();
                // Check each slice of blocks below in the same way a beacon would
                for(int x = xPos - yInc; x <= xPos + yInc; ++x) {
                    for(int z = zPos - yInc; z <= zPos + yInc; ++z) {
                        currentBlock = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                        // Return the current level if a new block cannot be found
                        if(!beaconBaseBlocks.contains(currentBlock))
                            return checkProportionality(mainBlockProportion, mainBlockCount, level);
                        else if(currentBlock.equals(mainBlock))
                            mainBlockCount++;
                    }
                }
            }
        }
        return checkProportionality(mainBlockProportion, mainBlockCount, level);
    }

    private static int checkProportionality(final Pair<Block, Float> mainBlockProportion, int mainBlockCount, int level) {
        float proportion = mainBlockProportion.getRight();

        if(proportion > 0 && level > 0)
            return (float) mainBlockCount / beaconSizes[level-1] >= proportion ? level : 0;
        else
            return level;
    }

    static {
        corruptedBeaconBaseBlocks = List.of(TerritorialBlocks.OMNISCIENT_OBSIDIAN, Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN);
        corruptedBeaconMainBlockProportion = new Pair<>(TerritorialBlocks.OMNISCIENT_OBSIDIAN, 0.5f);
    }
}
