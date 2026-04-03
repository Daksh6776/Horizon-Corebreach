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

public class AlchemyLabMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerLevelAccess access;

    public AlchemyLabMenu(int containerId, Inventory playerInventory) {
        // 5 slots: 3 Inputs, 1 Fuel, 1 Output
        this(containerId, playerInventory, new SimpleContainer(5), ContainerLevelAccess.NULL);
    }

    public AlchemyLabMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(DataRegistries.ALCHEMY_LAB_MENU.get(), containerId);
        this.container = container;
        this.access = access;

        checkContainerSize(container, 5);
        container.startOpen(playerInventory.player);

        // Slots: 0,1,2 = Inputs | 3 = Fuel | 4 = Output
        this.addSlot(new Slot(container, 0, 44, 17));
        this.addSlot(new Slot(container, 1, 62, 17));
        this.addSlot(new Slot(container, 2, 80, 17));
        this.addSlot(new Slot(container, 3, 62, 53)); // Fuel slot below the middle input

        this.addSlot(new Slot(container, 4, 124, 35) {
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

            if (index == 4) { // Output slot
                if (!this.moveItemStackTo(itemstack1, 5, 41, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index > 4) { // Player inventory
                // Move to inputs (0, 1, 2) or fuel (3)
                if (!this.moveItemStackTo(itemstack1, 0, 4, false)) return ItemStack.EMPTY;
            } else { // Inputs or Fuel
                if (!this.moveItemStackTo(itemstack1, 5, 41, false)) return ItemStack.EMPTY;
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