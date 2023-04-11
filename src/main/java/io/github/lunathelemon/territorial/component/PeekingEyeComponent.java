package io.github.lunathelemon.territorial.component;

import io.github.lunathelemon.territorial.api.component.IPeekingEyeComponent;
import io.github.lunathelemon.territorial.init.TerritorialDamageTypes;
import io.github.lunathelemon.territorial.util.NbtUtils;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public class PeekingEyeComponent implements IPeekingEyeComponent {

    private boolean isPeeking = false;
    private int ticksPeeking = 0;

    @Nullable private RegistryKey<DimensionType> startingDimensionKey;
    @Nullable private BlockPos startingPos;

    private final PlayerEntity provider;

    public PeekingEyeComponent(PlayerEntity provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        if(nbt.contains("peeking"))
            isPeeking = nbt.getBoolean("peeking");
        if(nbt.contains("startingPos"))
            startingPos = (BlockPos) NbtUtils.deserializeVec3i(nbt.getIntArray("startingPos"));
        if(nbt.contains("startingDimensionKey"))
            startingDimensionKey = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(nbt.getString("startingDimensionKey")));
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("peeking", isPeeking);
        if(startingPos != null)
            nbt.putIntArray("startingPos", NbtUtils.serializeVec3i(startingPos));
        if(startingDimensionKey != null)
            nbt.putString("startingDimensionKey", startingDimensionKey.getValue().toString());
    }

    @Override public void setPeeking(boolean peeking) {
        if(peeking) {
            this.startingDimensionKey = provider.getWorld().getDimensionKey();
            this.startingPos = provider.getBlockPos();
        } else {
            provider.dropItem(getItemToDrop());
            teleportToStartingPos();
            this.startingPos = null;
        }
        this.isPeeking = peeking;
        TerritorialComponents.PEEKING_EYE.sync(provider);
    }

    private void teleportToStartingPos() {
        if(startingPos != null && startingDimensionKey != null) {
            var dimensionID = provider.getWorld().getDimensionKey().getValue();
            boolean canTeleport = false;

            // Check if the player has switched dimensions
            if(provider.getServer() != null && !dimensionID.equals(startingDimensionKey.getValue())) {
                for(var world : provider.getServer().getWorlds()) {
                    // Switch to the dimension if it still exists and teleport back
                    if(world.getDimensionKey().getValue().equals(startingDimensionKey.getValue())) {
                        provider.moveToWorld(world);
                        canTeleport = true;
                        break;
                    }
                }
            } else canTeleport = true;

            // Teleport the player back if the dimension exists
            if(canTeleport) {
                provider.requestTeleport(startingPos.getX(), startingPos.getY(), startingPos.getZ());
                provider.getWorld().sendEntityStatus(provider, (byte) 46);
                provider.getWorld().playSoundFromEntity(null, provider, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
            } else { // Make the player fall into the void if the stored dimension doesn't exist anymore lol
                var outOfWorldDamageType = TerritorialDamageTypes.create(provider.getWorld(), DamageTypes.OUT_OF_WORLD);
                provider.damage(outOfWorldDamageType, Integer.MAX_VALUE);
            }
        }
    }

    @Override
    public void tick() {
        boolean isClient = provider.getWorld().isClient;
        if(isClient) {
            ticksPeeking = (isPeeking) ? ticksPeeking + 1 : 0;
        }
    }

    @Override @Nullable public BlockPos getStartingPos() { return startingPos; }
    @Override public boolean isPeeking() { return isPeeking; }
    @Override public int getTicksPeeking() { return ticksPeeking; }
}
