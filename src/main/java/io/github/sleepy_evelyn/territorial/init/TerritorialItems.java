package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.item.LensItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class TerritorialItems {

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Territorial.id("territorial"));

    public static final LensItem LENS = register("lens", new LensItem());

    public static void initialize() {}

    static <T extends Item> T register(String path, T item) {
        return register(path, item, true);
    }

    static <T extends Item> T register(String path, T item, boolean addToGroupRegistry) {
        T registeredItem = Registry.register(Registries.ITEM, Territorial.id(path), item);
        if (addToGroupRegistry)
            ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register((entries) -> entries.addItem(item));
        return registeredItem;
    }
}
