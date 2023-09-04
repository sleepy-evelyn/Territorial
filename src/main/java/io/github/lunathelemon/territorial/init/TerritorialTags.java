package io.github.lunathelemon.territorial.init;

import io.github.lunathelemon.territorial.Territorial;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class TerritorialTags {

    public static class Blocks {
        public static final TagKey<Block> CORRUPTED_PYRAMID = TagKey.of(RegistryKeys.BLOCK, Territorial.getID("corrupted_pyramid"));
    }

    public static class Items {}
}
