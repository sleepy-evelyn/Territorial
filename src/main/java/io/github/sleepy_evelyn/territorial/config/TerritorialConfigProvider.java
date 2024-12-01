package io.github.sleepy_evelyn.territorial.config;

import io.github.sleepy_evelyn.territorial.api.TerritorialAPI;
/**
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import static io.github.sleepy_evelyn.territorial.config.ConfigConstants.*;
import io.github.sleepy_evelyn.territorial.config.TerritorialConfig.*;

@Config.Gui.Background("territorial:textures/misc/config_background.png")
@Config(name = TerritorialAPI.MOD_ID + "/" + TerritorialAPI.MOD_ID)
public class TerritorialConfigProvider implements ConfigData, ConfigAccess, ClientConfigAccess {

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    ClientModule client = new ClientModule();

    @ConfigEntry.Category("features")
    @ConfigEntry.Gui.TransitiveObject
    FeaturesModule features = new FeaturesModule();

    @ConfigEntry.Category("tweaks")
    @ConfigEntry.Gui.TransitiveObject
    TweaksModule tweaks = new TweaksModule();

    @Config(name = "client")
    static class ClientModule implements ConfigData {
        @Comment(SHOW_LOCK_NAME_COMMENT)
        boolean showLockName = true;
    }

    @Config(name = "features")
    static class FeaturesModule implements ConfigData {
        @Comment(OMNISCIENT_OBSIDIAN_RECIPE_COMMENT)
        boolean omniscientObsidianRecipe = true;

        @Comment(OMNISCIENT_OBSIDIAN_SPREAD_COMMENT)
        boolean omniscientObsidianSpread = true;
    }

    @Config(name = "tweaks")
    static class TweaksModule implements ConfigData {
        @ConfigEntry.BoundedDiscrete(min = ERMR_MIN, max = ERMR_MAX)
        int eclipseRoseMaxReach = ERMR_DEFAULT;

        @ConfigEntry.BoundedDiscrete(min = LTMR_MIN, max = LTMR_MAX)
        int laserTransmitterMaxReach = LTMR_DEFAULT;

        @Comment(PLINTH_OF_PEEKING_MIN_REACH_COMMENT)
        @ConfigEntry.BoundedDiscrete(min = POPMR_MIN, max = POPMR_MAX)
        int plinthOfPeekingMinReach = POPMR_DEFAULT;

        @Comment(LASER_TARGETS_ALL_MOBS_COMMENT)
        boolean laserTargetsAllMobs = false;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();
        TerritorialConfig.setCommon(this);
        TerritorialConfig.setClient(this);
    }

    private double constrainValue(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }

    @Override
    public boolean omniscientObsidianRecipe() {
        return features.omniscientObsidianRecipe;
    }

    @Override
    public boolean omniscientObsidianSpread() {
        return features.omniscientObsidianSpread;
    }

    @Override
    public boolean laserTargetsAllMobs() {
        return tweaks.laserTargetsAllMobs;
    }

    @Override
    public int getPlinthOfPeekingMinReach() {
        return (int) constrainValue(tweaks.plinthOfPeekingMinReach, POPMR_MIN, POPMR_MAX);
    }

    @Override
    public int getEclipseRoseMaxReach() {
        return (int) constrainValue(tweaks.eclipseRoseMaxReach, ERMR_MIN, ERMR_MAX);
    }

    @Override
    public int getLaserTransmitterMaxReach() {
        return (int) constrainValue(tweaks.laserTransmitterMaxReach, LTMR_MIN, LTMR_MAX);
    }
}
 **/
