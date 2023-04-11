package io.github.lunathelemon.territorial.mixin.common;

import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface AnvilChunkStorageAccessor {
    @Accessor("watchDistance")
    int getWatchDistance();
}
