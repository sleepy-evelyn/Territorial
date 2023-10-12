package io.github.sleepy_evelyn.territorial.init.client;

import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlockEntities;
import io.github.sleepy_evelyn.territorial.client.render.block.entity.CorruptedBeaconBlockEntityRenderer;
import io.github.sleepy_evelyn.territorial.client.render.block.entity.LaserBlockEntityRenderer;
import io.github.sleepy_evelyn.territorial.client.render.block.entity.PlinthOfPeekingBlockEntityRenderer;
import io.github.sleepy_evelyn.territorial.screen.TerritorialScreenHandlers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public final class RenderRegistry {

    public static void register() {
        registerRenderLayers();
        registerBlockEntityRenderers();
        registerScreens();
    }

    private static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(TerritorialBlocks.LASER_TRANSMITTER, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                TerritorialBlocks.ECLIPSE_ROSE,
                TerritorialBlocks.ECLIPSE_ROSE_BUSH,
                TerritorialBlocks.CORRUPTED_BEACON
        );
    }

    private static void registerScreens() {
		//HandledScreens.register(TerritorialScreenHandlers.CORRUPTED_BEACON_SCREEN_HANDLER_TYPE, CorruptedBeaconScreen::new);
    }

    private static void registerBlockEntityRenderers() {
       BlockEntityRendererFactories.register(TerritorialBlockEntities.LASER_BLOCK_ENTITY_TYPE, ctx -> new LaserBlockEntityRenderer());
	   BlockEntityRendererFactories.register(TerritorialBlockEntities.PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE, PlinthOfPeekingBlockEntityRenderer::new);
	   BlockEntityRendererFactories.register(TerritorialBlockEntities.CORRUPTED_BEACON_BLOCK_ENTITY_TYPE, ctx -> new CorruptedBeaconBlockEntityRenderer());
    }
}
