package io.github.sleepy_evelyn.territorial.block.entity;

import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
import io.github.sleepy_evelyn.territorial.init.TerritorialTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public abstract class CorruptedPyramidBlockEntity extends BlockEntity {
    private static final int[] beaconSizes = { 9, 34, 83, 164 };
    protected Block mainPyramidBlock = TerritorialBlocks.OMNISCIENT_OBSIDIAN;
    protected float mainBlockFormationThreshold = 0.5f;

    public CorruptedPyramidBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected int updateLevel() {
        final int bottomY = world.getBottomY();

        int level = 0, mainPyramidBlockCount = 0, y, xPos, zPos;
        BlockState currentState;

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
                        currentState = world.getBlockState(new BlockPos(x, y, z));

                        // Return the current level if a new block cannot be found
                        if(!currentState.isIn(TerritorialTags.CORRUPTED_PYRAMID)) {
                            return checkFormationThreshold(mainPyramidBlockCount, level);
                        } else if(currentState.getBlock().equals(mainPyramidBlock)) {
                            mainPyramidBlockCount++;
                        }
                    }
                }
            }
        }
        return checkFormationThreshold(mainPyramidBlockCount, level);
    }

    protected int checkFormationThreshold(int mainPyramidBlockCount, int level) {
        if(mainBlockFormationThreshold > 0 && level > 0)
            return (float) mainPyramidBlockCount / beaconSizes[level-1] >= mainBlockFormationThreshold ? level : 0;
        else
            return level;
    }
}
