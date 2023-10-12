package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.Territorial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class TerritorialDamageSources {

    public static final RegistryKey<DamageType> OMNISCIENT_OBSIDIAN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Territorial.id("omniscient_obsidian"));
    public static final RegistryKey<DamageType> LASER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Territorial.id("laser"));

	public static DamageSource omniscientObsidian(World world) {
		return TerritorialDamageSources.create(world, TerritorialDamageSources.OMNISCIENT_OBSIDIAN);
	}

    public static DamageSource create(World world, RegistryKey<DamageType> key) {
		return create(world, key, null, null);
    }

	public static DamageSource create(World world, RegistryKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker) {
		return world.getRegistryManager()
				.get(RegistryKeys.DAMAGE_TYPE)
				.getHolder(key)
				.map((type) -> new DamageSource(type, source, attacker))
				.orElse(world.getDamageSources().genericKill()); // Fallback, should never reach this
	}
}
