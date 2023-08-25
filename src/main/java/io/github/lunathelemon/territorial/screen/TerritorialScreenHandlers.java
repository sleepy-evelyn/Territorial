package io.github.lunathelemon.territorial.screen;

import io.github.lunathelemon.territorial.Territorial;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class TerritorialScreenHandlers {

    public static final ScreenHandlerType<CorruptedBeaconScreenHandler> CORRUPTED_BEACON_SCREEN_HANDLER_TYPE
            = ScreenHandlerRegistry.registerSimple(new Identifier(Territorial.MOD_ID, "boundary_beacon"), CorruptedBeaconScreenHandler::new);
}
