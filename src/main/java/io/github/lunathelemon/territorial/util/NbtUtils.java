package io.github.lunathelemon.territorial.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

public class NbtUtils {

    public static int[] serializeVec3i(@NotNull Vec3i vec3i) {
        int[] posSerializable = new int[3];
        posSerializable[0] = vec3i.getX();
        posSerializable[1] = vec3i.getY();
        posSerializable[2] = vec3i.getZ();
        return posSerializable;
    }

    public static Vec3i deserializeVec3i(int[] posSerializable) {
        return new Vec3i(posSerializable[0], posSerializable[1], posSerializable[2]);
    }

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

    public static NbtCompound removeBlockFeatures(NbtCompound nbtCompound) {
        if(nbtCompound != null) {
            nbtCompound.remove("id");
            nbtCompound.remove("x");
            nbtCompound.remove("y");
            nbtCompound.remove("z");
        }
        return nbtCompound;
    }
}
