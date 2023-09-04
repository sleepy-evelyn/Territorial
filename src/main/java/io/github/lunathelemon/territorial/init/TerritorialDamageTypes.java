package io.github.lunathelemon.territorial.init;

import io.github.lunathelemon.territorial.Territorial;
import net.minecraft.entity.damage.DamageSource;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class TerritorialDamageTypes {
    public static final RegistryKey<DamageType> OMNISCIENT_OBSIDIAN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Territorial.MOD_ID, "omniscient_obsidian"));
    public static final RegistryKey<DamageType> LASER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Territorial.MOD_ID, "laser"));

    public static DamageSource create(World world, RegistryKey<DamageType> key) {
        return new DamageSource(
                world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key)
        );
    }

}
