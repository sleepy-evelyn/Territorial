package io.github.lunathelemon.territorial.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.api.component.IPeekingEyeComponent;
import net.minecraft.util.Identifier;

public class TerritorialComponents implements EntityComponentInitializer {

    public static final ComponentKey<IPeekingEyeComponent> PEEKING_EYE
            = ComponentRegistry.getOrCreate(new Identifier(Territorial.MOD_ID, "peeking_eye"), IPeekingEyeComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PEEKING_EYE, PeekingEyeComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
