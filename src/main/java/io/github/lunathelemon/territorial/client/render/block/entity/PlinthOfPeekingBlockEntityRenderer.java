package io.github.lunathelemon.territorial.client.render.block.entity;

import io.github.lunathelemon.territorial.block.entity.PlinthOfPeekingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class PlinthOfPeekingBlockEntityRenderer implements BlockEntityRenderer<PlinthOfPeekingBlockEntity> {

    public PlinthOfPeekingBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(PlinthOfPeekingBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var clientInstance = MinecraftClient.getInstance();
        var podiumEye = be.getPodiumEye();
        double offset;

        if(podiumEye != null) {
            int levelMultiplier = be.getLevel() + 1;

            offset = Math.sin((be.getWorld().getTime() + tickDelta) / (32.0 / levelMultiplier)) / 16.0;

            matrices.push();
            matrices.translate(0.5, 1.25 + offset, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((be.getWorld().getTime() + tickDelta) * (levelMultiplier * 3)));

            int lightAbove = WorldRenderer.getLightmapCoordinates(be.getWorld(), be.getPos().up());
            clientInstance.getItemRenderer().renderItem(podiumEye.getDefaultStack(), ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, be.getWorld(), 0);
            matrices.pop();
        }
    }

    @Override
    public boolean rendersOutsideBoundingBox(PlinthOfPeekingBlockEntity blockEntity) {
        return true;
    }
}
