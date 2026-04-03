package com.horizonpack.horizoncore.inventory;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.network.HorizonPacketHandler;
import com.horizonpack.horizoncore.network.packets.SyncPlayerDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ResearchBenchMenu extends AbstractContainerMenu {
    private final Container container;
    private final Player player; // <-- 1. STORE THE PLAYER

    public ResearchBenchMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(2));
    }

    public ResearchBenchMenu(int containerId, Inventory playerInventory, Container container) {
        super(HorizonRegistries.RESEARCH_BENCH_MENU.get(), containerId);
        this.container = container;
        this.player = playerInventory.player; // <-- 2. GRAB THE PLAYER

        checkContainerSize(container, 2);
        container.startOpen(this.player);

        // Slot 0: Input
        this.addSlot(new Slot(container, 0, 27, 47));

        // Slot 1: Output (Result)
        this.addSlot(new Slot(container, 1, 134, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }

            // 3. GRANT XP WHEN TAKEN
            @Override
            public void onTake(Player p, ItemStack stack) {
                if (!p.level().isClientSide && p instanceof ServerPlayer serverPlayer) {
                    IHorizonPlayerData data = HorizonCapabilities.get(serverPlayer);
                    if (data != null) {
                        data.addIntelligenceXP(10); // Reward 10 XP per research
                        HorizonPacketHandler.sendToPlayer(new SyncPlayerDataPacket(data.serializeNBT()), serverPlayer);
                    }
                }
                super.onTake(p, stack);
            }
        });

        // Inventory & Hotbar setup (same as before)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == this.container) {
            ItemStack inputStack = container.getItem(0);
            if (!inputStack.isEmpty()) {
                ResearchRecipes.ResearchResult entry = ResearchRecipes.getEntry(inputStack.getItem());

                // 4. CHECK STATS
                IHorizonPlayerData data = HorizonCapabilities.get(this.player);
                if (entry != null && data != null && data.getIntelligence() >= entry.requiredIntel()) {
                    container.setItem(1, new ItemStack(entry.item()));
                } else {
                    container.setItem(1, ItemStack.EMPTY);
                }
            } else {
                container.setItem(1, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // ... (Shift-click logic from earlier) ...
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}