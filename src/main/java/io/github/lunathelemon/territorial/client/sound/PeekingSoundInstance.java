package io.github.lunathelemon.territorial.client.sound;

import io.github.lunathelemon.territorial.api.component.BoundBlockEntity;
import io.github.lunathelemon.territorial.component.PeekingEyeComponent;
import io.github.lunathelemon.territorial.component.TerritorialComponents;
import io.github.lunathelemon.territorial.util.TickCounter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class PeekingSoundInstance extends MovingSoundInstance {

    private BoundBlockEntity bbe;
    private final ClientPlayerEntity player;

    public PeekingSoundInstance(ClientPlayerEntity player, BoundBlockEntity bbe) {
        super(SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.AMBIENT, SoundInstance.createRandom());
        this.bbe = bbe;
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public boolean canPlay() {
        return player.getComponent(TerritorialComponents.PEEKING_EYE).isPeeking();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    public void rebind(BoundBlockEntity bbe) {
        this.bbe = bbe;
    }

    @Override
    public void tick() {
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.volume = 0;

        float screamBorder = (bbe.getReach() < 100) ? 5 : 10;
        float distanceToBlockEntity = PeekingEyeComponent.getDistanceToBoundBE(player, bbe);

        if(bbe.getReach() - screamBorder < distanceToBlockEntity)
            this.volume = (distanceToBlockEntity - (bbe.getReach() - screamBorder)) / screamBorder;
    }
}
