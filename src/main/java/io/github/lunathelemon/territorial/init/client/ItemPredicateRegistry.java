package io.github.lunathelemon.territorial.init.client;

import io.github.lunathelemon.territorial.item.TerritorialItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ItemPredicateRegistry {

    public static void register() {
        ModelPredicateProviderRegistry.register(TerritorialItems.LENS, new Identifier("colour"), (itemStack, clientWorld, livingEntity, seed) -> {
            var beamNbt = itemStack.getSubNbt("beam");
            if(beamNbt != null) {
                int colourId = beamNbt.getInt("colour");
                return colourId / 16F;
            }
            return 0F;
        });
    }
}
