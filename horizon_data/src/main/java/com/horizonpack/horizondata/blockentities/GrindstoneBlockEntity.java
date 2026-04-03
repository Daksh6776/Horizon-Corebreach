package com.horizonpack.horizondata.blockentities;

import com.horizonpack.horizondata.core.DataRegistries;
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

public class GrindstoneBlockEntity extends BlockEntity {
    // Slot 0: Input (Ingot), Slot 1: Output (Dust)
    // No fuel required.
    private final SimpleContainer inventory = new SimpleContainer(2);

    private int progress = 0;
    private static final int MAX_PROGRESS = 100;

    public GrindstoneBlockEntity(BlockPos pos, BlockState state) {
        super(DataRegistries.GRINDSTONE_BE.get(), pos, state);
    }

    public SimpleContainer getInventory() { return inventory; }

    public static void tick(Level level, BlockPos pos, BlockState state, GrindstoneBlockEntity entity) {
        if (level.isClientSide) return;

        boolean hasChanged = false;
        ItemStack inputStack = entity.inventory.getItem(0);

        // Grindstone works automatically if an item is present (no fuel check needed)
        if (!inputStack.isEmpty()) {
            entity.progress++;
            hasChanged = true;

            if (entity.progress >= MAX_PROGRESS) {
                // TODO: Replace with actual Grindstone Recipe lookup
                // ItemStack output = RecipeRegistry.getGrindstoneResult(inputStack);

                inputStack.shrink(1);
                // entity.inventory.setItem(1, output);

                entity.progress = 0;
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
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Progress", this.progress);
        tag.put("Inventory", this.inventory.createTag(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.progress = tag.getInt("Progress");
        if (tag.contains("Inventory")) {
            this.inventory.fromTag(tag.getList("Inventory", 10), registries);
        }
    }
}