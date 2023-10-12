package io.github.sleepy_evelyn.territorial;

import io.github.sleepy_evelyn.territorial.client.render.entity.PeekingEyeRenderer;
import io.github.sleepy_evelyn.territorial.event.client.HudRenderHandler;
import io.github.sleepy_evelyn.territorial.init.client.ItemPredicateRegistry;
import io.github.sleepy_evelyn.territorial.init.client.RenderRegistry;
import net.fabricmc.api.ClientModInitializer;

public class TerritorialClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RenderRegistry.register();
        ItemPredicateRegistry.register();
        HudRenderHandler.initialize();

        // Events
        PeekingEyeRenderer.registerEvents();
    }
}
