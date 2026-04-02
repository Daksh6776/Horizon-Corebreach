package com.horizonpack.horizoncore.blockentities;

import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.data.SkillType;
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

public class LaboratoryBlockEntity extends BlockEntity implements MenuProvider {
    public LaboratoryBlockEntity(BlockPos pos, BlockState state) {
        super(HorizonRegistries.LABORATORY_BE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.horizoncore.laboratory");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return ChestMenu.threeRows(containerId, playerInventory);
    }

    public boolean attemptExperiment(Player player) {
        IHorizonPlayerData data = HorizonCapabilities.get(player);
        if (data == null) return false;

        int scienceLevel = data.getSkillLevel(SkillType.SCIENCE);
        float successChance = 0.2f + (scienceLevel * 0.008f); // Starts at 20%, caps at 100%

        if (player.getRandom().nextFloat() < successChance) {
            // Logic for successful research [cite: 188]
            data.addSkillXP(SkillType.SCIENCE, 150);
            return true;
        } else {
            // Failure might consume materials or cause a small explosion
            return false;
        }
    }

    public void performExperiment(Player player) {
        IHorizonPlayerData data = HorizonCapabilities.get(player);
        if (data == null) return;

        int scienceLevel = data.getSkillLevel(com.horizonpack.horizoncore.data.SkillType.SCIENCE);
        // Base 20% chance + skill scaling as per spec [cite: 220]
        float successProbability = 0.2f + (scienceLevel * 0.008f);

        if (player.getRandom().nextFloat() < successProbability) {
            // Success: Grant significant Science XP [cite: 188]
            data.addSkillXP(com.horizonpack.horizoncore.data.SkillType.SCIENCE, 150);
            player.displayClientMessage(Component.literal("Experiment Successful!"), true);
        } else {
            // Failure logic: Consume reagents without result [cite: 220]
            player.displayClientMessage(Component.literal("Experiment Failed. Reagents lost."), true);
        }
    }
}