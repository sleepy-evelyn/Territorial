package io.github.lunathelemon.territorial.api.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IPeekingEyeComponent extends Component, AutoSyncedComponent, ClientTickingComponent {

    void startPeeking(@Nullable BoundBlockEntity blockEntityOwner);
    void stopPeeking();

    boolean isPeeking();
    int getTicksPeeking();

    PlayerEntity getProvider();
    @Nullable BoundBlockEntity getBoundBlockEntity();
    default Item getItemToDrop() { return Items.ENDER_EYE; }
}
