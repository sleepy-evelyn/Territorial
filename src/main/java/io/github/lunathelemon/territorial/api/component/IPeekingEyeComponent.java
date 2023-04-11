package io.github.lunathelemon.territorial.api.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IPeekingEyeComponent extends Component, AutoSyncedComponent, ClientTickingComponent {
    void setPeeking(boolean peeking);
    boolean isPeeking();
    int getTicksPeeking();
    @Nullable BlockPos getStartingPos();
}
