package com.horizonpack.horizoncore.blockentities;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class UniversityBlockEntity extends BlockEntity implements MenuProvider {
    public UniversityBlockEntity(BlockPos pos, BlockState state) {
        super(HorizonRegistries.UNIVERSITY_BE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.horizoncore.university");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return ChestMenu.threeRows(containerId, playerInventory);
    }
}