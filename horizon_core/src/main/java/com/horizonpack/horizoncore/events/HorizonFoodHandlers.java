package com.horizonpack.horizoncore.events;

import com.horizonpack.horizoncore.HorizonCore;
import com.horizonpack.horizoncore.capabilities.HorizonCapabilities;
import com.horizonpack.horizoncore.capabilities.IHorizonPlayerData;
import com.horizonpack.horizoncore.network.HorizonPacketHandler;
import com.horizonpack.horizoncore.network.packets.SyncPlayerDataPacket;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = HorizonCore.MODID)
public class HorizonFoodHandlers {

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack itemStack = event.getItem();

            // Check if the item is actually edible in 1.21.1
            FoodProperties food = itemStack.get(DataComponents.FOOD);
            if (food != null) {
                IHorizonPlayerData data = HorizonCapabilities.get(player);
                if (data != null) {

                    // Apply specific nutrients based on the item
                    applyNutrition(data, itemStack.getItem());

                    // Sync the new data straight to the client so the GTNH bars update instantly
                    HorizonPacketHandler.sendToPlayer(new SyncPlayerDataPacket(data.serializeNBT()), player);
                }
            }
        }
    }

    /**
     * Categorizes vanilla items into nutritional profiles.
     * Note: Math.min ensures the bars never go above 100%.
     */
    private static void applyNutrition(IHorizonPlayerData data, Item item) {

        // 🥩 Red Meats (High Protein, Med Fat, No Fiber)
        if (item == Items.COOKED_BEEF || item == Items.COOKED_PORKCHOP || item == Items.COOKED_MUTTON) {
            data.setProtein(Math.min(100f, data.getProtein() + 30.0f));
            data.setFats(Math.min(100f, data.getFats() + 15.0f));
        }

        // 🍗 Poultry & Fish (High Protein, Low Fat, No Fiber)
        else if (item == Items.COOKED_CHICKEN || item == Items.COOKED_RABBIT || item == Items.COOKED_COD || item == Items.COOKED_SALMON) {
            data.setProtein(Math.min(100f, data.getProtein() + 25.0f));
            data.setFats(Math.min(100f, data.getFats() + 5.0f));
        }

        // 🥖 Heavy Carbs (High Carbs, Low Fiber)
        else if (item == Items.BREAD || item == Items.BAKED_POTATO) {
            data.setCarbohydrates(Math.min(100f, data.getCarbohydrates() + 35.0f));
            data.setFiber(Math.min(100f, data.getFiber() + 10.0f));
        }

        // 🥕 Vegetables & Hard Fruits (Med Carbs, High Fiber, Low Hydration)
        else if (item == Items.APPLE || item == Items.CARROT || item == Items.BEETROOT) {
            data.setCarbohydrates(Math.min(100f, data.getCarbohydrates() + 15.0f));
            data.setFiber(Math.min(100f, data.getFiber() + 20.0f));
            data.setHydration(Math.min(100f, data.getHydration() + 10.0f));
        }

        // 🍉 Juicy Fruits (Low Carbs, Low Fiber, HIGH Hydration)
        else if (item == Items.MELON_SLICE || item == Items.SWEET_BERRIES || item == Items.GLOW_BERRIES || item == Items.CHORUS_FRUIT) {
            data.setCarbohydrates(Math.min(100f, data.getCarbohydrates() + 10.0f));
            data.setFiber(Math.min(100f, data.getFiber() + 5.0f));
            data.setHydration(Math.min(100f, data.getHydration() + 30.0f));
        }

        // 🍲 Stews and Soups (Mixed + High Hydration)
        else if (item == Items.MUSHROOM_STEW || item == Items.BEETROOT_SOUP || item == Items.RABBIT_STEW || item == Items.SUSPICIOUS_STEW) {
            data.setProtein(Math.min(100f, data.getProtein() + 15.0f));
            data.setCarbohydrates(Math.min(100f, data.getCarbohydrates() + 20.0f));
            data.setFiber(Math.min(100f, data.getFiber() + 15.0f));
            data.setHydration(Math.min(100f, data.getHydration() + 40.0f));
        }

        // 🍰 Sweets / Junk Food (Massive Carbs & Fats, NO Fiber)
        else if (item == Items.COOKIE || item == Items.PUMPKIN_PIE) {
            data.setCarbohydrates(Math.min(100f, data.getCarbohydrates() + 40.0f));
            data.setFats(Math.min(100f, data.getFats() + 25.0f));
        }

        // 🤢 Raw/Bad Meats (Low yield, realistically should make you sick later)
        else if (item == Items.BEEF || item == Items.PORKCHOP || item == Items.MUTTON || item == Items.CHICKEN || item == Items.RABBIT || item == Items.COD || item == Items.SALMON || item == Items.ROTTEN_FLESH || item == Items.SPIDER_EYE) {
            data.setProtein(Math.min(100f, data.getProtein() + 10.0f));
            data.setFats(Math.min(100f, data.getFats() + 5.0f));
        }

        // 🍎 Golden/Magic Items (Restores everything substantially)
        else if (item == Items.GOLDEN_APPLE || item == Items.ENCHANTED_GOLDEN_APPLE || item == Items.GOLDEN_CARROT) {
            data.setProtein(Math.min(100f, data.getProtein() + 25.0f));
            data.setCarbohydrates(Math.min(100f, data.getCarbohydrates() + 25.0f));
            data.setFats(Math.min(100f, data.getFats() + 25.0f));
            data.setFiber(Math.min(100f, data.getFiber() + 25.0f));
            data.setHydration(Math.min(100f, data.getHydration() + 25.0f));
        }
    }
}