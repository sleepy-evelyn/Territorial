package io.github.lunathelemon.territorial.block.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.lunathelemon.territorial.api.sound.TerritorialSounds;
import io.github.lunathelemon.territorial.block.CorruptedBeaconBlock;
import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import io.github.lunathelemon.territorial.screen.CorruptedBeaconScreenHandler;
import io.github.lunathelemon.territorial.util.BeaconUtils;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static io.github.lunathelemon.territorial.api.sound.TerritorialSounds.CORRUPTED_BEACON_AMBIENT;
import static io.github.lunathelemon.territorial.api.sound.TerritorialSounds.CORRUPTED_BEACON_POWER_SELECT;

public class CorruptedBeaconBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {

    public static final StatusEffect[][] EFFECTS_BY_LEVEL = new StatusEffect[][]{
            {StatusEffects.NAUSEA, StatusEffects.HUNGER},
            {StatusEffects.SLOWNESS, StatusEffects.BLINDNESS},
            {StatusEffects.POISON},
            {StatusEffects.WITHER}};

    List<CorruptedBeaconBlockEntity.BeamSegment> beamSegments = Lists.newArrayList();
    private List<CorruptedBeaconBlockEntity.BeamSegment> beamSegmentsTemp = Lists.newArrayList();
    int level;
    private int minY;
    @Nullable
    StatusEffect primary, secondary;

    @Nullable
    private Text customName;
    private ContainerLock lock;
    protected final PropertyDelegate propertyDelegate;
    public CorruptedBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(TerritorialBlockEntities.CORRUPTED_BEACON_BLOCK_ENTITY_TYPE, pos, state);

        this.lock = ContainerLock.EMPTY;
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> CorruptedBeaconBlockEntity.this.level;
                    case 1 -> StatusEffect.getRawId(CorruptedBeaconBlockEntity.this.primary);
                    case 2 -> StatusEffect.getRawId(CorruptedBeaconBlockEntity.this.secondary);
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CorruptedBeaconBlockEntity.this.level = value;
                    case 1 -> {
                        if (!world.isClient && !CorruptedBeaconBlockEntity.this.beamSegments.isEmpty())
                            CORRUPTED_BEACON_POWER_SELECT.play(world, pos);
                        CorruptedBeaconBlockEntity.this.primary = CorruptedBeaconBlockEntity.getPotionEffectById(value);
                    }
                    case 2 -> CorruptedBeaconBlockEntity.this.secondary = CorruptedBeaconBlockEntity.getPotionEffectById(value);
                }
            }
            public int size() {
                return 3;
            }
        };
    }

    public static void tick(World world, BlockPos pos, BlockState state, CorruptedBeaconBlockEntity blockEntity) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        BlockPos blockPos;

        if (blockEntity.minY < y) {
            blockPos = pos;
            blockEntity.beamSegmentsTemp = Lists.newArrayList();
            blockEntity.minY = pos.getY() - 1;
        } else {
            blockPos = new BlockPos(x, blockEntity.minY + 1, z);
        }

        var beamSegment = blockEntity.beamSegmentsTemp.isEmpty() ? null : blockEntity.beamSegmentsTemp.get(blockEntity.beamSegmentsTemp.size() - 1);
        int topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);

        int i;
        for(i = 0; i < 10 && blockPos.getY() <= topY; ++i) {
            var blockState = world.getBlockState(blockPos);
            var block = blockState.getBlock();
            if (block instanceof Stainable) {
                var colours = ((Stainable)block).getColor().getColorComponents();
                if (blockEntity.beamSegmentsTemp.size() <= 1) {
                    beamSegment = new CorruptedBeaconBlockEntity.BeamSegment(colours);
                    blockEntity.beamSegmentsTemp.add(beamSegment);
                } else if (beamSegment != null) {
                    if (Arrays.equals(colours, beamSegment.color)) {
                        beamSegment.increaseHeight();
                    } else {
                        beamSegment = new CorruptedBeaconBlockEntity.BeamSegment(new float[]{(beamSegment.color[0] + colours[0]) / 2.0F, (beamSegment.color[1] + colours[1]) / 2.0F, (beamSegment.color[2] + colours[2]) / 2.0F});
                        blockEntity.beamSegmentsTemp.add(beamSegment);
                    }
                }
            } else {
                if (beamSegment == null || blockState.getOpacity(world, blockPos) >= 15 && !blockState.isOf(Blocks.BEDROCK)) {
                    blockEntity.beamSegmentsTemp.clear();
                    blockEntity.minY = topY;
                    break;
                }
                beamSegment.increaseHeight();
            }

            blockPos = blockPos.up();
            ++blockEntity.minY;
        }

        i = blockEntity.level;
        if (world.getTime() % 80L == 0L) {
            if (!blockEntity.beamSegments.isEmpty()) {
                blockEntity.level = BeaconUtils.updateLevel(world, pos, List.of(TerritorialBlocks.OMNISCIENT_OBSIDIAN));
            }
            if (blockEntity.level > 0 && !blockEntity.beamSegments.isEmpty()) {
                applyPlayerEffects(world, pos, blockEntity.level, blockEntity.primary, blockEntity.secondary);
                CORRUPTED_BEACON_AMBIENT.play(world, pos);
            }
        }

        if (blockEntity.minY >= topY) {
            blockEntity.minY = world.getBottomY() - 1;
            boolean bl = i > 0;
            blockEntity.beamSegments = blockEntity.beamSegmentsTemp;
            if (!world.isClient) {
                boolean bl2 = blockEntity.level > 0;
                if (!bl && bl2) {
                    TerritorialSounds.CORRUPTED_BEACON_ACTIVATED.play(world, pos);
                    world.setBlockState(pos, state.with(CorruptedBeaconBlock.CORRUPTED, true));

                    for (ServerPlayerEntity serverPlayerEntity : world.getNonSpectatingEntities(ServerPlayerEntity.class, (new Box((double) x, (double) y, (double) z, (double) x, (double) (y - 4), (double) z)).expand(10.0D, 5.0D, 10.0D))) {
                        Criteria.CONSTRUCT_BEACON.trigger(serverPlayerEntity, blockEntity.level);
                    }
                } else if (bl && !bl2) {
                    TerritorialSounds.CORRUPTED_BEACON_DEACTIVATED.play(world, pos);
                    world.setBlockState(pos, state.with(CorruptedBeaconBlock.CORRUPTED, false));
                }
            }
        }

    }

    public void markRemoved() {
        TerritorialSounds.CORRUPTED_BEACON_DEACTIVATED.play(world, pos);
        super.markRemoved();
    }

    protected static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel, @Nullable StatusEffect primaryEffect, @Nullable StatusEffect secondaryEffect) {
        if (!world.isClient && primaryEffect != null) {
            double d = (beaconLevel * 10 + 10);
            int i = 0;
            if (beaconLevel >= 4 && primaryEffect == secondaryEffect) {
                i = 1;
            }

            int j = (9 + beaconLevel * 2) * 20;
            Box box = (new Box(pos)).expand(d).stretch(0.0D, world.getHeight(), 0.0D);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
            Iterator<PlayerEntity> var11 = list.iterator();

            PlayerEntity playerEntity;
            while(var11.hasNext()) {
                playerEntity = var11.next();
                playerEntity.addStatusEffect(new StatusEffectInstance(primaryEffect, j, i, true, true));
            }

            if (beaconLevel >= 4 && primaryEffect != secondaryEffect && secondaryEffect != null) {
                var11 = list.iterator();

                while(var11.hasNext()) {
                    playerEntity = var11.next();
                    playerEntity.addStatusEffect(new StatusEffectInstance(secondaryEffect, j, 0, true, true));
                }
            }

        }
    }

    public static void onCorruptedBlock(World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if(blockEntity instanceof BeaconBlockEntity) {
            world.setBlockState(pos, TerritorialBlocks.CORRUPTED_BEACON.getDefaultState().with(CorruptedBeaconBlock.CORRUPTED, true));
            TerritorialSounds.BEACON_CORRUPTED.play(world, pos);
        }
    }

    public List getBeamSegments() {
        return this.level == 0 ? ImmutableList.of() : this.beamSegments;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Nullable
    static StatusEffect getPotionEffectById(int id) {
        return StatusEffect.byRawId(id);
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.primary = getPotionEffectById(nbt.getInt("Primary"));
        this.secondary = getPotionEffectById(nbt.getInt("Secondary"));
        if (nbt.contains("CustomName", 8)) {
            this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }

        this.lock = ContainerLock.fromNbt(nbt);
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("Primary", StatusEffect.getRawIdNullable(this.primary));
        nbt.putInt("Secondary", StatusEffect.getRawIdNullable(this.secondary));
        nbt.putInt("Levels", this.level);
        if (this.customName != null) {
            nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
        }

        this.lock.writeNbt(nbt);
    }

    public void setCustomName(@Nullable Text customName) {
        this.customName = customName;
    }

    public Text getDisplayName() {
        return (this.customName != null ? this.customName : Text.translatable("container.beacon"));
    }

    public void setWorld(World world) {
        super.setWorld(world);
        this.minY = world.getBottomY() - 1;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CorruptedBeaconScreenHandler(syncId, playerInventory, propertyDelegate, ScreenHandlerContext.create(this.world, this.getPos()));
    }

    public static class BeamSegment {
        final float[] color;
        private int height;

        public BeamSegment(float[] color) {
            this.color = color;
            this.height = 1;
        }

        protected void increaseHeight() {
            ++this.height;
        }

        public float[] getColor() {
            return this.color;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
