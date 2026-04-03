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

public class GrindstoneMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerLevelAccess access;

    public GrindstoneMenu(int containerId, Inventory playerInventory) {
        // 2 slots: 1 Input, 1 Output
        this(containerId, playerInventory, new SimpleContainer(2), ContainerLevelAccess.NULL);
    }

    public GrindstoneMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(DataRegistries.GRINDSTONE_MENU.get(), containerId);
        this.container = container;
        this.access = access;

        checkContainerSize(container, 2);
        container.startOpen(playerInventory.player);

        // Slots: 0 = Input, 1 = Output
        this.addSlot(new Slot(container, 0, 56, 35));
        this.addSlot(new Slot(container, 1, 116, 35) {
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

            if (index == 1) { // Output slot
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 1) { // Player inventory
                // Move to input (0)
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) return ItemStack.EMPTY;
            } else { // Input slot
                if (!this.moveItemStackTo(itemstack1, 2, 38, false)) return ItemStack.EMPTY;
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