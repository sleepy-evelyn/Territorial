package io.github.sleepy_evelyn.territorial.mixin.common;

import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.piece.RuinedPortalStructurePiece;
import net.minecraft.structure.piece.SimpleStructurePiece;
import net.minecraft.structure.piece.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
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
    public void generate(StructureWorldAccess world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomGenerator random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo ci) {
        if(random.nextDouble() < 0.35D) {
            BlockBox blockBox = this.getStructure().calculateBoundingBox(this.placementData, this.pos);
            if (chunkBox.isInside(blockBox.getCenter())) {
                BlockPos.stream(this.getBoundingBox()).forEach((xPos) -> {
                    BlockState blockState = world.getBlockState(xPos);
                    if(blockState.isOf(Blocks.CRYING_OBSIDIAN))
                        world.setBlockState(xPos, TerritorialBlocks.OMNISCIENT_OBSIDIAN.getDefaultState(), 3);
                });
            }
        }
    }

    @Override
    public void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, RandomGenerator random, BlockBox boundingBox) {}
}

