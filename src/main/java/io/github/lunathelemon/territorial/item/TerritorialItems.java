package io.github.lunathelemon.territorial.item;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.init.ItemGroupRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class TerritorialItems {

    public static final Item LENS = new LensItem();

    public static void registerAll() {
        register("lens", LENS);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM, new Identifier(Territorial.MOD_ID, id), item);
        ItemGroupRegistry.queueStackRegistration(item.getDefaultStack());
    }
}
