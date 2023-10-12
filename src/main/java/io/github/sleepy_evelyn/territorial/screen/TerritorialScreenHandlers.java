package io.github.sleepy_evelyn.territorial.screen;

import io.github.sleepy_evelyn.territorial.Territorial;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class TerritorialScreenHandlers {

    public static final ScreenHandlerType<ScreenHandler> CORRUPTED_BEACON_SCREEN_HANDLER_TYPE
		= ScreenHandlerRegistry.registerSimple(Territorial.id("boundary_beacon"), CorruptedBeaconScreenHandler::new);
}
