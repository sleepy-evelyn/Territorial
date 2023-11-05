package io.github.sleepy_evelyn.territorial.config;

final class ConfigConstants {
    // Comments
    static final String SHOW_LOCK_NAME_COMMENT = "Shows the lock name in the GUI",
        OMNISCIENT_OBSIDIAN_RECIPE_COMMENT = "Whether omniscient obsidian can be crafted",
        OMNISCIENT_OBSIDIAN_SPREAD_COMMENT = "Allow spreading to adjacent Obsidian blocks",
        PLINTH_OF_PEEKING_MIN_REACH_COMMENT = "Minimum reach of a Plinth of peeking without a pyramid formation",
        LASER_TARGETS_ALL_MOBS_COMMENT = "Whether the laser targets all mobs or just players";

    // Bounds & Defaults
    static final int ERMR_MIN = 0, ERMR_MAX = 16, ERMR_DEFAULT = 8; // Eclipse Rose max reach
    static final int LTMR_MIN = 0, LTMR_MAX = 60, LTMR_DEFAULT = 48; // Laser Transmitter max reach
    static final int POPMR_MIN = 0, POPMR_MAX = 200, POPMR_DEFAULT = 20; // Plinth of peeking min reach
}
