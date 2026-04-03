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

public class WorkbenchMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerLevelAccess access;

    public WorkbenchMenu(int containerId, Inventory playerInventory) {
        // 10 slots: 9 for the 3x3 grid, 1 for Output
        this(containerId, playerInventory, new SimpleContainer(10), ContainerLevelAccess.NULL);
    }

    public WorkbenchMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(DataRegistries.WORKBENCH_MENU.get(), containerId);
        this.container = container;
        this.access = access;

        checkContainerSize(container, 10);
        container.startOpen(playerInventory.player);

        // Slots 0-8: 3x3 Crafting Grid
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(container, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        // Slot 9: Output
        this.addSlot(new Slot(container, 9, 124, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
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

            if (index == 9) { // Output slot
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 9) { // Player inventory
                if (!this.moveItemStackTo(itemstack1, 0, 9, false)) return ItemStack.EMPTY; // Move to Grid
            } else { // Grid slots
                if (!this.moveItemStackTo(itemstack1, 10, 46, false)) return ItemStack.EMPTY;
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