package io.github.sleepy_evelyn.territorial.mixin.common;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntityAccessor {
    @Invoker("updateLevel")
    static int updateLevel(World world, int x, int y, int z) {
        throw new AssertionError();
    }
}
