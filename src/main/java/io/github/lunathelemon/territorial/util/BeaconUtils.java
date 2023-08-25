package io.github.lunathelemon.territorial.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BeaconUtils {

    public static int updateLevel(final World world, final BlockPos pos, final List<Block> targetBlocks) {
        int level = 0;
        final int bottomY = world.getBottomY();
        int y, xPos, zPos;

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
                        // Return the current level if a new block cannot be found
                        if(!targetBlocks.contains(world.getBlockState(new BlockPos(x, y, z)).getBlock())) {
                            return level;
                        }
                    }
                }
            }
        }
        return level;
    }
}
