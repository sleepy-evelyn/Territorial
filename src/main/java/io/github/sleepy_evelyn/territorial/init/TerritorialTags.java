package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.Territorial;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class TerritorialTags {

    public static final TagKey<Block> CORRUPTED_PYRAMID = TagKey.of(RegistryKeys.BLOCK, Territorial.id("corrupted_pyramid"));
}
