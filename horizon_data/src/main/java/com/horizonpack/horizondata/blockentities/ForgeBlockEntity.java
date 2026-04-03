package com.horizonpack.horizondata.blockentities;

import com.horizonpack.horizondata.core.DataRegistries;
import com.horizonpack.horizondata.data.FuelDefinition;
import com.horizonpack.horizondata.systems.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ForgeBlockEntity extends BlockEntity {
    private final SimpleContainer inventory = new SimpleContainer(3);
    private int progress = 0;
    private int burnTime = 0;
    private static final int MAX_PROGRESS = 200;

    public ForgeBlockEntity(BlockPos pos, BlockState state) {
        super(DataRegistries.FORGE_BE.get(), pos, state);
    }

    public SimpleContainer getInventory() { return inventory; }

    public static void tick(Level level, BlockPos pos, BlockState state, ForgeBlockEntity entity) {
        if (level.isClientSide) return;
        boolean isBurning = entity.burnTime > 0;
        boolean hasChanged = false;

        if (isBurning) {
            entity.burnTime--;
            hasChanged = true;
        }

        ItemStack input = entity.inventory.getItem(0);
        ItemStack fuelStack = entity.inventory.getItem(1);

        if (!input.isEmpty()) {
            if (!isBurning && !fuelStack.isEmpty()) {
                FuelDefinition fuel = FuelRegistry.getFuel(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(fuelStack.getItem()));
                if (fuel != null) {
                    entity.burnTime = fuel.getBurnTime();
                    fuelStack.shrink(1);
                    isBurning = true;
                    hasChanged = true;
                }
            }
            if (isBurning) {
                entity.progress++;
                if (entity.progress >= MAX_PROGRESS) {
                    input.shrink(1);
                    entity.progress = 0;
                    hasChanged = true;
                }
            }
        }
        if (hasChanged) entity.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Progress", progress);
        tag.putInt("BurnTime", burnTime);
        tag.put("Inventory", inventory.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        progress = tag.getInt("Progress");
        burnTime = tag.getInt("BurnTime");
        inventory.fromTag(tag.getList("Inventory", 10), registries);
    }
}