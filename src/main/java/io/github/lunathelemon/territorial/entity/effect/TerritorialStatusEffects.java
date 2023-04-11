package io.github.lunathelemon.territorial.entity.effect;

import io.github.lunathelemon.territorial.Territorial;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class TerritorialStatusEffects {
    public static final EclipseStatusEffect ECLIPSE_EFFECT = new EclipseStatusEffect();

    public static void registerAll() {
        register("eclipse", ECLIPSE_EFFECT);
    }

    private static void register(String id, StatusEffect statusEffect) {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Territorial.MOD_ID, id), statusEffect);
    }
}
