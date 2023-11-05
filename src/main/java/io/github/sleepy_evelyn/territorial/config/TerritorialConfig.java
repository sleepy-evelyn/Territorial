package io.github.sleepy_evelyn.territorial.config;


public final class TerritorialConfig {

    private static ConfigAccess config = null;
    private static ClientConfigAccess clientConfig = null;

    public interface ConfigAccess {
        boolean omniscientObsidianRecipe();
        boolean omniscientObsidianSpread();
        boolean laserTargetsAllMobs();
        int getPlinthOfPeekingMinReach();
        int getEclipseRoseMaxReach();
        int getLaserTransmitterMaxReach();
    }

    public interface ClientConfigAccess {
        // Nothing here yet...
    }

    public static void setCommon(ConfigAccess config) {
        TerritorialConfig.config = config;
    }

    public static void setClient(ClientConfigAccess clientConfig) {
        TerritorialConfig.clientConfig = clientConfig;
    }

    public static ConfigAccess common() {
        return config;
    }

    public static ClientConfigAccess client() {
        return clientConfig;
    }
}
