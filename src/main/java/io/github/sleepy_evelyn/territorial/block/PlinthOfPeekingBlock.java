package io.github.sleepy_evelyn.territorial.block;

import io.github.sleepy_evelyn.territorial.block.entity.PlinthOfPeekingBlockEntity;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlockEntities;
//import gay.sylv.territorial.component.TerritorialComponents;
import io.github.sleepy_evelyn.territorial.util.NetworkingUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlinthOfPeekingBlock extends BlockWithEntity {

    public static final BooleanProperty ENABLED;

    public PlinthOfPeekingBlock() {
		super(FabricBlockSettings.copyOf(Blocks.ENCHANTING_TABLE));
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(ENABLED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.75, 1);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var be = world.getBlockEntity(pos);
        var mainHandStack = player.getMainHandStack();

        if(be instanceof PlinthOfPeekingBlockEntity ppbe && !world.isClient) {
            boolean hasEmptyPodium = ppbe.getPodiumEye() == Items.AIR;
            //var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);

            if(hasEmptyPodium) {
                if(mainHandStack.getItem() instanceof EnderEyeItem enderEyeItem) {
                    ppbe.setPodiumEye(enderEyeItem);
                    if (!player.getAbilities().creativeMode)
                        mainHandStack.decrement(1);
                }
            } else {
                if(player.isSneaking()) {
                    dropStack(world, pos.up(), ppbe.getPodiumEye().getDefaultStack());
                    ppbe.setPodiumEye(Items.AIR);
                } /* else if(!peekingComponent.isPeeking()) {
                    ppbe.setPodiumEye(Items.AIR);
                    peekingComponent.startPeeking(ppbe);
                }*/
            }
            NetworkingUtils.markDirtyAndSync(be, world);
        }
        return ActionResult.PASS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.hasBlockEntity() && !state.isOf(newState.getBlock())) {
            var be = world.getBlockEntity(pos);

            if(be instanceof PlinthOfPeekingBlockEntity ppbe)
                ppbe.onBlockDestroyed();
            world.removeBlockEntity(pos);
        }
    }

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder lootParameterBuilder) {
		return super.getDroppedStacks(state, lootParameterBuilder);
	}

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlinthOfPeekingBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, TerritorialBlockEntities.PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE, PlinthOfPeekingBlockEntity::tick);
    }

    static {
        ENABLED = Properties.ENABLED;
    }
}
