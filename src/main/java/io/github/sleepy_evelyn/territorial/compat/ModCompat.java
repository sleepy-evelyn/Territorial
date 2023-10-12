package io.github.sleepy_evelyn.territorial.compat;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Acquired from <a href="https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/compat/Mods.java">Create</a> to make dependency management easier :3
 **/
public enum ModCompat {
    CREATE;

    public boolean isLoaded() { return FabricLoader.getInstance().isModLoaded(asId()); }

    public String asId() { return name().toLowerCase(); }

    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        if (isLoaded())
            return Optional.of(toRun.get().get());
        return Optional.empty();
    }

    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (isLoaded())
            toExecute.get().run();
    }
}