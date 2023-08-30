package io.github.lunathelemon.territorial.client.render.block.entity;

import io.github.lunathelemon.territorial.block.entity.LaserTransmitterBlockEntity;
import io.github.lunathelemon.territorial.client.render.CustomRenderLayers;
import io.github.lunathelemon.territorial.util.MathUtils;
import io.github.lunathelemon.territorial.util.RenderUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.Arrays;

public class LaserBlockEntityRenderer implements BlockEntityRenderer<LaserTransmitterBlockEntity> {

    private static final float[][] RAINBOW_COLOURS = new float[][]{
            {176, 46, 38},  // Red
            {249, 128, 29}, // Orange
            {255, 216, 61}, // Yellow
            {93, 124, 21},  // Lime
            {60, 68, 169},  // Blue
            {137, 50, 183}  // Purple
    };

    private static final float[] rainbowColour = new float[]{0, 0, 0};
    private static int rainbowTargetIndex = 0;

    public LaserBlockEntityRenderer() {
        ClientTickEvents.START_WORLD_TICK.register((clientWorld) -> LaserBlockEntityRenderer.rainbowColourTick());
        ClientTickEvents.END_CLIENT_TICK.register((client) -> LaserBlockEntityRenderer.rainbowColourTick());
    }

    @Override
    public void render(LaserTransmitterBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction facing = be.getCachedState().get(Properties.FACING);
        int power = be.getCachedState().get(Properties.POWER);
        boolean isRainbow = be.getMods().get("rainbow");
        boolean isSparkle = be.getMods().get("sparkle");

        float[] colour = isRainbow ? rainbowColour : be.getColour().getColorComponents();
        VertexConsumer lineConsumer = vertexConsumers.getBuffer(CustomRenderLayers.QUAD_LINES);

        // Don't render anything if it isn't being powered
        if(power > 0) {
            if(isSparkle) {
                var clientWorld = (ClientWorld) be.getWorld();
                if(clientWorld != null) {
                    if(be.getSparkleDistance() < be.getReach()) {
                        Vec3d sparklePos = Vec3d.of(be.getPos()).add(MathUtils.Pos.zeroMove(Vec3d.of(facing.getVector()).multiply(be.getSparkleDistance()), 0.5));
                        clientWorld.addParticle(new DustParticleEffect(new Vector3f(colour[0], colour[1], colour[2]), 1f),
                                true, sparklePos.getX() , sparklePos.getY(), sparklePos.getZ(),
                                0.1, 0.1, 0.1);
                    }
                    be.incrementSparkleDistance();
                    if(be.getSparkleDistance() >= be.getMaxReach())
                        be.resetSparkleDistance();
                }
            }
            else {
                float w = LaserTransmitterBlockEntity.SIGNAL_STRENGTH_WIDTHS[power - 1];
                float l = be.getPrevReach() + ((be.getReach() - be.getPrevReach()) * tickDelta);

                // Opaque beam
                matrices.push();
                RenderUtils.drawQuadLine(matrices, lineConsumer, facing, w/2, l, colour, 10);
                matrices.pop();

                // Transparent beam (fancy graphics)
                if(MinecraftClient.isFancyGraphicsOrBetter()) {
                    matrices.push();
                    RenderUtils.drawQuadLine(matrices, lineConsumer, facing, w, l, colour, 180);
                    matrices.pop();
                }
            }
        }
        // Lens
        matrices.push();
        RenderUtils.drawQuadLine(matrices, lineConsumer, facing, 0.5f, 0.38f, colour, 220);
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(LaserTransmitterBlockEntity blockEntity) {
        return true;
    }

    public static void rainbowColourTick() {
        // TODO - Only run this if a rainbow laser is found
        float[] targetColour = RAINBOW_COLOURS[rainbowTargetIndex];
        for(int i=0; i < 3; i++) {
            if(targetColour[i] != rainbowColour[i]) {
                if(targetColour[i] - rainbowColour[i] > 0)
                    rainbowColour[i]++;
                else
                    rainbowColour[i]--;
            }
            else {
                if(Arrays.equals(targetColour, rainbowColour)) {
                    if(rainbowTargetIndex == (RAINBOW_COLOURS.length - 1))
                        rainbowTargetIndex = 0;
                    else
                        rainbowTargetIndex++;
                    break;
                }
            }
        }
    }
}
