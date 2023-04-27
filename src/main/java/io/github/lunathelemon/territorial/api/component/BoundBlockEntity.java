package io.github.lunathelemon.territorial.api.component;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface BoundBlockEntity {

    void addBoundEntity(Entity boundEntity);

    void removeBoundEntity(Entity boundEntity);

    BlockPos getPos();
}
