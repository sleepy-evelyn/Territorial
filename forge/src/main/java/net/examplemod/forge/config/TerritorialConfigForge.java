package net.examplemod.forge.config;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.examplemod.ExampleMod;
import net.examplemod.config.ClientConfigConstants;
import net.examplemod.config.CommonConfigConstants;
import net.examplemod.platform.TerritorialConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static net.examplemod.platform.TerritorialConfig.DEFAULTS;

@Mod.EventBusSubscriber(modid = ExampleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TerritorialConfigForge {

    private static final Common COMMON;
    public static final Client CLIENT;
    private static final ForgeConfigSpec COMMON_SPEC, CLIENT_SPEC;

    private static class Common implements TerritorialConfig.ConfigAccess, CommonConfigConstants {

        public final ForgeConfigSpec.BooleanValue omniscientObsidianRecipe;
        public final ForgeConfigSpec.BooleanValue omniscientObsidianSpread;
        public final ForgeConfigSpec.BooleanValue laserTargetsAllMobs;
        public final ForgeConfigSpec.IntValue laserTransmitterMaxReach;
        public final ForgeConfigSpec.IntValue eclipseRoseMaxReach;
        public final ForgeConfigSpec.IntValue plinthOfPeekingMinReach;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push(TWEAKS_DIVIDER);
            omniscientObsidianRecipe = builder
                    .comment(OMNISCIENT_OBSIDIAN_RECIPE_COMMENT)
                    .define("omniscientObsidianRecipe", DEFAULTS.omniscientObsidianRecipe());
             omniscientObsidianSpread = builder
                     .comment(OMNISCIENT_OBSIDIAN_RECIPE_SPREAD_COMMENT)
                     .define("omniscientObsidianSpread", DEFAULTS.omniscientObsidianSpread());
             laserTargetsAllMobs = builder
                     .comment(LASER_TARGETS_ALL_MOBS_COMMENT)
                     .define("laserTargetsAllMobs", DEFAULTS.laserTargetsAllMobs());
             laserTransmitterMaxReach = builder
                     .comment(LASER_TRANSMITTER_MAX_REACH_COMMENT)
                     .defineInRange("laserTransmitterMaxReach", DEFAULTS.laserTransmitterMaxReach(),
                             LTMR_MIN, LTMR_MAX);
             eclipseRoseMaxReach = builder
                     .comment(ECLIPSE_ROSE_MAX_REACH_COMMENT)
                     .defineInRange("eclipseRoseMaxReach", DEFAULTS.eclipseRoseMaxReach(),
                             ERMR_MIN, ERMR_MAX);
             plinthOfPeekingMinReach = builder
                     .comment(PLINTH_OF_PEEKING_MIN_REACH_COMMENT)
                     .defineInRange("plinthOfPeekingMinReach", DEFAULTS.plinthOfPeekingMinReach(),
                             0, Integer.MAX_VALUE);
             builder.pop();
        }

        @Override public boolean omniscientObsidianRecipe() { return omniscientObsidianRecipe.get(); }
        @Override public boolean omniscientObsidianSpread() { return omniscientObsidianSpread.get(); }
        @Override public boolean laserTargetsAllMobs() { return laserTargetsAllMobs.get(); }
        @Override public int laserTransmitterMaxReach() { return laserTransmitterMaxReach.get(); }
        @Override public int eclipseRoseMaxReach() { return eclipseRoseMaxReach.get(); }
        @Override public int plinthOfPeekingMinReach() { return plinthOfPeekingMinReach.get(); }
    }

    private static class Client implements TerritorialConfig.ClientConfigAccess, ClientConfigConstants {
        public Client(ForgeConfigSpec.Builder builder) {}
    }

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

        final Pair<Client, ForgeConfigSpec> specPairClient = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPairClient.getRight();
        CLIENT = specPairClient.getLeft();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
        TerritorialConfig.setCommon(COMMON);

        if (Platform.getEnvironment() == Env.CLIENT) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
            TerritorialConfig.setClient(CLIENT);
        }
    }
}
