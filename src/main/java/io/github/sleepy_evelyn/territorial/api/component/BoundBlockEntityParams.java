package io.github.sleepy_evelyn.territorial.api.component;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public record BoundBlockEntityParams(RegistryKey<DimensionType> dimensionKey, BlockPos pos, int reach) {}
