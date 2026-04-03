package com.horizonpack.horizondata.blockentities;

import com.horizonpack.horizondata.core.DataRegistries;
import com.horizonpack.horizondata.data.FuelDefinition;
import com.horizonpack.horizondata.systems.FuelRegistry;
// import com.horizonpack.horizondata.systems.AlloyRegistry; // To be implemented later
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

public class CrucibleBlockEntity extends BlockEntity {
    // Slot 0: Input 1, Slot 1: Input 2, Slot 2: Fuel, Slot 3: Output (Alloy)
    private final SimpleContainer inventory = new SimpleContainer(4);

    private int progress = 0;
    private int burnTime = 0;
    private int maxBurnTime = 0;
    private static final int MAX_PROGRESS = 300;

    public CrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(DataRegistries.CRUCIBLE_BE.get(), pos, state);
    }

    public SimpleContainer getInventory() { return inventory; }

    public static void tick(Level level, BlockPos pos, BlockState state, CrucibleBlockEntity entity) {
        if (level.isClientSide) return;

        boolean isBurning = entity.burnTime > 0;
        boolean hasChanged = false;

        if (isBurning) {
            entity.burnTime--;
            hasChanged = true;
        }

        ItemStack input1 = entity.inventory.getItem(0);
        ItemStack input2 = entity.inventory.getItem(1);
        ItemStack fuelStack = entity.inventory.getItem(2);

        // 2. Process item if we have BOTH inputs
        if (!input1.isEmpty() && !input2.isEmpty()) {

            // NOTE: In production, check AlloyRegistry here to ensure input1 and input2 form a valid recipe
            // boolean hasValidRecipe = AlloyRegistry.hasRecipe(input1, input2);

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
                    // TODO: Replace with actual Alloy lookup
                    // ItemStack output = AlloyRegistry.getAlloyResult(input1, input2);

                    input1.shrink(1);
                    input2.shrink(1);
                    // entity.inventory.setItem(3, output);

                    entity.progress = 0;
                    hasChanged = true;
                }
            } else {
                if (entity.progress > 0) {
                    entity.progress = Math.max(0, entity.progress - 2);
                    hasChanged = true;
                }
            }
        } else {
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