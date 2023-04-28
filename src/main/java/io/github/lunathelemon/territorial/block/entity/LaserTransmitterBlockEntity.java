package io.github.lunathelemon.territorial.block.entity;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import io.github.lunathelemon.territorial.init.TerritorialDamageTypes;
import io.github.lunathelemon.territorial.mixin.common.AnvilChunkStorageAccessor;
import io.github.lunathelemon.territorial.util.MathUtils;
import io.github.lunathelemon.territorial.util.NetworkingUtils;
import io.github.lunathelemon.territorial.util.TickCounter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.minecraft.util.math.Direction.UP;

public class LaserTransmitterBlockEntity extends BlockEntity {

    public static final float[] SIGNAL_STRENGTH_WIDTHS = { 0.001f, 0.0015f, 0.0030f, 0.0045f, 0.0070f, 0.01f, 0.0135f, 0.02f, 0.025f, 0.035f, 0.06f, 0.1f, 0.16f, 0.25f, 0.38f };
    private static final int maxEntitiesPerBeamTick = 30;
    private static final int lightBlockSpacing = 4;
    private final TickCounter lightBlocksTicker;

    private int strength, colour, maxReach, prevPower;
    private float sparkleDistance, reach, prevReach;
    private boolean updateLights;
    private Vec3d startPos, endPos;
    private Map<String, Boolean> mods = new HashMap<>();
    private Entity trackedEntity;
    private BlockPos receiverPos;

    public LaserTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(TerritorialBlockEntities.LASER_BLOCK_ENTITY_TYPE, pos, state);
        prevPower = -1;
        maxReach = Territorial.getConfig().getLaserTransmitterMaxReach();
        lightBlocksTicker = new TickCounter(6);
        mods.put("sparkle", false);
        mods.put("rainbow", false);
        mods.put("highlight", false);
        mods.put("light", false);
        mods.put("death", false);
    }

    public void createFromLens(ItemStack stack) {
        NbtCompound tag = stack.getSubNbt("beam");
        if(tag != null) {
            setStrength(tag.getByte("strength"));
            setColour(tag.getInt("colour"));
            assignMods(Map.of(
                    "rainbow", tag.getBoolean("rainbow"),
                    "sparkle", tag.getBoolean("sparkle"),
                    "death", tag.getBoolean("death"),
                    "highlight", tag.getBoolean("highlight"),
                    "light", tag.getBoolean("light")
            ));
            assert world != null;
            NetworkingUtils.markDirtyAndSync(this, world);

            boolean powered = false;
            if(tag.getBoolean("light")) powered = getCachedState().get(Properties.POWERED);
            updateLightBlocks(powered, getCachedState().get(Properties.FACING));
        }
    }

    public ItemStack writeNbtStack(ItemStack stack) {
        stack.setSubNbt("beam", createNbt());

        // Remove block identifying features
        NbtCompound beamSubTag = stack.getSubNbt("beam");
        if(beamSubTag != null) {
            // TODO - Switch to NbtUtils.removeBlockFeatures() soon
            beamSubTag.remove("id");
            beamSubTag.remove("x");
            beamSubTag.remove("y");
            beamSubTag.remove("z");

            beamSubTag.remove("max_reach");
        }
        return stack;
    }

    public static void tick(World world, BlockPos pos, BlockState state, LaserTransmitterBlockEntity be) {
        final int power = state.get(Properties.POWER);
        final Direction facing = state.get(Properties.FACING);

        if(power <= 0) {
            if(be.receiverPos != null) be.setReceiverPowered(false);
            if(be.mods.get("light") && be.prevPower != 0) be.updateLightBlocks(false, facing);
            be.prevPower = power;
            return;
        }
        else if(be.reach != be.prevReach || power != be.prevPower) {
            if(power != be.prevPower) {
                be.startPos = Vec3d.ofCenter(pos)
                        .add(MathUtils.Pos.zeroMove(Vec3d.of(facing.getVector()).multiply(0.5), SIGNAL_STRENGTH_WIDTHS[power - 1] / 2));
            }
            be.endPos = Vec3d.ofCenter(pos)
                    .add(MathUtils.Pos.zeroMove(Vec3d.of(facing.getVector()).multiply(be.reach + 0.5), -(SIGNAL_STRENGTH_WIDTHS[power - 1] / 2)));
            be.updateLights = true;
            be.prevPower = power;
            be.prevReach = be.reach;
        }
        be.lightBlocksTicker.increment();

        List<Entity> entities = new ArrayList<>();
        if(be.startPos != null && be.endPos != null) {
            entities = world.getOtherEntities(null, new Box(be.startPos, be.endPos));
            applyEffects(entities, state.get(Properties.FACING), be);
        }
        beamTick(world, pos, state, be, entities);
    }

    private static void beamTick(World world, BlockPos pos, BlockState state, LaserTransmitterBlockEntity be, List<Entity> entitiesCollided) {
        final Direction facing = state.get(Properties.FACING);
        BlockPos posIterator;
        BlockState bs;
        boolean foundReceiver = false;

        if (entitiesCollided.size() > 0) { // Collides with an entity
            int entityCounter = 0;
            float trackedReach = be.reach;

            for (Entity entity : entitiesCollided) {
                float distance = MathUtils.Pos.getDistanceAlongAxis(Vec3d.of(be.getPos()).add(0.5, 0.5, 0.5),
                        entity.getPos(), facing.getAxis());

                if (distance <= (be.maxReach - 1f)) {
                    if (be.trackedEntity != null && distance < be.maxReach) {
                        if (entity.equals(be.trackedEntity)) trackedReach = distance;
                    }
                    if (distance <= trackedReach) {
                        be.trackedEntity = entity;
                        be.reach = distance;
                        trackedReach = distance;
                        be.setReceiverPowered(false);
                    }
                }
                entityCounter++;
                if (entityCounter >= maxEntitiesPerBeamTick) break;
            }
        } else { // Check for block collisions
            for (int i = 0; i < be.maxReach; i++) {
                posIterator = pos.offset(facing, i);
                bs = world.getBlockState(posIterator);

                // Check for a receiver
                if (bs.getBlock() == TerritorialBlocks.LASER_RECEIVER) {
                    if (facing.equals(bs.get(Properties.FACING).getOpposite())) {
                        be.receiverPos = posIterator;
                        be.setReceiverPowered(true);
                    }
                    foundReceiver = true;
                }
                // Extend the lasers reach up to the first Opaque block or a receiver
                if ((bs.getOpacity(world, posIterator) >= 15 && !bs.isOf(Blocks.BEDROCK)) || (i == (be.maxReach - 1)) || foundReceiver) {
                    if (!foundReceiver) be.setReceiverPowered(false);
                    be.reach = i;
                    break;
                }
            }
        }
        if (!world.isClient) {
            // Check for adding/removing light blocks
            if (be.mods.get("light") && be.updateLights) {
                be.updateLightBlocks(true, facing);
            }
            int watchDistance = ((AnvilChunkStorageAccessor) ((ServerWorld) world).getChunkManager().threadedAnvilChunkStorage).getWatchDistance();
            int watchDistanceMaxReach = (watchDistance < 2) ? 16 : (watchDistance * 16) - 16;

            if (be.maxReach != watchDistanceMaxReach) {
                be.maxReach = Math.min(watchDistanceMaxReach, Territorial.getConfig().getLaserTransmitterMaxReach());
                be.markDirty();
            }
        }
    }

    private static void applyEffects(List<Entity> entities, Direction facing, LaserTransmitterBlockEntity be) {
        Item armorItem;
        int numArmorPieces;
        boolean hasGoldHelmet;
        boolean targetAllMobs = Territorial.getConfig().laserTargetsAllMobs();

        for(var entity : entities) {
            if(targetAllMobs || entity.isPlayer()) {
                numArmorPieces = 0;
                hasGoldHelmet = false;

                // Gold (reflective) armor resistance
                for(var armorStack : entity.getArmorItems()) {
                    armorItem = armorStack.getItem();
                    if(entity instanceof HorseEntity && armorItem == Items.GOLDEN_HORSE_ARMOR
                            || (facing == UP && armorItem == Items.GOLDEN_BOOTS)
                            || (facing == Direction.DOWN && armorItem == Items.GOLDEN_HELMET)) return;
                    else {
                        if(entity.getBlockY() == be.getPos().getY())
                            if(armorItem == Items.GOLDEN_LEGGINGS || armorItem == Items.GOLDEN_BOOTS) numArmorPieces++;
                        else
                            if(armorItem == Items.GOLDEN_CHESTPLATE || armorItem == Items.GOLDEN_HELMET) numArmorPieces++;
                    }
                    if(numArmorPieces == 2) return;
                    if(armorItem == Items.GOLDEN_HELMET) hasGoldHelmet = true;
                }
                // Blindness effect
                if(entity.isPlayer() && be.strength > 0)
                    if(!hasGoldHelmet)
                        ((PlayerEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200 * be.strength));

                // Fire effect
                if(be.strength > 1)
                    entity.setOnFireFor(6);

                // Highlight (Glow) effect
                if(be.mods.get("highlight"))
                    if(entity.isLiving())
                        ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 810));

                // Insta death effect
                if(be.mods.get("death") && be.world != null) {}
                    entity.damage(TerritorialDamageTypes.create(be.world, TerritorialDamageTypes.LASER), 4.0F);
            }
        }
    }

    public void setReceiverPowered(boolean powered) {
        if(receiverPos != null && world != null) {
            BlockState receiverState = world.getBlockState(receiverPos);
            if(receiverState.getBlock().equals(TerritorialBlocks.LASER_RECEIVER)) {
                int power = (powered) ? getCachedState().get(Properties.POWER) : 0;
                world.setBlockState(receiverPos, receiverState
                        .with(Properties.POWER, power)
                        .with(Properties.POWERED, powered));
            }
            if(!powered) receiverPos = null;
        }
    }

    public void updateLightBlocks(boolean powered, Direction facing) {
        if(lightBlocksTicker.test() || !powered) {
            updateLights = false;

            if(world != null && !world.isClient) {
                BlockState currentState;
                BlockPos posIterator = pos.offset(facing, 1);

                for(int i = 0; i <= (maxReach/ lightBlockSpacing); i++) {
                    currentState = world.getBlockState(posIterator);

                    if(powered) {
                        if(i <= (int) reach/ lightBlockSpacing && currentState.isAir()) {
                            world.setBlockState(posIterator, Blocks.LIGHT.getDefaultState());
                        }
                        else if(i > (int) reach/ lightBlockSpacing && currentState.equals(Blocks.LIGHT.getDefaultState())) {
                            world.setBlockState(posIterator, Blocks.AIR.getDefaultState(), (1 << 1) | (1 << 4));
                        }
                    }
                    else if(currentState.equals(Blocks.LIGHT.getDefaultState())){
                        world.setBlockState(posIterator, Blocks.AIR.getDefaultState(), (1 << 1) | (1 << 4));
                    }
                    posIterator = posIterator.offset(facing, lightBlockSpacing);
                }
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("colour", colour);
        tag.putInt("max_reach", maxReach);
        tag.putBoolean("rainbow", mods.get("rainbow"));
        tag.putBoolean("sparkle", mods.get("sparkle"));
        tag.putBoolean("light", mods.get("light"));
        tag.putByte("strength", (byte) strength);
        tag.putBoolean("highlight", mods.get("highlight"));
        tag.putBoolean("death", mods.get("death"));
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        colour = tag.getInt("colour");
        maxReach = tag.getInt("max_reach");
        mods.put("rainbow", tag.getBoolean("rainbow"));
        mods.put("sparkle", tag.getBoolean("sparkle"));
        mods.put("light", tag.getBoolean("light"));
        strength = tag.getByte("strength");
        mods.put("highlight", tag.getBoolean("highlight"));
        mods.put("death", tag.getBoolean("death"));
    }

    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = new NbtCompound();
        writeNbt(tag);

        // Prevent hidden nbt data being sent to the client
        if(tag.contains("strength")) tag.remove("strength");
        if(tag.contains("highlight")) tag.remove("highlight");
        if(tag.contains("death")) tag.remove("death");
        return tag;
    }

    public void setStrength(int strength) { this.strength = strength; }
    public void setColour(int colour) { this.colour = colour; }
    public void assignMods(Map<String, Boolean> mods) { this.mods = mods; }
    public void incrementSparkleDistance() { sparkleDistance += 0.3f; }
    public void resetSparkleDistance() { sparkleDistance = 0; }

    public float getReach() { return reach; }
    public float getPrevReach() { return prevReach; }
    public int getMaxReach() { return maxReach; }
    public float getSparkleDistance() { return sparkleDistance; }
    public DyeColor getColour() { return DyeColor.byId(colour); }
    public Map<String, Boolean> getMods() { return mods; }
}
