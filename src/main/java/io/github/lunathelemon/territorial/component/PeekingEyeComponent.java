package io.github.lunathelemon.territorial.component;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.api.component.BoundBlockEntity;
import io.github.lunathelemon.territorial.api.component.IPeekingEyeComponent;
import io.github.lunathelemon.territorial.client.sound.PeekingSoundInstance;
import io.github.lunathelemon.territorial.init.TerritorialDamageTypes;
import io.github.lunathelemon.territorial.util.NbtUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public class PeekingEyeComponent implements IPeekingEyeComponent {

    private boolean isPeeking;
    private int ticksPeeking;
    @Nullable private RegistryKey<DimensionType> startingDimensionKey;
    @Nullable private BlockPos startingPos, boundPos;

    @Nullable private PeekingSoundInstance soundInstance;
    private final PlayerEntity provider;

    public PeekingEyeComponent(PlayerEntity provider) {
        this.provider = provider;
        this.isPeeking = false;
        this.ticksPeeking = 0;

        if(provider.world.isClient)
            ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> onClientDisconnect());
    }

    @Override
    public void startPeeking(@Nullable BoundBlockEntity boundBlockEntity) {
        this.startingDimensionKey = provider.getWorld().getDimensionKey();
        this.startingPos = provider.getBlockPos();
        this.isPeeking = true;
        if(boundBlockEntity != null)
            boundPos = boundBlockEntity.getPos();
        TerritorialComponents.PEEKING_EYE.sync(provider);
    }

    @Override
    public void stopPeeking() {
        provider.dropItem(getItemToDrop());
        teleportToStartingPos();
        reset(false);
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
                        var teleportTarget = new TeleportTarget(startingPos.toCenterPos(), Vec3d.ZERO, provider.getYaw(), provider.getPitch());

                        FabricDimensions.teleport((ServerPlayerEntity) provider, world, teleportTarget);
                        postTeleportActions();
                        break;
                    }
                }
            } else
                canTeleport = true;

            // Teleport the player back if the dimension exists
            if(canTeleport) {
                provider.requestTeleport(startingPos.getX(), startingPos.getY(), startingPos.getZ());
                postTeleportActions();
            } else { // Make the player fall into the void if the stored dimension doesn't exist anymore lol
                var outOfWorldDamageType = TerritorialDamageTypes.create(provider.getWorld(), DamageTypes.OUT_OF_WORLD);
                provider.damage(outOfWorldDamageType, Integer.MAX_VALUE);
            }
        }
    }

    private void postTeleportActions() {
        provider.getWorld().sendEntityStatus(provider, (byte) 46);
        provider.getWorld().playSoundFromEntity(null, provider, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
    }

    @Override
    public void clientTick() {
        if(isPeeking) {
            ticksPeeking += 1;
        } else if(ticksPeeking > 0)
            reset(true);
    }

    private void reset(boolean isClient) {
        if(isClient) {
            ticksPeeking = 0;
            soundInstance = null;
        } else
            this.startingPos = null;
        this.startingDimensionKey = null;
        this.boundPos = null;
        this.isPeeking = false;
    }

    private void onClientDisconnect() {
        ticksPeeking = 0;
        soundInstance = null;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        if(nbt.contains("peeking"))
            isPeeking = nbt.getBoolean("peeking");
        if(nbt.contains("startingDimensionKey"))
            startingDimensionKey = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(nbt.getString("startingDimensionKey")));
        if(nbt.contains("startingPos"))
            startingPos = new BlockPos(NbtUtils.deserializeVec3i(nbt.getIntArray("startingPos")));
        if(nbt.contains("boundPos"))
            boundPos = new BlockPos(NbtUtils.deserializeVec3i(nbt.getIntArray("boundPos")));
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("peeking", isPeeking);
        if(startingDimensionKey != null)
            nbt.putString("startingDimensionKey", startingDimensionKey.getValue().toString());
        if(startingPos != null)
            nbt.putIntArray("startingPos", NbtUtils.serializeVec3i(startingPos));
        if(boundPos != null)
            nbt.putIntArray("boundPos", NbtUtils.serializeVec3i(boundPos));
    }

    @Override public boolean shouldSyncWith(ServerPlayerEntity player) { return player == this.provider; }
    @Override public boolean isPeeking() { return isPeeking; }
    @Override public int getTicksPeeking() { return ticksPeeking; }

    @Override
    public PlayerEntity getProvider() {
        return provider;
    }

    @Override
    public @Nullable BoundBlockEntity getBoundBlockEntity() {
        var server = provider.getServer();
        if(server != null && startingDimensionKey != null && boundPos != null) {
            for(var world : server.getWorlds()) {
                if(world.getDimensionKey().getValue().equals(startingDimensionKey.getValue())) {
                    var beInWorld = world.getBlockEntity(boundPos);
                    // Check the block entity exists within the world
                    if(beInWorld instanceof BoundBlockEntity bbe)
                        return bbe;
                }
            }
            // A block entity no longer exists here so reset everything, something broke, or the world was removed.
            reset(false);
        }
        return null;
    }
}
