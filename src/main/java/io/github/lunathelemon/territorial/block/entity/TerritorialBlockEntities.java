package io.github.lunathelemon.territorial.block.entity;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TerritorialBlockEntities {

    public static final BlockEntityType<LaserTransmitterBlockEntity> LASER_BLOCK_ENTITY_TYPE
            = FabricBlockEntityTypeBuilder.create(LaserTransmitterBlockEntity::new, TerritorialBlocks.LASER_TRANSMITTER).build();
    public static final BlockEntityType<PlinthOfPeekingBlockEntity> PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE
            = FabricBlockEntityTypeBuilder.create(PlinthOfPeekingBlockEntity::new, TerritorialBlocks.PLINTH_OF_PEEKING).build();

    public static final BlockEntityType<CorruptedBeaconBlockEntity> CORRUPTED_BEACON_BLOCK_ENTITY_TYPE
            = FabricBlockEntityTypeBuilder.create(CorruptedBeaconBlockEntity::new, TerritorialBlocks.CORRUPTED_BEACON).build();


    public static void registerAll() {
        register("laser_be", LASER_BLOCK_ENTITY_TYPE);
        register("plinth_of_peeking_be", PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE);
        register("corrupted_beacon_be", CORRUPTED_BEACON_BLOCK_ENTITY_TYPE);
    }

    private static <T extends BlockEntity> void register(String id, BlockEntityType<T> beType) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Territorial.MOD_ID, id), beType);
    }
}
