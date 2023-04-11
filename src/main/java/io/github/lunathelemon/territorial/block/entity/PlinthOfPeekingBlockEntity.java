package io.github.lunathelemon.territorial.block.entity;

import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PlinthOfPeekingBlockEntity extends BlockEntity {

    private int level;
    private ItemStack podiumStack;

    public PlinthOfPeekingBlockEntity(BlockPos pos, BlockState state) {
        super(TerritorialBlockEntities.PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE, pos, state);
        level = 0;
        podiumStack = ItemStack.EMPTY;
    }

    public static void tick(World world, BlockPos pos, BlockState state, PlinthOfPeekingBlockEntity be) {
        if(world.getTime() % 80 == 0) {
            int newLevel = be.updateLevel(world, pos);

            if(newLevel != be.level)
                world.setBlockState(pos, state.with(Properties.ENABLED, newLevel > 0));
            be.level = newLevel;
        }
    }

    private int updateLevel(World world, BlockPos pos) {
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
                        if(!world.getBlockState(new BlockPos(x, y, z)).getBlock()
                                .equals(TerritorialBlocks.OMNISCIENT_OBSIDIAN)) {
                             return level;
                        }
                    }
                }
            }
        }
        return level;
    }

    @NotNull
    public ItemStack getPodiumStack() { return podiumStack; }
    public void setPodiumStack(ItemStack podiumStack) { this.podiumStack = podiumStack.copyWithCount(1); }
    public void clearPodium() { this.podiumStack = ItemStack.EMPTY; }

    public int getLevel() { return level; }
}
