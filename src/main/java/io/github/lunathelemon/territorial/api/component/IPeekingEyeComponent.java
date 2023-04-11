package io.github.lunathelemon.territorial.api.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface IPeekingEyeComponent extends Component, AutoSyncedComponent, CommonTickingComponent {

    void setPeeking(boolean peeking);

    boolean isPeeking();

    int getTicksPeeking();

    @Nullable BlockPos getStartingPos();

    default Item getItemToDrop() { return Items.ENDER_EYE; }
}
