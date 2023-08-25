package io.github.lunathelemon.territorial.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public abstract class BaseScreenHandler extends ScreenHandler {
    int inventorySize;

    protected BaseScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, int inventorySize) {
        super(type, syncId);
        this.inventorySize = inventorySize;
    }

    abstract void createScreen(PlayerInventory player, ItemStack itemStack);

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        var itemStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot.hasStack()) {
            var stackToInsert = slot.getStack();
            itemStack = stackToInsert.copy();
            if (index < inventorySize) {
                if (!this.insertItem(stackToInsert, inventorySize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(stackToInsert, 0, inventorySize, false)) {
                return ItemStack.EMPTY;
            }

            if (stackToInsert.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemStack;
    }

    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
