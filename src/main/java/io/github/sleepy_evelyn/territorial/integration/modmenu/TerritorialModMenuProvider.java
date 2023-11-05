package io.github.sleepy_evelyn.territorial.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.sleepy_evelyn.territorial.config.TerritorialConfigProvider;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class TerritorialModMenuProvider implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> (Screen) AutoConfig.getConfigScreen(TerritorialConfigProvider.class, parent).get();
    }
}
