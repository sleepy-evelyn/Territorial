package io.github.lunathelemon.territorial.component;

import io.github.lunathelemon.territorial.api.component.IPeekingEyeComponent;
import io.github.lunathelemon.territorial.util.NbtUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PeekingEyeComponent implements IPeekingEyeComponent {

    private boolean isPeeking = false;
    private int ticksPeeking = 0;
    private BlockPos startingPos;

    private final PlayerEntity provider;

    public PeekingEyeComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        isPeeking = nbt.getBoolean("peeking");
        if(startingPos != null)
            startingPos = (BlockPos) NbtUtils.deserializeVec3i(nbt.getIntArray("startingPos"));
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("peeking", isPeeking);
        if(nbt.contains("startingPos"))
            nbt.putIntArray("startingPos", NbtUtils.serializeVec3i(startingPos));
    }

    @Override public void setPeeking(boolean peeking) {
        if(peeking)
            this.startingPos = provider.getBlockPos();
        else {
            teleportToStartingPos();
            this.startingPos = null;
        }
        this.isPeeking = peeking;
        TerritorialComponents.PEEKING_EYE.sync(provider);
    }

    private void teleportToStartingPos() {
        if(startingPos != null) {
            provider.requestTeleport(startingPos.getX(), startingPos.getY(), startingPos.getZ());
            provider.getWorld().sendEntityStatus(provider, (byte) 46);
        }
    }

    @Override @Nullable
    public BlockPos getStartingPos() {
        return startingPos;
    }

    @Override public boolean isPeeking() { return isPeeking; }
    @Override public int getTicksPeeking() { return ticksPeeking; }
    @Override public void clientTick() {
        ticksPeeking = (isPeeking) ? ticksPeeking + 1 : 0;
    }
}
