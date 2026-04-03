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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import com.horizonpack.horizondata.network.WorkshopProgressPacket;

public class KitchenBlockEntity extends BlockEntity {
    // Slots 0,1,2,3: Food Inputs | Slot 4: Fuel | Slot 5: Output
    private final SimpleContainer inventory = new SimpleContainer(6);
    private int progress = 0;
    private int burnTime = 0;
    private int maxBurnTime = 0;
    private static final int MAX_PROGRESS = 300;

    public KitchenBlockEntity(BlockPos pos, BlockState state) {
        super(DataRegistries.KITCHEN_BE.get(), pos, state);
    }

    public SimpleContainer getInventory() { return inventory; }

    public static void tick(Level level, BlockPos pos, BlockState state, KitchenBlockEntity entity) {
        if (level.isClientSide) return;
        boolean isBurning = entity.burnTime > 0;
        boolean hasChanged = false;

        if (isBurning) {
            entity.burnTime--;
            hasChanged = true;
        }

        boolean hasIngredients = false;
        for(int i = 0; i < 4; i++) {
            if (!entity.inventory.getItem(i).isEmpty()) hasIngredients = true;
        }

        ItemStack fuelStack = entity.inventory.getItem(4);

        if (hasIngredients) {
            if (!isBurning && !fuelStack.isEmpty()) {
                FuelDefinition fuel = FuelRegistry.getFuel(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(fuelStack.getItem()));
                if (fuel != null) {
                    entity.burnTime = fuel.getBurnTime();
                    entity.maxBurnTime = fuel.getBurnTime();
                    fuelStack.shrink(1);
                    isBurning = true;
                    hasChanged = true;
                }
            }

            if (isBurning) {
                entity.progress++;
                if (entity.progress >= MAX_PROGRESS) {
                    // TODO: Recipe execution
                    for(int i = 0; i < 4; i++) {
                        if (!entity.inventory.getItem(i).isEmpty()) entity.inventory.getItem(i).shrink(1);
                    }
                    entity.progress = 0;
                    hasChanged = true;
                }
            } else if (entity.progress > 0) {
                entity.progress = Math.max(0, entity.progress - 2);
                hasChanged = true;
            }
        } else if (entity.progress > 0) {
            entity.progress = 0;
            hasChanged = true;
        }

        if (hasChanged) entity.setChanged();

        if (hasChanged) {
            entity.setChanged(); // Tells Minecraft to save the chunk

            // --- PASTE THIS HERE ---
            // Sends the progress packet to the client
            PacketDistributor.sendToPlayersTrackingChunk(
                    (ServerLevel) level,
                    new ChunkPos(pos),
                    new WorkshopProgressPacket(pos, entity.progress, MAX_PROGRESS)
            );
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Progress", this.progress);
        tag.putInt("BurnTime", this.burnTime);
        tag.putInt("MaxBurnTime", this.maxBurnTime);
        tag.put("Inventory", this.inventory.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.progress = tag.getInt("Progress");
        this.burnTime = tag.getInt("BurnTime");
        this.maxBurnTime = tag.getInt("MaxBurnTime");
        if (tag.contains("Inventory")) this.inventory.fromTag(tag.getList("Inventory", 10), registries);
    }
}