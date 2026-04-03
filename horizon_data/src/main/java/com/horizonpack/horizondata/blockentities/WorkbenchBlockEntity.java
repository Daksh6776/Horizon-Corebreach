package com.horizonpack.horizondata.blockentities;

import com.horizonpack.horizondata.core.DataRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WorkbenchBlockEntity extends BlockEntity {
    // Slots 0-8: 3x3 Grid | Slot 9: Output
    private final SimpleContainer inventory = new SimpleContainer(10);

    public WorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(DataRegistries.WORKBENCH_BE.get(), pos, state);

        // Add a listener to handle vanilla instant crafting logic on inventory change
        this.inventory.addListener(container -> setChanged());
    }

    public SimpleContainer getInventory() { return inventory; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", this.inventory.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) this.inventory.fromTag(tag.getList("Inventory", 10), registries);
    }
}