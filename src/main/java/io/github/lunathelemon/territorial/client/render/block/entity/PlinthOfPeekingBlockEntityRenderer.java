package io.github.lunathelemon.territorial.client.render.block.entity;

import io.github.lunathelemon.territorial.block.entity.PlinthOfPeekingBlockEntity;
import io.github.lunathelemon.territorial.client.render.CustomRenderLayers;
import io.github.lunathelemon.territorial.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

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
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((boxIdx + 1) / numBoxes) * (be.getWorld().getTime() + tickDelta) * levelMultiplier));
                matrices.translate(-0.5, -0.5, -0.5);
                RenderUtils.drawQuadLine(matrices, vertexConsumers.getBuffer(CustomRenderLayers.QUAD_LINES), Direction.UP, (numBoxes - boxIdx) * topBoxWidth, boxLength, boxColour, boxAlpha);
                matrices.pop();
            }
        }

        // Draw the rotating podium eye
        if(podiumEye != Blocks.AIR.asItem()) {
            matrices.push();
            matrices.translate(0.5f, pyramidTop + 0.25f + offset, 0.5f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((be.getWorld().getTime() + tickDelta) * (levelMultiplier * 2)));

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
