package io.github.lunathelemon.territorial.api.component;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public record BoundBlockEntityParams(RegistryKey<DimensionType> dimensionKey, BlockPos pos, int reach) {}
