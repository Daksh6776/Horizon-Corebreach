package com.horizonpack.horizondata.inventory;

import com.horizonpack.horizondata.core.DataRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TanneryMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerLevelAccess access;

    public TanneryMenu(int containerId, Inventory playerInventory) {
        // 3 slots: Hide Input, Tannin Input, Output
        this(containerId, playerInventory, new SimpleContainer(3), ContainerLevelAccess.NULL);
    }

    public TanneryMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(DataRegistries.TANNERY_MENU.get(), containerId);
        this.container = container;
        this.access = access;

        checkContainerSize(container, 3);
        container.startOpen(playerInventory.player);

        // Slots: 0 = Hide, 1 = Tannin, 2 = Output
        this.addSlot(new Slot(container, 0, 44, 35));
        this.addSlot(new Slot(container, 1, 68, 35));
        this.addSlot(new Slot(container, 2, 124, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // Output slot
            }
        });

        // Player Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8, 84 + i * 18));
            }
        }
        // Player Hotbar
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 2) { // Output slot
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 2) { // Player inventory
                // Move to inputs (0, 1)
                if (!this.moveItemStackTo(itemstack1, 0, 2, false)) return ItemStack.EMPTY;
            } else { // Input slots
                if (!this.moveItemStackTo(itemstack1, 3, 39, false)) return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemstack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}