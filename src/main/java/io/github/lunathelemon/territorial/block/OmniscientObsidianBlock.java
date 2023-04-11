package io.github.lunathelemon.territorial.block;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.component.TerritorialComponents;
import io.github.lunathelemon.territorial.init.TerritorialDamageTypes;
import io.github.lunathelemon.territorial.util.MovementUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class OmniscientObsidianBlock extends CryingObsidianBlock implements BeforeBreakCancellable {

    private static final int SPREAD_ATTEMPTS = 3;

    private static final Predicate<Iterable<ItemStack>> pumpkinHelmetPredicate = armorItems -> {
        for(var armorItemStack : armorItems)
            if(armorItemStack.getItem().equals(Items.CARVED_PUMPKIN))
                return true;
        return false;
    };

    public OmniscientObsidianBlock() {
        super(FabricBlockSettings.of(Material.STONE, MapColor.BLACK)
                .requiresTool()
                .strength(50.0F, 1200.0F)
                .luminance((state) -> 10)
                .ticksRandomly()
        );
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double x = (pos.getX() + 0.5D) + (random.nextDouble() - 0.5D);
        double y = (pos.getY() + 0.5D) + (random.nextDouble() - 0.5D);
        double z = (pos.getZ() + 0.5D) + (random.nextDouble() - 0.5D);

        double vx = random.nextDouble() - 0.5D;
        double vy = random.nextDouble() - 0.5D;
        double vz = random.nextDouble() - 0.5D;
        world.addParticle(ParticleTypes.PORTAL, x, y, z, vx, vy, vz);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(Territorial.getConfig().omniscientObsidianSpread() && !world.isClient && random.nextDouble() < 0.0280D)
            tickSpread(state, world, pos, random);
    }

    private void tickSpread(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos spreadPos;
        for(int i=0; i < SPREAD_ATTEMPTS; i++) {
            spreadPos = pos.add(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
            if(world.getBlockState(spreadPos).getBlock() == Blocks.OBSIDIAN) {
                world.setBlockState(spreadPos, state, 3);
                break;
            }
        }
    }
    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if(!world.isClient) {
            if(isPlayerVisible(player)) {
                var random = player.getRandom();
                double randomPercent = random.nextDouble();

                var damageSource = TerritorialDamageTypes.create(world, TerritorialDamageTypes.OMNISCIENT_OBSIDIAN);

                player.damage(damageSource, random.nextInt(14 - 8) + 8);
                if(randomPercent < 0.5)
                    knockBackPlayer(player, random);
                else
                    MovementUtils.randomTeleport((ServerWorld) world, (ServerPlayerEntity) player, 0, 6, true, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
            }
        }
        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 0.5F, 0.2F, true);
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public boolean beforeBreak(ServerWorld world, BlockPos pos, BlockState state, ServerPlayerEntity player) {
        if(isPlayerVisible(player)) {
            knockBackPlayer(player, Random.create());
            return false;
        }
        return true;
    }

    private void knockBackPlayer(PlayerEntity player, Random random) {
        var unitVec = player.getHorizontalFacing().getUnitVector();
        var randomPitch = random.nextFloat() / 3;

        player.playSound(SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.BLOCKS, 0.1F, randomPitch);
        player.takeKnockback(1, unitVec.x * ((random.nextDouble() / 2) + 0.5), unitVec.z * ((random.nextDouble() / 2) + 0.5));
    }

    private static boolean isPlayerVisible(PlayerEntity player) {
        return !(player.isInvisible() || pumpkinHelmetPredicate.test(player.getArmorItems()));
    }
}
