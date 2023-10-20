package net.examplemod.quilt;

import net.examplemod.ExampleMod;
import net.examplemod.platform.TerritorialConfig;
import net.examplemod.quilt.config.QuiltCommonConfig;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.config.v2.QuiltConfig;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ExampleModQuilt implements ModInitializer {

    public static final QuiltCommonConfig COMMON_CONFIG = QuiltConfig.create(ExampleMod.MOD_ID, "config", QuiltCommonConfig.class);

    @Override
    public void onInitialize(ModContainer mod) {
        TerritorialConfig.setCommon(COMMON_CONFIG);
        ExampleMod.init();
    }
}
