package io.github.lunathelemon.territorial.mixin.common;

import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RuinedPortalStructurePiece.class)
public abstract class RuinedPortalStructurePieceMixin extends SimpleStructurePiece {

    public RuinedPortalStructurePieceMixin(StructurePieceType type, int length, StructureTemplateManager structureManager, Identifier id, String template, StructurePlacementData placementData, BlockPos pos) {
        super(type, length, structureManager, id, template, placementData, pos);
    }

    @Inject(at = @At("TAIL"), method = "generate")
    public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        if(random.nextDouble() < 0.35D) {
            BlockBox blockBox = this.template.calculateBoundingBox(this.placementData, this.pos);
            if (chunkBox.contains(blockBox.getCenter())) {
                BlockPos.stream(this.getBoundingBox()).forEach((xPos) -> {
                    BlockState blockState = world.getBlockState(xPos);
                    if(blockState.isOf(Blocks.CRYING_OBSIDIAN))
                        world.setBlockState(xPos, TerritorialBlocks.OMNISCIENT_OBSIDIAN.getDefaultState(), 3);
                });
            }
        }
    }

    @Override
    public void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {}
}

