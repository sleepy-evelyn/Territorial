package net.examplemod.platform;

import net.examplemod.ExampleMod;

public class TerritorialConfig {

    public static final ConfigAccess DEFAULTS = new ConfigAccess() {
        @Override public boolean omniscientObsidianRecipe() { return true; }
        @Override public boolean omniscientObsidianSpread() { return true; }
        @Override public boolean laserTargetsAllMobs() { return true; }
        @Override public int laserTransmitterMaxReach() { return 48; }
        @Override public int eclipseRoseMaxReach() { return 8; }
        @Override public int plinthOfPeekingMinReach() { return 20; }
    };

    public static final ClientConfigAccess CLIENT_DEFAULTS = new ClientConfigAccess() {};

    public interface ConfigAccess {
        boolean omniscientObsidianRecipe();
        boolean omniscientObsidianSpread();
        boolean laserTargetsAllMobs();
        int laserTransmitterMaxReach();
        int eclipseRoseMaxReach();
        int plinthOfPeekingMinReach();
    }

    public interface ClientConfigAccess {
        // Nothing here yet...
    }

    private static ConfigAccess config = DEFAULTS;
    private static ClientConfigAccess clientConfig = CLIENT_DEFAULTS;
    private static boolean configAssigned, clientConfigAssigned;

    public static ConfigAccess common() {
        if (!configAssigned) errorUsingDefaultValues("common");
        return config;
    }
    public static ClientConfigAccess client() {
        if (!clientConfigAssigned) errorUsingDefaultValues("client");
        return clientConfig;
    }

    public static void setCommon(ConfigAccess access) {
        if (configAssigned) warnTriedToReassign("common");
        else {
            config = access;
            configAssigned = true;
        }
    }

    public static void setClient(ClientConfigAccess access) {
        if (clientConfigAssigned) warnTriedToReassign("client");
        else {
            clientConfig = access;
            clientConfigAssigned = true;
        }
    }

    private static void errorUsingDefaultValues(String configType) {
        ExampleMod.LOGGER.error("Failed to set " + configType + " config values for: " + ExampleMod.MOD_ID
                + ". Using default values instead");
    }

    private static void warnTriedToReassign(String configType) {
        ExampleMod.LOGGER.warn("Tried to re-assign the " + configType + " config manager. Config manager already assigned");
    }
}
