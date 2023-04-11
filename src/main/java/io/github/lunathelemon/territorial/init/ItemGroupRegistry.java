package io.github.lunathelemon.territorial.init;

import io.github.lunathelemon.territorial.Territorial;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayDeque;
import java.util.Queue;

public final class ItemGroupRegistry {

    public static final ItemGroup BASE_GROUP = FabricItemGroup.builder(new Identifier(Territorial.MOD_ID, "base_group"))
            .icon(() -> new ItemStack(Blocks.OBSIDIAN)).build();

    private static final Queue<ItemStack> registrationStackQueue = new ArrayDeque<>();

    public static void queueStackRegistration(ItemStack itemStack) {
        registrationStackQueue.add(itemStack);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(BASE_GROUP).register(entries -> {
            entries.addAll(registrationStackQueue);
        });
    }
}
