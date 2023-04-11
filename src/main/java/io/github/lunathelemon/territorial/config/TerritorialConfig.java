package io.github.lunathelemon.territorial.config;

import io.github.lunathelemon.territorial.Territorial;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config.Gui.Background("minecraft:textures/block/bedrock.png")
@Config(name = Territorial.MOD_ID + "/" + Territorial.MOD_ID)
public class TerritorialConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    private static boolean loaded = false;

    @Comment("Whether omniscient obsidian can be crafted")
    boolean omniscientObsidianRecipe = true;

    @Comment("Allow spreading to adjacent Obsidian blocks")
    boolean omniscientObsidianSpread = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 16)
    int eclipseRoseMaxReach = 8;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    int laserTransmitterMaxReach = 48;

    @Comment("Whether the laser targets all mobs or just players")
    boolean laserTargetsAllMobs = false;

    private double getWithinBounds(String name, double configValue, double defaultValue, double min, double max) {
        if(configValue < min || configValue > max) {
            if(!loaded || Territorial.IS_DEBUG_MODE)
                Territorial.LOGGER.warn("Incorrect value for " + name + ": " + configValue + " set in the config file, choosing default value: " + defaultValue);
            return defaultValue;
        }
        return configValue;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();
        getLaserTransmitterMaxReach();
        getEclipseRoseMaxReach();
        loaded = true;
    }

    public boolean omniscientObsidianRecipe() { return omniscientObsidianRecipe; }
    public boolean omniscientObsidianSpread() { return omniscientObsidianSpread; }
    public boolean laserTargetsAllMobs() { return laserTargetsAllMobs; }
    public int getEclipseRoseMaxReach() { return (int) getWithinBounds("eclipseRoseMaxReach", eclipseRoseMaxReach, 8, 1, 16); }
    public int getLaserTransmitterMaxReach() { return (int) getWithinBounds("laserTransmitterMaxReach", laserTransmitterMaxReach, 48, 1, 60); }
}
