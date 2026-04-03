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


public class BloomeryBlockEntity extends BlockEntity {
    // Slot 0: Input (Raw Ore), Slot 1: Fuel, Slot 2: Output (Ingot)
    private final SimpleContainer inventory = new SimpleContainer(3);

    private int progress = 0;
    private int burnTime = 0;
    private int maxBurnTime = 0;
    private static final int MAX_PROGRESS = 400; // Slower stone-age tier processing

    public BloomeryBlockEntity(BlockPos pos, BlockState state) {
        super(DataRegistries.BLOOMERY_BE.get(), pos, state);
    }

    public SimpleContainer getInventory() { return inventory; }

    public static void tick(Level level, BlockPos pos, BlockState state, BloomeryBlockEntity entity) {
        if (level.isClientSide) return;

        boolean isBurning = entity.burnTime > 0;
        boolean hasChanged = false;

        // 1. Handle fuel consumption
        if (isBurning) {
            entity.burnTime--;
            hasChanged = true;
        }

        ItemStack inputStack = entity.inventory.getItem(0);
        ItemStack fuelStack = entity.inventory.getItem(1);

        // 2. Process item if we have valid input
        if (!inputStack.isEmpty()) {
            // Check if we need to consume fuel to keep processing
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

            // If we are burning, advance progress
            if (isBurning) {
                entity.progress++;
                if (entity.progress >= MAX_PROGRESS) {
                    // TODO: Replace with actual Recipe lookup
                    // ItemStack output = RecipeRegistry.getSmeltingResult(inputStack);

                    inputStack.shrink(1);
                    // entity.inventory.setItem(2, output); // Add to output slot

                    entity.progress = 0;
                    hasChanged = true;
                }
            } else {
                // If fuel runs out, progress halts (and cools down/resets)
                if (entity.progress > 0) {
                    entity.progress = Math.max(0, entity.progress - 2);
                    hasChanged = true;
                }
            }
        } else {
            // Reset progress if input is removed
            if (entity.progress > 0) {
                entity.progress = 0;
                hasChanged = true;
            }
        }

        if (hasChanged) {
            entity.setChanged();
        }

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
        if (tag.contains("Inventory")) {
            this.inventory.fromTag(tag.getList("Inventory", 10), registries);
        }
    }
}