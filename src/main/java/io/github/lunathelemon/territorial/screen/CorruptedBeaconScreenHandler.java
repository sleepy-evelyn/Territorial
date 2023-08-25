package io.github.lunathelemon.territorial.screen;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CorruptedBeaconScreenHandler extends ScreenHandler {
    private final Inventory payment;
    private final PaymentSlot paymentSlot;
    private final ScreenHandlerContext context;
    private final PropertyDelegate propertyDelegate;

    public CorruptedBeaconScreenHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, new ArrayPropertyDelegate(3), ScreenHandlerContext.EMPTY);
    }

    public CorruptedBeaconScreenHandler(int syncId, Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        super(TerritorialScreenHandlers.CORRUPTED_BEACON_SCREEN_HANDLER_TYPE, syncId);
        this.payment = new SimpleInventory(1) {
            public boolean isValid(int slot, ItemStack stack) {
                return stack.isIn(ItemTags.BEACON_PAYMENT_ITEMS);
            }
            public int getMaxCountPerStack() {
                return 1;
            }
        };
        checkDataCount(propertyDelegate, 3);
        this.propertyDelegate = propertyDelegate;
        this.context = context;
        this.paymentSlot = new PaymentSlot(this.payment, 0, 136, 110);
        this.addSlot(this.paymentSlot);
        this.addProperties(propertyDelegate);

        enableSyncing();

        int k;
        for(k = 0; k < 3; ++k) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + k * 9 + 9, 36 + l * 18, 137 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 36 + k * 18, 195));
        }
    }

    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (!player.world.isClient) {
            ItemStack itemStack = this.paymentSlot.takeStack(this.paymentSlot.getMaxItemCount());
            if (!itemStack.isEmpty()) {
                player.dropItem(itemStack, false);
            }

        }
    }

    public void setProperty(int id, int value) {
        super.setProperty(id, value);
        this.sendContentUpdates();
    }

    @Override
    public boolean canUse(PlayerEntity player) { return true; }

    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 0) {
                if (!this.insertItem(itemStack2, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (!this.paymentSlot.hasStack() && this.paymentSlot.canInsert(itemStack2) && itemStack2.getCount() == 1) {
                if (!this.insertItem(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 1 && index < 28) {
                if (!this.insertItem(itemStack2, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                if (!this.insertItem(itemStack2, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 1, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    public int getProperties() {
        return this.propertyDelegate.get(0);
    }

    @Nullable
    public StatusEffect getPrimaryEffect() {
        return StatusEffect.byRawId(this.propertyDelegate.get(1));
    }

    @Nullable
    public StatusEffect getSecondaryEffect() {
        return StatusEffect.byRawId(this.propertyDelegate.get(2));
    }

    public void setEffects(Optional<StatusEffect> primary, Optional<StatusEffect> secondary) {
        if (this.paymentSlot.hasStack()) {
            this.propertyDelegate.set(1, primary.map(StatusEffect::getRawId).orElse(-1));
            this.propertyDelegate.set(2, secondary.map(StatusEffect::getRawId).orElse(-1));
            this.paymentSlot.takeStack(1);
            this.context.run(World::markDirty);
        }
    }

    public boolean hasPayment() {
        return !this.payment.getStack(0).isEmpty();
    }

    private class PaymentSlot extends Slot {
        public PaymentSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        public boolean canInsert(ItemStack stack) {
            return stack.isIn(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        public int getMaxItemCount() {
            return 1;
        }
    }
}

