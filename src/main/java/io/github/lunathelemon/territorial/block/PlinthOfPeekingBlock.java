package io.github.lunathelemon.territorial.block;

import io.github.lunathelemon.territorial.block.entity.PlinthOfPeekingBlockEntity;
import io.github.lunathelemon.territorial.block.entity.TerritorialBlockEntities;
import io.github.lunathelemon.territorial.component.TerritorialComponents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
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
    public static final BooleanProperty MAGIC_POWERED;

    public PlinthOfPeekingBlock() {
        super(FabricBlockSettings.copyOf(Blocks.ENCHANTING_TABLE));
        this.setDefaultState(this.getStateManager().getDefaultState().with(ENABLED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
        builder.add(MAGIC_POWERED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.75, 1);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var be = world.getBlockEntity(pos);
        var mainHandStack = player.getMainHandStack();

        if(be instanceof PlinthOfPeekingBlockEntity ppbe) {
            boolean hasEmptyPodium = ppbe.getPodiumStack().isEmpty();
            var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);

            if(hasEmptyPodium) {
                if(mainHandStack.getItem().equals(Items.ENDER_EYE)) {
                    ppbe.setPodiumStack(mainHandStack);
                    if (!player.getAbilities().creativeMode)
                        mainHandStack.decrement(1);
                }
            } else {
                if(player.isSneaking()) {
                    dropStack(world, pos.up(), ppbe.getPodiumStack());
                    ppbe.clearPodium();
                } else if(!peekingComponent.isPeeking()) {
                    ppbe.clearPodium();
                    if(!world.isClient)
                        peekingComponent.setPeeking(true);
                }
            }
        }
        return ActionResult.PASS;
    }

    public void onFinishPeeking() {

    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return super.getDroppedStacks(state, builder);
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
        MAGIC_POWERED = BooleanProperty.of("magic_powered");
    }
}
