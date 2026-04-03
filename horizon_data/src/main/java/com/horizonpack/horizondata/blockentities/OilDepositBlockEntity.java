package com.horizonpack.horizondata.blockentities;

import com.horizonpack.horizondata.core.DataRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class OilDepositBlockEntity extends BlockEntity {
    // Tracks both oil and gas remaining uses
    private int remainingUses = 16;

    public OilDepositBlockEntity(BlockPos pos, BlockState state) {
        super(DataRegistries.DEPOSIT_BE.get(), pos, state);
    }

    /**
     * Attempts to extract one use from the deposit.
     * @return true if successful, false if depleted.
     */
    public boolean extract() {
        if (this.remainingUses > 0) {
            this.remainingUses--;
            this.setChanged();
            return true;
        }
        return false;
    }

    public boolean isDepleted() {
        return this.remainingUses <= 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("RemainingUses", this.remainingUses);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.remainingUses = tag.getInt("RemainingUses");
    }
}