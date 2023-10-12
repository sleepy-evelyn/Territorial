package io.github.sleepy_evelyn.territorial.compat;

import io.github.sleepy_evelyn.territorial.Territorial;
import net.fabricmc.loader.api.FabricLoader;

public class CreateCompat {
    public static void init() {
        if(FabricLoader.getInstance().isModLoaded("create")) {
            Territorial.LOGGER.info("Found Create as a dependency. Time to ponder away!");
        }
    }

}
