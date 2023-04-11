package io.github.lunathelemon.territorial;

import io.github.lunathelemon.territorial.client.render.entity.PeekingEyeRenderer;
import io.github.lunathelemon.territorial.event.client.HudRenderHandler;
import io.github.lunathelemon.territorial.init.client.ItemPredicateRegistry;
import io.github.lunathelemon.territorial.init.client.RenderRegistry;
import net.fabricmc.api.ClientModInitializer;

public class TerritorialClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderRegistry.register();
        ItemPredicateRegistry.register();
        HudRenderHandler.init();
        PeekingEyeRenderer.init();
    }
}
