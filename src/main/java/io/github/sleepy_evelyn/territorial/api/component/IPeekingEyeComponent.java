package io.github.sleepy_evelyn.territorial.api.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.github.sleepy_evelyn.territorial.api.component.BoundBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;


public interface IPeekingEyeComponent extends Component, AutoSyncedComponent, ClientTickingComponent, ServerTickingComponent {

    void startPeeking(@Nullable BoundBlockEntity bbe);
    void stopPeeking();

    boolean isPeeking();
    int getTicksPeeking();

    void rebind(BoundBlockEntity bbe);
    @Nullable BlockEntity getBoundBlockEntity();
    default Item getItemToDrop() { return Items.ENDER_EYE; }
}
