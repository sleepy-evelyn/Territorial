package io.github.lunathelemon.territorial;

import io.github.lunathelemon.territorial.api.TerritorialAPI;
import io.github.lunathelemon.territorial.api.event.CorruptionEvents;
import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import io.github.lunathelemon.territorial.block.entity.CorruptedBeaconBlockEntity;
import io.github.lunathelemon.territorial.block.entity.TerritorialBlockEntities;
import io.github.lunathelemon.territorial.config.TerritorialConfig;
import io.github.lunathelemon.territorial.entity.effect.TerritorialStatusEffects;
import io.github.lunathelemon.territorial.init.C2SPacketRegistry;
import io.github.lunathelemon.territorial.init.ItemGroupRegistry;
import io.github.lunathelemon.territorial.item.TerritorialItems;
import io.github.lunathelemon.territorial.recipe.TerritorialRecipes;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

public class Territorial implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(TerritorialAPI.MOD_ID);
	public static final boolean IS_DEBUG_MODE = isDebugMode();

	@Override
	public void onInitialize() {
		AutoConfig.register(TerritorialConfig.class, JanksonConfigSerializer::new);
		TerritorialItems.registerAll();
		TerritorialBlocks.registerAll();
		TerritorialBlockEntities.registerAll();
		TerritorialRecipes.registerAll();
		TerritorialStatusEffects.registerAll();
		ItemGroupRegistry.register();

		// Events
		CorruptionEvents.BLOCK_CORRUPTED.register(CorruptedBeaconBlockEntity::onCorruptedBlock);

		// Packets
		C2SPacketRegistry.register();
	}

	public static Identifier getID(String path) {
		return new Identifier(TerritorialAPI.MOD_ID, path);
	}

	private static boolean isDebugMode() {
		boolean debugMode = false;
		for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
			if (arg.contains("jdwp")) {
				debugMode = true;
				break;
			}
		}
		return debugMode;
	}

	public static TerritorialConfig getConfig() {
		return AutoConfig.getConfigHolder(TerritorialConfig.class).getConfig();
	}
}
