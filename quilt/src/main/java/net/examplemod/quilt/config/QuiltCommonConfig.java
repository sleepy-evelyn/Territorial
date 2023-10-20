package net.examplemod.quilt.config;

import net.examplemod.config.CommonConfigConstants;
import net.examplemod.platform.TerritorialConfig;
import org.quiltmc.config.api.ReflectiveConfig;
import org.quiltmc.config.api.annotations.Comment;
import org.quiltmc.config.api.annotations.IntegerRange;
import org.quiltmc.config.api.values.TrackedValue;

import static net.examplemod.platform.TerritorialConfig.DEFAULTS;

public class QuiltCommonConfig extends ReflectiveConfig implements CommonConfigConstants, TerritorialConfig.ConfigAccess {

    @Comment(TWEAKS_DIVIDER)
    public final TweaksConfig tweaks = new TweaksConfig();

    public static class TweaksConfig extends Section {
        @Comment(OMNISCIENT_OBSIDIAN_RECIPE_COMMENT)
        public final TrackedValue<Boolean> omniscientObsidianRecipe = this.value(DEFAULTS.omniscientObsidianRecipe());

        @Comment(OMNISCIENT_OBSIDIAN_RECIPE_SPREAD_COMMENT)
        public final TrackedValue<Boolean> omniscientObsidianSpread = this.value(DEFAULTS.omniscientObsidianSpread());

        @Comment(LASER_TARGETS_ALL_MOBS_COMMENT)
        public final TrackedValue<Boolean> laserTargetsAllMobs = this.value(DEFAULTS.laserTargetsAllMobs());

        @Comment(LASER_TRANSMITTER_MAX_REACH_COMMENT)
        @IntegerRange(min = LTMR_MIN, max = LTMR_MAX)
        public final TrackedValue<Integer> laserTransmitterMaxReach = this.value(DEFAULTS.laserTransmitterMaxReach());

        @Comment(ECLIPSE_ROSE_MAX_REACH_COMMENT)
        @IntegerRange(min = ERMR_MIN, max = ERMR_MAX)
        public final TrackedValue<Integer> eclipseRoseMaxReach = this.value(DEFAULTS.eclipseRoseMaxReach());

        @Comment(PLINTH_OF_PEEKING_MIN_REACH_COMMENT)
        public final TrackedValue<Integer> plinthOfPeekingMinReach = this.value(DEFAULTS.plinthOfPeekingMinReach());
    }

    @Override public boolean omniscientObsidianRecipe() { return tweaks.omniscientObsidianRecipe.value(); }
    @Override public boolean omniscientObsidianSpread() { return tweaks.omniscientObsidianSpread.value(); }
    @Override public boolean laserTargetsAllMobs() { return tweaks.laserTargetsAllMobs.value(); }
    @Override public int laserTransmitterMaxReach() { return tweaks.laserTransmitterMaxReach.value(); }
    @Override public int eclipseRoseMaxReach() { return tweaks.eclipseRoseMaxReach.value(); }
    @Override public int plinthOfPeekingMinReach() { return tweaks.plinthOfPeekingMinReach.value(); }
}