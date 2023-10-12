package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.entity.effect.EclipseStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class TerritorialStatusEffects {

    public static final EclipseStatusEffect ECLIPSE_EFFECT = register("eclipse", new EclipseStatusEffect());

    public static void initialize() {}

    private static <T extends StatusEffect> T register(String path, T statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, Territorial.id(path), statusEffect);
    }
}
