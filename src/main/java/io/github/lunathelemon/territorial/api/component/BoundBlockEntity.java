package io.github.lunathelemon.territorial.api.component;

import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface BoundBlockEntity {

    void addBoundEntity(Entity boundEntity);

    void removeBoundEntity(Entity boundEntity);

    void onBlockDestroyed();

    @Nullable BoundBlockEntityParams getParams();
}
