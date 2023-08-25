package io.github.lunathelemon.territorial.block;

import io.github.lunathelemon.territorial.block.entity.CorruptedBeaconBlockEntity;
import io.github.lunathelemon.territorial.block.entity.TerritorialBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CorruptedBeaconBlock extends BeaconBlock {
    public static final BooleanProperty CORRUPTED = BooleanProperty.of("corrupted");

    public CorruptedBeaconBlock() {
        super(FabricBlockSettings.copyOf(Blocks.BEACON));
        setDefaultState(getStateManager().getDefaultState()
                .with(CORRUPTED, false)
        );
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CorruptedBeaconBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CORRUPTED);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, TerritorialBlockEntities.CORRUPTED_BEACON_BLOCK_ENTITY_TYPE, CorruptedBeaconBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            var blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof CorruptedBeaconBlockEntity cbbe) {
                player.openHandledScreen(cbbe);
                player.incrementStat(Stats.INTERACT_WITH_BEACON);
            }
            return ActionResult.CONSUME;
        }
    }
}
