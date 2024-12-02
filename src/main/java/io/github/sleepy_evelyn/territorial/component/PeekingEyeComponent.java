package io.github.sleepy_evelyn.territorial.component;

import io.github.sleepy_evelyn.territorial.api.component.BoundBlockEntity;
import io.github.sleepy_evelyn.territorial.api.component.BoundBlockEntityParams;
import io.github.sleepy_evelyn.territorial.api.component.IPeekingEyeComponent;
import io.github.sleepy_evelyn.territorial.client.sound.PeekingSoundInstance;
import io.github.sleepy_evelyn.territorial.init.TerritorialDamageSources;
import io.github.sleepy_evelyn.territorial.util.NetworkingUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
    @Nullable private BlockPos startingPos;
    @Nullable private BoundBlockEntityParams bbeParams;
    @Nullable private PeekingSoundInstance soundInstance;
    private final PlayerEntity provider;

    public PeekingEyeComponent(PlayerEntity provider) {
        this.provider = provider;
        this.isPeeking = false;
        this.ticksPeeking = 0;

        if(provider.getWorld().isClient)
            ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> onClientDisconnect());
    }

    @Override
    public void startPeeking(@Nullable BoundBlockEntity bbe) {
        this.startingDimensionKey = provider.getWorld().getDimensionKey();
        this.startingPos = provider.getBlockPos();
        this.isPeeking = true;
        if(bbe != null) {
            bbeParams = bbe.getParams();
            bbe.addBoundEntity(provider);
        }
        TerritorialComponents.PEEKING_EYE.sync(provider);
    }

    @Override
    public void stopPeeking() {
        provider.dropItem(getItemToDrop());
        teleportToStartingPos();
        if(getBoundBlockEntity() instanceof BoundBlockEntity bbe)
            bbe.removeBoundEntity(provider);
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
                        var teleportTarget = new TeleportTarget(startingPos.ofCenter(), Vec3d.ZERO, provider.getYaw(), provider.getPitch());
                        FabricDimensions.teleport(provider, world, teleportTarget);
                        postTeleportActions();
                        break;
                    }
                }
            } else
                canTeleport = true;

            // Teleport the player back if the dimension exists
            if(canTeleport) {
                provider.requestTeleportAndDismount(startingPos.getX(), startingPos.getY(), startingPos.getZ());
                postTeleportActions();
            } else { // Make the player fall into the void if the stored dimension doesn't exist anymore lol
                var outOfWorldDamageType = TerritorialDamageSources.create(provider.getWorld(), DamageTypes.OUT_OF_WORLD);
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

    @Override
    public void serverTick() {
        if(isPeeking) {
            if(provider.getWorld().getDimensionKey() != null && bbeParams != null) {
                var playerDimensionId = provider.getWorld().getDimensionKey().getValue();
                var bbeDimensionId = bbeParams.dimensionKey().getValue();
                var distanceToBlockEntity = (float) Math.sqrt(provider.getBlockPos().getSquaredDistance(bbeParams.pos()));

                // Stop peeking if the player has switched dimension or is out of reach
                if (!bbeDimensionId.equals(playerDimensionId) || distanceToBlockEntity > bbeParams.reach()) {
                    var bbe = getBoundBlockEntity();
                    if(bbe != null) {
                        stopPeeking();
                        NetworkingUtils.markDirtyAndSync(bbe, provider.getWorld());
                    }
                }
            }
        }
    }

    private void reset(boolean isClient) {
        if(isClient) {
            ticksPeeking = 0;
            soundInstance = null;
        } else
            this.startingPos = null;
        this.startingDimensionKey = null;
        this.bbeParams = null;
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
            startingPos = NbtHelper.toBlockPos(nbt.getCompound("startingPos"));
        if(nbt.contains("boundBlockEntity")) {
            var bbeNbt = nbt.getCompound("boundBlockEntity");
            bbeParams = new BoundBlockEntityParams(
                    RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(nbt.getString("dimensionKey"))),
                            NbtHelper.toBlockPos(nbt.getCompound("pos")),
                            bbeNbt.getInt("reach")
            );
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("peeking", isPeeking);
        if(startingDimensionKey != null)
            nbt.putString("startingDimensionKey", startingDimensionKey.getValue().toString());
        if(startingPos != null)
            nbt.put("startingPos", NbtHelper.fromBlockPos(startingPos));
        if(bbeParams != null) {
            var bbeNbt = new NbtCompound();
            bbeNbt.putString("dimensionKey", bbeParams.dimensionKey().getValue().toString());
            bbeNbt.put("pos", NbtHelper.fromBlockPos(bbeParams.pos()));
            bbeNbt.putInt("reach", bbeParams.reach());
            nbt.put("boundBlockEntity", bbeNbt);
        }
    }

    @Override public boolean isPeeking() { return isPeeking; }
    @Override public int getTicksPeeking() { return ticksPeeking; }

    @Override
    public void rebind(BoundBlockEntity bbe) {
        bbeParams = bbe.getParams();
    }

    @Override
    public @Nullable BlockEntity getBoundBlockEntity() {
        var server = provider.getServer();
        if(server != null && startingDimensionKey != null && bbeParams != null) {
            for(var world : server.getWorlds()) {
                if(world.getDimensionKey().getValue().equals(startingDimensionKey.getValue())) {
                    var beInWorld = world.getBlockEntity(bbeParams.pos());
                    // Check the block entity exists within the world
                    if(beInWorld instanceof BoundBlockEntity)
                        return beInWorld;
                }
            }
        }
        return null;
    }
}
