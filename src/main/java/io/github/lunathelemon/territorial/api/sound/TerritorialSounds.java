package io.github.lunathelemon.territorial.api.sound;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public final class TerritorialSounds {

    private static final Random random = Random.create();
    public static final BlendedSound BEACON_CORRUPTED = new BlendedSound(
            new Sound(SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 0.8f, 0.1f),
            new Sound(SoundEvents.BLOCK_SCULK_SENSOR_STEP, SoundCategory.BLOCKS, 0.6f, 0.3f)
    );

    public static final BlendedSound CORRUPTED_BEACON_AMBIENT = new BlendedSound(
            new Sound(SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.BLOCKS, 0.6f, 0.1f),
            new Sound(SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundCategory.AMBIENT, 0.3f, 0.3f, 6)
    );

    public static final Sound CORRUPTED_BEACON_ACTIVATED = new Sound(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1f, 0.4f);
    public static final Sound CORRUPTED_BEACON_DEACTIVATED = new Sound(SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1f, 0.2f);
    public static final Sound CORRUPTED_BEACON_POWER_SELECT = new Sound(SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1f, 0.2f);

    public record Sound(SoundEvent soundEvent, SoundCategory category, float volume, float pitch, int rollChance) {
        Sound(SoundEvent soundEvent, SoundCategory category, float volume, float pitch) {
            this(soundEvent, category, volume, pitch, 1);
        }

        public void play(World world, BlockPos pos) {
            play(world, pos, 1);
        }

        public void play(World world, BlockPos pos, int rollChance) {
            if(random.nextInt(rollChance) == 0)
                world.playSound(null, pos, soundEvent, category, volume, pitch);
        }
    }

    public record BlendedSound(Sound... blendedSound) {
        public void play(World world, BlockPos pos) {
            for(var sound : blendedSound)
                sound.play(world, pos);
        }
    }
    private TerritorialSounds() {}
}
