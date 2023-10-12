package io.github.sleepy_evelyn.territorial.api;

import net.minecraft.util.Identifier;

public class TerritorialAPI {

    public static final String MOD_ID = "territorial";

	public static Identifier id(String path) {
		return new Identifier(TerritorialAPI.MOD_ID, path);
	}
}
