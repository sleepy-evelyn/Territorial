package io.github.sleepy_evelyn.territorial.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;

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
