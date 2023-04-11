package io.github.lunathelemon.territorial.util;

import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MathUtils {

    public enum Shape { SPHERE, CUBE, CYLINDER }

    public static class Pos {
        public static Vec3d zeroMove(Vec3d vecIn, double amount) {
            return new Vec3d(
                    (vecIn.x == 0) ? vecIn.x + amount : vecIn.x,
                    (vecIn.y == 0) ? vecIn.y + amount : vecIn.y,
                    (vecIn.z == 0) ? vecIn.z + amount : vecIn.z
            );
        }
        public static float getDistanceAlongAxis(Vec3d vecStart, Vec3d vecFinish, Direction.Axis axis) {
            double dxstance = 0;
            switch(axis) {
                case X -> dxstance = Math.abs(vecFinish.x - vecStart.x);
                case Y -> dxstance = Math.abs(vecFinish.y - vecStart.y);
                case Z -> dxstance = Math.abs(vecFinish.z - vecStart.z);
            }
            return (float) dxstance;
        }
    }

    @NotNull
    public static ArrayList<BlockPos> getBlocksWithinCube(ServerWorld world, BlockPos centrePos, Block targetBlock, Integer radius) {
        return getBlocksWithinShape(world, centrePos, targetBlock, radius, null, Shape.CUBE);
    }

    @NotNull
    public static ArrayList<BlockPos> getBlocksWithinShape(ServerWorld world, BlockPos centrePos, Block targetBlock, Integer radius, @Nullable Integer height, Shape shape) {
        if(radius > 0) radius = radius - 1;
        else return new ArrayList<>();

        var minPos = centrePos.add(-radius, -radius, -radius);
        var maxPos = centrePos.add(radius, radius, radius);
        double radiusSquared = 0;

        if(shape == Shape.SPHERE) radiusSquared = 3 * Math.pow(radius, 2);
        else if(shape == Shape.CYLINDER) {
            radiusSquared = 2 * Math.pow(radius, 2);
            if(height != null) {
                minPos = new BlockPos(minPos.getX(), centrePos.getY() - height, minPos.getZ());
                maxPos = new BlockPos(maxPos.getX(), centrePos.getY() + height, maxPos.getZ());
            }
        }

        ArrayList<BlockPos> blocks = new ArrayList<>();
        boolean addBlock = false;
        BlockPos blockPos;
        for(int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for(int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for(int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    blockPos = new BlockPos(x, y, z);

                    if(shape == Shape.SPHERE) {
                        if(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) <= radiusSquared) addBlock = true;
                    }
                    else if(shape == Shape.CYLINDER) {
                        if(Math.pow(x, 2) + Math.pow(z, 2) <= radiusSquared) addBlock = true;
                    }
                    else {
                        addBlock = true;
                    }

                    if(addBlock) {
                        if(world.getBlockState(blockPos).getBlock() == targetBlock) blocks.add(blockPos);
                    }
                }
            }
        }
        return blocks;
    }
}
