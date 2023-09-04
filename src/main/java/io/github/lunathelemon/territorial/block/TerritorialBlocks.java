package io.github.lunathelemon.territorial.block;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.init.ItemGroupRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class TerritorialBlocks {

    public static final Block OMNISCIENT_OBSIDIAN = new OmniscientObsidianBlock();
    public static final Block ECLIPSE_ROSE = new EclipseWitherRoseBlock();
    public static final Block ECLIPSE_ROSE_BUSH = new EclipseRoseBushBlock();
    public static final Block ECLIPSE_TRAP = new Block(FabricBlockSettings.copyOf(Blocks.ROSE_BUSH));
    public static final Block LASER_TRANSMITTER = new LaserTransmitterBlock();
    public static final Block LASER_RECEIVER = new LaserReceiverBlock();
    public static final Block PLINTH_OF_PEEKING = new PlinthOfPeekingBlock();
    public static final Block CORRUPTED_BEACON = new CorruptedBeaconBlock();

    public static void registerAll() {
        register("omniscient_obsidian", OMNISCIENT_OBSIDIAN);
        register("eclipse_rose", ECLIPSE_ROSE);
        register("eclipse_rose_bush", ECLIPSE_ROSE_BUSH);
        register("eclipse_trap", ECLIPSE_TRAP);
        register("laser_transmitter", LASER_TRANSMITTER);
        register("laser_receiver", LASER_RECEIVER);
        register("plinth_of_peeking", PLINTH_OF_PEEKING);
        register("corrupted_beacon", CORRUPTED_BEACON, true, false);
    }


    private static void register(String id, Block block) {
        register(id, block, true, true);
    }
    private static void register(String id, Block block, boolean registerBlockItem, boolean addToGroupRegistry) {
        Registry.register(Registries.BLOCK, Territorial.getID(id), block);

        if (registerBlockItem) {
            var blockItem = new BlockItem(block, new FabricItemSettings());
            Registry.register(Registries.ITEM, Territorial.getID(id), blockItem);

            if(addToGroupRegistry)
                ItemGroupRegistry.queueStackRegistration(blockItem.getDefaultStack());
        }
    }
}
