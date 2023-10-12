package io.github.sleepy_evelyn.territorial.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.api.component.IPeekingEyeComponent;


public class TerritorialComponents implements EntityComponentInitializer {

    public static final ComponentKey<IPeekingEyeComponent> PEEKING_EYE
            = ComponentRegistry.getOrCreate(Territorial.id("peeking_eye"), IPeekingEyeComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PEEKING_EYE, PeekingEyeComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }
}

