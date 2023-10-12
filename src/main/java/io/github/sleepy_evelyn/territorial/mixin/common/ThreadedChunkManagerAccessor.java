package io.github.sleepy_evelyn.territorial.mixin.common;

import net.minecraft.server.world.ThreadedChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThreadedChunkManager.class)
public interface ThreadedChunkManagerAccessor {
    @Accessor("watchDistance")
    int getWatchDistance();
}
