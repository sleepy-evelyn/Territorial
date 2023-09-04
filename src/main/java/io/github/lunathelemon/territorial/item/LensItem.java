package io.github.lunathelemon.territorial.item;

import io.github.lunathelemon.territorial.block.entity.LaserTransmitterBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
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
        Text translationText = Text.translatable(this.getTranslationKey());
        NbtCompound tag = stack.getSubNbt("beam");

        if (tag != null) {
            var dyeColour = Optional.of(DyeColor.byId(tag.getInt("colour"))).orElse(DyeColor.WHITE);

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
        NbtCompound tag = stack.getSubNbt("beam");
        if (tag != null) {
            int strengthMod = tag.getByte("strength");
            tooltip.add(Text.translatable("tooltip.territorial.lens_strength_" + strengthMod));
            if (tag.getBoolean("light")) tooltip.add(Text.translatable("tooltip.territorial.modifier.light"));
            if (tag.getBoolean("highlight"))
                tooltip.add(Text.translatable("tooltip.territorial.modifier.highlight"));
            if (tag.getBoolean("death")) tooltip.add(Text.translatable("tooltip.territorial.modifier.death"));
            if (tag.getBoolean("rainbow")) tooltip.add(Text.translatable("tooltip.territorial.modifier.rainbow"));
            if (tag.getBoolean("sparkle")) tooltip.add(Text.translatable("tooltip.territorial.modifier.sparkle"));
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        var be = ctx.getWorld().getBlockEntity(ctx.getBlockPos());
        var world = ctx.getWorld();

        if (!world.isClient && be instanceof LaserTransmitterBlockEntity lbe) {
            var pos = ctx.getBlockPos();
            var player = ctx.getPlayer();
            var lensStack = TerritorialItems.LENS.getDefaultStack();

            if(!Objects.equals(lbe.writeNbtStack(lensStack).getSubNbt("beam"), ctx.getStack().getSubNbt("beam"))) {
                ItemEntity lensToDrop = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), lbe.writeNbtStack(lensStack));
                lbe.createFromLens(ctx.getStack());
                if(player != null && !player.isCreative()) {
                    ctx.getStack().decrement(1);
                    world.spawnEntity(lensToDrop);
                }
            }
        }
        return ActionResult.PASS;
    }
}