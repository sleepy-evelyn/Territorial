package io.github.sleepy_evelyn.territorial.item;

import io.github.sleepy_evelyn.territorial.block.entity.LaserTransmitterBlockEntity;
import io.github.sleepy_evelyn.territorial.init.TerritorialItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LensItem extends Item {

    public LensItem() {
        super(new FabricItemSettings());
    }

    @Override
    public Text getName(ItemStack stack) {
        var translationText = Text.translatable(this.getTranslationKey());
        var beamNbt = stack.getSubNbt("beam");

        if (beamNbt != null) {
            var dyeColour = Optional.of(DyeColor.byId(beamNbt.getInt("colour"))).orElse(DyeColor.WHITE);

            // TODO - Fix this for other languages
            String dyeNameCapitalized = Stream.of(dyeColour.getName().split("_"))
                    .map(part -> part.replace("_", ""))
                    .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1))
                    .collect(Collectors.joining(" "));
            return Text.of(dyeNameCapitalized + " " + translationText.getString());
        }
        return translationText;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext ctx) {
        super.appendTooltip(stack, world, tooltip, ctx);
        var beamNbt = stack.getSubNbt("beam");

        if (beamNbt != null) {
            int strengthMod = beamNbt.getByte("strength");
            tooltip.add(Text.translatable("tooltip.territorial.lens_strength_" + strengthMod));
            if (beamNbt.getBoolean("light"))
				tooltip.add(Text.translatable("tooltip.territorial.modifier.light"));
            if (beamNbt.getBoolean("highlight"))
                tooltip.add(Text.translatable("tooltip.territorial.modifier.highlight"));
            if (beamNbt.getBoolean("death"))
				tooltip.add(Text.translatable("tooltip.territorial.modifier.death"));
            if (beamNbt.getBoolean("rainbow"))
				tooltip.add(Text.translatable("tooltip.territorial.modifier.rainbow"));
            if (beamNbt.getBoolean("sparkle"))
				tooltip.add(Text.translatable("tooltip.territorial.modifier.sparkle"));
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        var be = ctx.getWorld().getBlockEntity(ctx.getBlockPos());
        var world = ctx.getWorld();

        if (!world.isClient && be instanceof LaserTransmitterBlockEntity ltbe) {
            var pos = ctx.getBlockPos();
            var player = ctx.getPlayer();
            var lensStack = TerritorialItems.LENS.getDefaultStack();

            if(!Objects.equals(ltbe.writeNbtStack(lensStack).getSubNbt("beam"), ctx.getStack().getSubNbt("beam"))) {
                ItemEntity lensToDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ltbe.writeNbtStack(lensStack));
                ltbe.createFromLens(ctx.getStack());
                if(player != null && !player.isCreative()) {
                    ctx.getStack().decrement(1);
                    world.spawnEntity(lensToDrop);
                }
            }
        }
        return ActionResult.PASS;
    }
}
