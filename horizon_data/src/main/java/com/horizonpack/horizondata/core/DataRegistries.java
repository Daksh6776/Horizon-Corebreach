package com.horizonpack.horizondata.core;

import com.horizonpack.horizondata.blockentities.*;
import com.horizonpack.horizondata.blocks.HorizonDataBlocks;
import com.horizonpack.horizondata.data.WorkshopType;
import com.horizonpack.horizondata.inventory.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;



public class DataRegistries {
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<BloomeryBlockEntity>> BLOOMERY_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CrucibleBlockEntity>> CRUCIBLE_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ForgeBlockEntity>> FORGE_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<GrindstoneBlockEntity>> GRINDSTONE_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<AlchemyLabBlockEntity>> ALCHEMY_LAB_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<AnvilBlockEntity>> ANVIL_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CarpentryBenchBlockEntity>> CARPENTRY_BENCH_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<KitchenBlockEntity>> KITCHEN_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<LoomBlockEntity>> LOOM_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<PotteryWheelBlockEntity>> POTTERY_WHEEL_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TanneryBlockEntity>> TANNERY_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<WorkbenchBlockEntity>> WORKBENCH_BE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<OilDepositBlockEntity>> DEPOSIT_BE;
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, HorizonData.MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, HorizonData.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HorizonData.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(BuiltInRegistries.MENU, HorizonData.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, HorizonData.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, HorizonData.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HorizonData.MODID);
    public static final Map<WorkshopType, Supplier<RecipeType<?>>> WORKSHOP_RECIPE_TYPES =
            new EnumMap<>(WorkshopType.class);
    // --- Menus ---
    public static final Supplier<MenuType<AnvilMenu>> ANVIL_MENU =
            MENU_TYPES.register("anvil", () -> new MenuType<>(AnvilMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<AlchemyLabMenu>> ALCHEMY_LAB_MENU =
            MENU_TYPES.register("alchemy_lab", () -> new MenuType<>(AlchemyLabMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<BloomeryMenu>> BLOOMERY_MENU =
            MENU_TYPES.register("bloomery", () -> new MenuType<>(BloomeryMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<CrucibleMenu>> CRUCIBLE_MENU =
            MENU_TYPES.register("crucible", () -> new MenuType<>(CrucibleMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<ForgeMenu>> FORGE_MENU =
            MENU_TYPES.register("forge", () -> new MenuType<>(ForgeMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<GrindstoneMenu>> GRINDSTONE_MENU =
            MENU_TYPES.register("grindstone", () -> new MenuType<>(GrindstoneMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<LoomMenu>> LOOM_MENU =
            MENU_TYPES.register("loom", () -> new MenuType<>(LoomMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<TanneryMenu>> TANNERY_MENU =
            MENU_TYPES.register("tannery", () -> new MenuType<>(TanneryMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<WorkbenchMenu>> WORKBENCH_MENU =
            MENU_TYPES.register("workbench", () -> new MenuType<>(WorkbenchMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<PotteryWheelMenu>> POTTERY_WHEEL_MENU =
            MENU_TYPES.register("pottery_wheel", () -> new MenuType<>(PotteryWheelMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<KitchenMenu>> KITCHEN_MENU =
            MENU_TYPES.register("kitchen", () -> new MenuType<>(KitchenMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<CarpentryBenchMenu>> CARPENTRY_BENCH_MENU =
            MENU_TYPES.register("carpentry_bench", () -> new MenuType<>(CarpentryBenchMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final Supplier<CreativeModeTab> HORIZON_TAB = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            // 1. Set the name of the tab (we will translate this in the lang file next)
            .title(Component.translatable("itemGroup.horizondata.main"))

            // 2. Set the icon for the tab (using the Forge block as an example)
            .icon(() -> new ItemStack(HorizonDataBlocks.FORGE.get()))

            // 3. Add the items
            .displayItems((parameters, output) -> {
                // MAGIC LOOP: This automatically adds EVERY item and block item
                // you have registered in your entire mod to this specific tab!
                for (var itemHolder : ITEMS.getEntries()) {
                    output.accept(itemHolder.get());
                }
            })
            .build());

    public static void register(IEventBus eventBus) {
        // 1. Subscribe all DeferredRegisters to the bus FIRST
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        MENU_TYPES.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        CREATIVE_TABS.register(eventBus);

        // 2. NOW it's safe to touch HorizonDataBlocks — bus is already subscribed
        HorizonDataBlocks.init();

        // 3. Register block entities (lambdas capture .get() lazily, safe here)
        BLOOMERY_BE = BLOCK_ENTITIES.register("bloomery",
                () -> BlockEntityType.Builder.of(BloomeryBlockEntity::new, HorizonDataBlocks.BLOOMERY.get()).build(null));
        CRUCIBLE_BE = BLOCK_ENTITIES.register("crucible",
                () -> BlockEntityType.Builder.of(CrucibleBlockEntity::new, HorizonDataBlocks.CRUCIBLE.get()).build(null));
        FORGE_BE = BLOCK_ENTITIES.register("forge",
                () -> BlockEntityType.Builder.of(ForgeBlockEntity::new, HorizonDataBlocks.FORGE.get()).build(null));
        GRINDSTONE_BE = BLOCK_ENTITIES.register("grindstone",
                () -> BlockEntityType.Builder.of(GrindstoneBlockEntity::new, HorizonDataBlocks.GRINDSTONE.get()).build(null));
        ALCHEMY_LAB_BE = BLOCK_ENTITIES.register("alchemy_lab",
                () -> BlockEntityType.Builder.of(AlchemyLabBlockEntity::new, HorizonDataBlocks.ALCHEMY_LAB.get()).build(null));
        ANVIL_BE = BLOCK_ENTITIES.register("anvil",
                () -> BlockEntityType.Builder.of(AnvilBlockEntity::new, HorizonDataBlocks.ANVIL.get()).build(null));
        CARPENTRY_BENCH_BE = BLOCK_ENTITIES.register("carpentry_bench",
                () -> BlockEntityType.Builder.of(CarpentryBenchBlockEntity::new, HorizonDataBlocks.CARPENTRY_BENCH.get()).build(null));
        KITCHEN_BE = BLOCK_ENTITIES.register("kitchen",
                () -> BlockEntityType.Builder.of(KitchenBlockEntity::new, HorizonDataBlocks.KITCHEN.get()).build(null));
        LOOM_BE = BLOCK_ENTITIES.register("loom",
                () -> BlockEntityType.Builder.of(LoomBlockEntity::new, HorizonDataBlocks.LOOM.get()).build(null));
        POTTERY_WHEEL_BE = BLOCK_ENTITIES.register("pottery_wheel",
                () -> BlockEntityType.Builder.of(PotteryWheelBlockEntity::new, HorizonDataBlocks.POTTERY_WHEEL.get()).build(null));
        TANNERY_BE = BLOCK_ENTITIES.register("tannery",
                () -> BlockEntityType.Builder.of(TanneryBlockEntity::new, HorizonDataBlocks.TANNERY.get()).build(null));
        WORKBENCH_BE = BLOCK_ENTITIES.register("workbench",
                () -> BlockEntityType.Builder.of(WorkbenchBlockEntity::new, HorizonDataBlocks.WORKBENCH.get()).build(null));
        DEPOSIT_BE = BLOCK_ENTITIES.register("deposit",
                () -> BlockEntityType.Builder.of(OilDepositBlockEntity::new,
                        HorizonDataBlocks.OIL_DEPOSIT.get(),
                        HorizonDataBlocks.GAS_DEPOSIT.get()).build(null));

        // 4. Register workshop recipe types
        for (WorkshopType type : WorkshopType.values()) {
            if (type.id() != null) {
                WORKSHOP_RECIPE_TYPES.put(type, RECIPE_TYPES.register(type.id() + "_recipe", () -> new RecipeType<>() {
                    @Override public String toString() { return HorizonData.MODID + ":" + type.id(); }
                }));
            }
        }
    }
}
