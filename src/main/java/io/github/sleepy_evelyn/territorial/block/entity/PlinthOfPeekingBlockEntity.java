package io.github.sleepy_evelyn.territorial.block.entity;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.api.component.BoundBlockEntity;
import io.github.sleepy_evelyn.territorial.api.component.BoundBlockEntityParams;
//import gay.sylv.territorial.api.component.IPeekingEyeComponent;
import io.github.sleepy_evelyn.territorial.config.TerritorialConfig;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlockEntities;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
//import gay.sylv.territorial.component.TerritorialComponents;
import io.github.sleepy_evelyn.territorial.util.BeaconUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PlinthOfPeekingBlockEntity extends BlockEntity implements BoundBlockEntity {

    private static final int[] reachMultipliers = { 1, 3, 8, 16, 27 };
    // TODO - Move this to a tag at some point
    private static final List<Block> acceptedBeaconBaseBlocks = List.of(
            TerritorialBlocks.OMNISCIENT_OBSIDIAN, Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN
    );

    private int level;
    private Item podiumItem;
    private final HashSet<UUID> boundPlayerUuids;

    public PlinthOfPeekingBlockEntity(BlockPos pos, BlockState state) {
        super(TerritorialBlockEntities.PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE, pos, state);
        boundPlayerUuids = new HashSet<>();
        podiumItem = Items.ENDER_EYE;
    }

    public static void tick(World world, BlockPos pos, BlockState state, PlinthOfPeekingBlockEntity be) {
        if(world.getTime() % 80 == 0) {
            int newLevel = BeaconUtils.updateLevel(
                    world, pos, acceptedBeaconBaseBlocks,
                    new Pair<>(TerritorialBlocks.OMNISCIENT_OBSIDIAN, 0.5f));

            // Level has changed
            if(newLevel != be.level) {
                world.setBlockState(pos, state.with(Properties.ENABLED, newLevel > 0));
                //be.getBoundPlayerComponents().forEach(peekingComponent -> peekingComponent.rebind(be));
            }
            be.level = newLevel;
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("level", level);
        nbt.putString("podiumItem", Registries.ITEM.getId(podiumItem).toString());

        var uuidList = new NbtList();
        for(var boundUuid : boundPlayerUuids)
            uuidList.add(NbtHelper.fromUuid(boundUuid));
        nbt.put("boundPlayers", uuidList);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if(nbt.contains("level"))
            level = nbt.getInt("level");
        if(nbt.contains("podiumItem"))
            this.podiumItem = Registries.ITEM.get(new Identifier(nbt.getString("podiumItem")));
        if(nbt.contains("boundPlayers")) {
            NbtList boundPlayersNbtList = nbt.getList("boundPlayers", NbtElement.INT_ARRAY_TYPE);
            for(var boundUuidIntArray : boundPlayersNbtList)
                boundPlayerUuids.add(NbtHelper.toUuid(boundUuidIntArray));
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.of(this);
    }

    @Override
    public NbtCompound toSyncedNbt() {
        return toNbt();
    }

    public Item getPodiumEye() { return podiumItem; }
    public void setPodiumEye(Item podiumItem) { this.podiumItem = podiumItem; }
    public int getLevel() { return level; }

    @Override
    public void addBoundEntity(Entity boundEntity) {
        if(boundEntity instanceof PlayerEntity playerEntity)
            boundPlayerUuids.add(playerEntity.getUuid());
    }

    @Override
    public void removeBoundEntity(Entity boundEntity) {
        if(boundEntity instanceof PlayerEntity playerEntity)
            boundPlayerUuids.remove(playerEntity.getUuid());
    }

    @Override
    public void onBlockDestroyed() {
        //getBoundPlayerComponents().forEach(IPeekingEyeComponent::stopPeeking);
    }

    @Override
    public BoundBlockEntityParams getParams() {
        if(world != null)
            return new BoundBlockEntityParams(world.getDimensionKey(), pos,
                    6 * reachMultipliers[level]);
        else return null;
    }


    /**
    private List<IPeekingEyeComponent> getBoundPlayerComponents() {
        List<IPeekingEyeComponent> components = new ArrayList<>();

        if(world != null) {
            for(var boundPlayerUuid : boundPlayerUuids) {
                var boundPlayer = world.getPlayerByUuid(boundPlayerUuid);
                if(boundPlayer != null)
                    components.add(boundPlayer.getComponent(TerritorialComponents.PEEKING_EYE));
            }
        }
        return components;
    }**/

}
