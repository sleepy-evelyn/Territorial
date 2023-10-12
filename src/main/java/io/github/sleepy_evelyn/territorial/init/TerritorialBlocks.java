package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class TerritorialBlocks {

    public static final Block OMNISCIENT_OBSIDIAN = register("omniscient_obsidian", new OmniscientObsidianBlock());
    public static final Block ECLIPSE_ROSE = register("eclipse_rose", new EclipseWitherRoseBlock());
    public static final Block ECLIPSE_ROSE_BUSH = register("eclipse_rose_bush", new EclipseRoseBushBlock());
    public static final Block ECLIPSE_TRAP = register("eclipse_trap", new Block(FabricBlockSettings.copyOf(Blocks.ROSE_BUSH)));
    public static final Block LASER_TRANSMITTER = register("laser_transmitter", new LaserTransmitterBlock());
    public static final Block LASER_RECEIVER = register("laser_receiver", new LaserReceiverBlock());
    public static final Block PLINTH_OF_PEEKING = register("plinth_of_peeking", new PlinthOfPeekingBlock());
    public static final Block CORRUPTED_BEACON = register("corrupted_beacon", new CorruptedBeaconBlock(), true, false);

    public static void initialize() {}

    private static <T extends Block> T register(String path, T block) {
        return register(path, block, true, true);
    }

    private static <T extends Block> T register(String path, T block, boolean addToGroupRegistry) {
        return register(path, block, addToGroupRegistry, true);
    }

    private static <T extends Block> T register(String path, T block, boolean registerBlockItem, boolean addToGroupRegistry) {
        T registeredBlock = Registry.register(Registries.BLOCK, Territorial.id(path), block);
        if (registerBlockItem)
            TerritorialItems.register(path, new BlockItem(block, new FabricItemSettings()), addToGroupRegistry);
        return registeredBlock;
    }
}
