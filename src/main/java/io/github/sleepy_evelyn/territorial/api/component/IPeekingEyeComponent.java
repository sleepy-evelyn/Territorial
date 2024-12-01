package io.github.sleepy_evelyn.territorial.api.component;

import io.github.sleepy_evelyn.territorial.api.component.BoundBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;

/**
public interface IPeekingEyeComponent extends Component, AutoSyncedComponent, ClientTickingComponent, ServerTickingComponent {

    void startPeeking(@Nullable BoundBlockEntity bbe);
    void stopPeeking();

    boolean isPeeking();
    int getTicksPeeking();

    void rebind(BoundBlockEntity bbe);
    @Nullable BlockEntity getBoundBlockEntity();
    default Item getItemToDrop() { return Items.ENDER_EYE; }
}
**/