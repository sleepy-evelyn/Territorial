package io.github.sleepy_evelyn.territorial.client.render.block.entity;

import io.github.sleepy_evelyn.territorial.block.entity.PlinthOfPeekingBlockEntity;
import io.github.sleepy_evelyn.territorial.client.render.CustomRenderLayers;
import io.github.sleepy_evelyn.territorial.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class PlinthOfPeekingBlockEntityRenderer implements BlockEntityRenderer<PlinthOfPeekingBlockEntity> {

    private static final float boxHeightStart = 0.95f, boxLength = 0.1f, topBoxWidth = 0.12f, boxAlpha = 180f;
    private static final float[] boxColour = new float[]{0.25f, 0.7f, 0.5f};

    public PlinthOfPeekingBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(PlinthOfPeekingBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var clientInstance = MinecraftClient.getInstance();
        var podiumEye = be.getPodiumEye();
        int levelMultiplier = be.getLevel() + 1;
        double offset = Math.sin((be.getWorld().getTime() + tickDelta) / (32.0 / levelMultiplier)) / 30.0;
        float numBoxes = be.getLevel();
        float pyramidTop = boxHeightStart + (numBoxes * boxLength);

        // Draw the beacon pyramid from boxes
        if(numBoxes > 0) {
            for(int boxIdx = 0; boxIdx <= numBoxes; boxIdx++) {
                matrices.push();
                matrices.translate(0.5, boxHeightStart + ((boxIdx + 1) * boxLength), 0.5);
                matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(((boxIdx + 1) / numBoxes) * (be.getWorld().getTime() + tickDelta) * levelMultiplier));
                matrices.translate(-0.5, -0.5, -0.5);
                RenderUtils.drawQuadLine(matrices, vertexConsumers.getBuffer(CustomRenderLayers.QUAD_LINES), Direction.UP, (numBoxes - boxIdx) * topBoxWidth, boxLength, boxColour, boxAlpha);
                matrices.pop();
            }
        }

        // Draw the rotating podium eye
        if(podiumEye != Blocks.AIR.asItem()) {
            matrices.push();
            matrices.translate(0.5f, pyramidTop + 0.25f + offset, 0.5f);
            matrices.multiply(Axis.Y_POSITIVE.rotationDegrees((be.getWorld().getTime() + tickDelta) * (levelMultiplier * 2)));

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
