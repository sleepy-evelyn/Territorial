package io.github.lunathelemon.territorial.compat;

import io.github.lunathelemon.territorial.Territorial;
import net.fabricmc.loader.api.FabricLoader;

public class CreateCompat {
    public static void init() {
        if(FabricLoader.getInstance().isModLoaded("create")) {
            Territorial.LOGGER.info("Found Create as a dependency. Time to ponder away!");
        }
    }

}
