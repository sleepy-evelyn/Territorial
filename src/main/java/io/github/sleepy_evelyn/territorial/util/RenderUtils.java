package io.github.sleepy_evelyn.territorial.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.IOException;

public class RenderUtils {

    private static final Vector3f positiveX = new Vector3f(1, 0, 0);
    private static final Vector3f positiveY = new Vector3f(0, 1, 0);
    private static final float degreeMultiplier = 0.017453292F;

    /**
     * Utility for drawing lines made out of a series of quads in any direction
     *
     * @param matrices  Matrix stack, used to track the current view transformations e.g. translation, rotation
     * @param consumer  Buffer to render the model to
     * @param facing Direction in which the line should be drawn
     * @param w Line width
     * @param l Line length
     * @param colour RGB colour array
     * @param a Colour alpha
     */
    public static void drawQuadLine(MatrixStack matrices, VertexConsumer consumer, Direction facing, float w, float l, float[] colour, float a) {
        // Get the transformation matrix and translate to the center
        Matrix4f transMatrix = matrices.peek().getModel();
        matrices.translate(0.5, 0.5, 0.5);

        float r = colour[0];
        float g = colour[1];
        float b = colour[2];

        // TODO - Switch to facing.getRotationQuaternion() approach at some point to optimise
        // Translations constructed from a north facing direction, hence rotate for other directions
        switch(facing) {
            case UP -> matrices.multiply(new Quaternionf(new AxisAngle4f(toRadians(90), positiveX)));
            case DOWN -> matrices.multiply(new Quaternionf(new AxisAngle4f(toRadians(-90), positiveX)));
            case SOUTH -> matrices.multiply(new Quaternionf(new AxisAngle4f(toRadians(180), positiveX)));
            case EAST -> matrices.multiply(new Quaternionf(new AxisAngle4f(toRadians(-90), positiveY)));
            case WEST -> matrices.multiply(new Quaternionf(new AxisAngle4f(toRadians(90), positiveY)));
        }

        // left side
        matrices.translate(-w/2, -w/2, 0);
        consumer.vertex(transMatrix, 0, 0, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, w, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, w, -l).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, 0, -l).color(r,g,b,a).next();

        // right side
        matrices.translate(w, 0, 0);
        consumer.vertex(transMatrix, 0, 0, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, w, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, w, -l).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, 0, -l).color(r,g,b,a).next();

        // bottom side
        consumer.vertex(transMatrix, 0, 0, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, -w, 0, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, -w, 0, -l).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, 0, -l).color(r,g,b,a).next();

        // top side
        matrices.translate(0, w, 0);
        consumer.vertex(transMatrix, 0, 0, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, -w, 0, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, -w, 0, -l).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, 0, -l).color(r,g,b,a).next();

        // end bit
        matrices.translate(0, -w, -l);
        consumer.vertex(transMatrix, 0, 0, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, 0, w, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, -w, w, 0).color(r,g,b,a).next();
        consumer.vertex(transMatrix, -w, 0, 0).color(r,g,b,a).next();

        matrices.translate(0.5, 0.5, 0.5);
    }

    public static float toRadians(int degrees) {
        return degrees * degreeMultiplier;
    }

    public static int toDegrees(float radians) {
        return (int) (radians / degreeMultiplier);
    }

    public static class Shader {

        @Nullable
        public static ShaderEffect shader;

        public static void load(Identifier id) throws IOException {
            var client = MinecraftClient.getInstance();
            shader = new ShaderEffect(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), id);
            shader.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
        }

        public static void render(float tickDelta) {
            if(shader != null)
                shader.render(tickDelta);
        }

        public static void close() {
            if(shader != null) {
                shader.close();
                shader = null;
            }
        }

        public static boolean isLoaded() { return shader != null; }
    }
}
