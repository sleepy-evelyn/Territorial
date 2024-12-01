package io.github.sleepy_evelyn.territorial;

import io.github.sleepy_evelyn.territorial.api.TerritorialAPI;
import io.github.sleepy_evelyn.territorial.api.event.CorruptionEvents;
import io.github.sleepy_evelyn.territorial.config.TerritorialConfig;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlocks;
import io.github.sleepy_evelyn.territorial.block.entity.CorruptedBeaconBlockEntity;
import io.github.sleepy_evelyn.territorial.init.TerritorialBlockEntities;
import io.github.sleepy_evelyn.territorial.init.TerritorialStatusEffects;
import io.github.sleepy_evelyn.territorial.init.C2SPacketRegistry;
import io.github.sleepy_evelyn.territorial.init.TerritorialItems;
import io.github.sleepy_evelyn.territorial.init.TerritorialRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Territorial implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(TerritorialAPI.MOD_ID);
	public static final boolean IS_DEV_ENV = FabricLoader.getInstance().isDevelopmentEnvironment();

	@Override
	public void onInitialize() {
		TerritorialItems.initialize();
		TerritorialBlocks.initialize();
		TerritorialBlockEntities.initialize();
		TerritorialRecipes.initialize();
		TerritorialStatusEffects.initialize();

		CorruptionEvents.BLOCK_CORRUPTED.register(CorruptedBeaconBlockEntity::onCorruptedBlock); // Events
		C2SPacketRegistry.initialize(); // Packets
	}

	public static Identifier id(String path) {
		return new Identifier(TerritorialAPI.MOD_ID, path);
	}
}
