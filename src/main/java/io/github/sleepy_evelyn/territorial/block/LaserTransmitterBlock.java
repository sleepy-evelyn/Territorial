package io.github.sleepy_evelyn.territorial.block;

import io.github.sleepy_evelyn.territorial.block.entity.LaserTransmitterBlockEntity;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlockEntities;
import io.github.sleepy_evelyn.territorial.util.TextUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LaserTransmitterBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final BooleanProperty POWERED;
    public static final IntProperty POWER;
    public static final DirectionProperty FACING;

    public LaserTransmitterBlock() {
		super(FabricBlockSettings.create()
			.nonOpaque()
			.requiresTool()
			.strength(5.0F, 6.0F)
			.sounds(BlockSoundGroup.METAL)
		);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(POWERED, false)
                .with(POWER, 0)
                .with(Properties.FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING);
        stateManager.add(POWERED);
        stateManager.add(POWER);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos);
        world.setBlockState(pos, state.with(POWER, world.getReceivedRedstonePower(pos))
                .with(POWERED, powered), 3);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        world.setBlockState(pos, state.with(POWER, world.getReceivedRedstonePower(pos))
                .with(POWERED, world.isReceivingRedstonePower(pos)), 3);
        BlockEntity be = world.getBlockEntity(pos);
        if (!world.isClient && be instanceof LaserTransmitterBlockEntity lbe) {
            lbe.createFromLens(stack);
        }
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack toolStack) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005F);
        dropStack(world, pos, getDroppedStacks(state, (ServerWorld) world, pos, be).get(0));
        //onStacksDropped(state, (ServerWorld) world, pos, toolStack, false);
    }

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
		ItemStack stackToDrop = asItem().getDefaultStack();
		return List.of(
			((LaserTransmitterBlockEntity) builder.getParameter(LootContextParameters.BLOCK_ENTITY)).writeNbtStack(stackToDrop)
		);
	}

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.hasBlockEntity() && !state.isOf(newState.getBlock())) {
            var be = world.getBlockEntity(pos);
            if(be instanceof LaserTransmitterBlockEntity ltbe) {
                ltbe.updateLightBlocks(false, state.get(Properties.FACING));
                ltbe.setReceiverPowered(false);
            }
            world.removeBlockEntity(pos);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LaserTransmitterBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, TerritorialBlockEntities.LASER_BLOCK_ENTITY_TYPE, LaserTransmitterBlockEntity::tick);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        NbtCompound tag = stack.getSubNbt("beam");
        if(tag != null) {
            // Colour
            DyeColor dyeColour = Optional.of(DyeColor.byId(tag.getInt("colour"))).orElse(DyeColor.WHITE);

            // TODO - Crash when hovering over tooltip in Beacon GUI. Class not found exception :/
            String dyeNameCapitalized = dyeColour.getName().substring(0, 1).toUpperCase() + dyeColour.getName().substring(1);
            tooltip.add(Text.of(TextUtils.getTextColourFormatting(dyeColour) + dyeNameCapitalized));

            // Modifiers
            int strengthMod = tag.getByte("strength");
            tooltip.add(Text.translatable("tooltip.territorial.lens_strength_" + strengthMod));
            if(tag.getBoolean("light")) tooltip.add(Text.translatable("tooltip.territorial.modifier.light"));
            if(tag.getBoolean("highlight")) tooltip.add(Text.translatable("tooltip.territorial.modifier.highlight"));
            if(tag.getBoolean("death")) tooltip.add(Text.translatable("tooltip.territorial.modifier.death"));
            if(tag.getBoolean("rainbow")) tooltip.add(Text.translatable("tooltip.territorial.modifier.rainbow"));
            if(tag.getBoolean("sparkle")) tooltip.add(Text.translatable("tooltip.territorial.modifier.sparkle"));
        }
    }

    static {
        FACING = Properties.FACING;
        POWER = Properties.POWER;
        POWERED = Properties.POWERED;
    }
}
