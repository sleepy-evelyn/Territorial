package io.github.lunathelemon.territorial.entity.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class EclipseStatusEffect extends StatusEffect {

    public EclipseStatusEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x010316);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        var world = entity.getEntityWorld();
        if(world.isClient && entity instanceof PlayerEntity)
            eclipsePhaseTick((ClientWorld) world);
    }

    @Environment(EnvType.CLIENT)
    private void eclipsePhaseTick(ClientWorld clientWorld) {
        long timeOfDay = clientWorld.getTimeOfDay();

        // Gradually move the sun or moon across the sky
        if(timeOfDay < 17950)
            clientWorld.setTimeOfDay(timeOfDay + 80);
        else if(timeOfDay > 18050)
            clientWorld.setTimeOfDay(timeOfDay - 80);
        else
            clientWorld.setTimeOfDay(18000);
    }
}
