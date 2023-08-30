package io.github.lunathelemon.territorial.util;

import io.github.lunathelemon.territorial.api.component.BoundBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NbtUtils {

    public static int[] serializeChunkPos(@NotNull ChunkPos chunkPos) {
        int[] chunkSerializable = new int[2];
        chunkSerializable[0] = chunkPos.x;
        chunkSerializable[1] = chunkPos.z;
        return chunkSerializable;
    }

    public static ChunkPos deserializeChunkPos(int[] posSerializable) {
        return new ChunkPos(posSerializable[0], posSerializable[1]);
    }

    public static <T extends Enum<T>> String serializeEnum(T enumValue) {
        return enumValue.name();
    }

    public static <T extends Enum<T>> T deserializeEnum(Class<T> enumClass, String enumNbtString) {
        return T.valueOf(enumClass, enumNbtString);
    }

    public static NbtCompound removeBlockFeatures(NbtCompound nbt) {
        if(nbt != null) {
            nbt.remove("id");
            nbt.remove("x");
            nbt.remove("y");
            nbt.remove("z");
        }
        return nbt;
    }
}
