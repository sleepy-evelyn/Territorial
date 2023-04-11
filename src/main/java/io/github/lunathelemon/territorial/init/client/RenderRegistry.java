package io.github.lunathelemon.territorial.init.client;

import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import io.github.lunathelemon.territorial.block.entity.TerritorialBlockEntities;
import io.github.lunathelemon.territorial.client.render.block.entity.LaserBlockEntityRenderer;
import io.github.lunathelemon.territorial.client.render.block.entity.PlinthOfPeekingBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public final class RenderRegistry {

    public static void register() {
        registerRenderLayers();
        registerBlockEntityRenderers();

    }

    private static void registerRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(TerritorialBlocks.LASER_TRANSMITTER, RenderLayer.getCutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                TerritorialBlocks.ECLIPSE_ROSE,
                TerritorialBlocks.ECLIPSE_ROSE_BUSH);
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(TerritorialBlockEntities.LASER_BLOCK_ENTITY_TYPE, ctx -> new LaserBlockEntityRenderer());
        BlockEntityRendererFactories.register(TerritorialBlockEntities.PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE, PlinthOfPeekingBlockEntityRenderer::new);
    }
}
