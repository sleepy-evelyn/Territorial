package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.block.entity.CorruptedBeaconBlockEntity;
import io.github.sleepy_evelyn.territorial.block.entity.LaserTransmitterBlockEntity;
import io.github.sleepy_evelyn.territorial.block.entity.PlinthOfPeekingBlockEntity;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class TerritorialBlockEntities {

	public static final BlockEntityType<LaserTransmitterBlockEntity> LASER_BLOCK_ENTITY_TYPE
            = register("laser", FabricBlockEntityTypeBuilder.create(LaserTransmitterBlockEntity::new, TerritorialBlocks.LASER_TRANSMITTER).build());
    public static final BlockEntityType<PlinthOfPeekingBlockEntity> PLINTH_OF_PEEKING_BLOCK_ENTITY_TYPE
            = register("plinth_of_peeking", FabricBlockEntityTypeBuilder.create(PlinthOfPeekingBlockEntity::new, TerritorialBlocks.PLINTH_OF_PEEKING).build());
    public static final BlockEntityType<CorruptedBeaconBlockEntity> CORRUPTED_BEACON_BLOCK_ENTITY_TYPE
            = register("corrupted_beacon", FabricBlockEntityTypeBuilder.create(CorruptedBeaconBlockEntity::new, TerritorialBlocks.CORRUPTED_BEACON).build());

    public static void initialize() {}

    private static <T extends BlockEntityType<?>> T register(String path, T beType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Territorial.id(path), beType);
    }
}
