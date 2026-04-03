import os

BASE_DIR_DATA = r"G:\Modpack\Horizon-Corebreach\horizon_data\src\main\java\com\horizonpack\horizondata"
BASE_DIR_CORE = r"G:\Modpack\Horizon-Corebreach\horizon_core\src\main\java\com\horizonpack\horizoncore"

WORKSHOPS = ["CarpentryBench", "Loom", "AlchemyLab", "Kitchen", "Tannery", "PotteryWheel", "Anvil", "Workbench"]
SHAPES = ["SlopeBlock", "ArchBlock", "PillarBlock", "BeamBlock", "CarvedBlock"]
MATERIALS = ["WoodBlock", "StoneBlock", "MetalBlock", "CompositeBlock", "WeatheringBlock"]
ITEMS = ["RawOreItem", "IngotItem", "DustItem", "PlateItem", "WireItem", "GearItem"]
RECIPES = ["ForgeRecipe", "CarpentryRecipe", "AlloyRecipe"]
EVENTS = ["OreMinedEvent", "WorkshopCraftEvent"]
PACKETS = ["SyncWorkshopPacket", "WorkshopProgressPacket"]

def write_class(base, pkg, class_name, content, imports=[]):
    dir_path = os.path.join(base, *pkg.split("."))
    os.makedirs(dir_path, exist_ok=True)
    file_path = os.path.join(dir_path, class_name + ".java")
    
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(f"package {base.replace(os.sep, '.').split('src.main.java.')[1]}.{pkg};\n\n")
        for imp in imports:
            f.write(f"import {imp};\n")
        f.write("\n")
        f.write(content)

# Workshops Blocks
for w in WORKSHOPS:
    content = f"""public class {w}Block extends Block {{
    public {w}Block(Properties properties) {{
        super(properties);
    }}
}}
"""
    write_class(BASE_DIR_DATA, "blocks.workshops", f"{w}Block", content, ["net.minecraft.world.level.block.Block"])

# Workshop Block Entities
for w in WORKSHOPS:
    content = f"""public class {w}BlockEntity extends BlockEntity {{
    public {w}BlockEntity(BlockPos pos, BlockState state) {{
        super(null, pos, state); // null should be a registered BlockEntityType
    }}
}}
"""
    write_class(BASE_DIR_DATA, "blockentities", f"{w}BlockEntity", content, [
        "net.minecraft.world.level.block.entity.BlockEntity",
        "net.minecraft.core.BlockPos",
        "net.minecraft.world.level.block.state.BlockState"
    ])

# Workshop Menus
for w in WORKSHOPS:
    content = f"""public class {w}Menu extends AbstractContainerMenu {{
    public {w}Menu(int containerId, Inventory playerInventory) {{
        super(null, containerId); // null should be a registered MenuType
    }}

    @Override
    public ItemStack quickMoveStack(Player player, int index) {{
        return ItemStack.EMPTY;
    }}

    @Override
    public boolean stillValid(Player player) {{
        return true;
    }}
}}
"""
    write_class(BASE_DIR_DATA, "inventory", f"{w}Menu", content, [
        "net.minecraft.world.inventory.AbstractContainerMenu",
        "net.minecraft.world.entity.player.Inventory",
        "net.minecraft.world.entity.player.Player",
        "net.minecraft.world.item.ItemStack"
    ])

# Shapes
for s in SHAPES:
    content = f"""public class {s} extends Block {{
    public {s}(Properties properties) {{
        super(properties);
    }}
}}
"""
    write_class(BASE_DIR_DATA, "blocks.shapes", s, content, ["net.minecraft.world.level.block.Block"])

# Materials
for m in MATERIALS:
    content = f"""public class {m} extends Block {{
    public {m}(Properties properties) {{
        super(properties);
    }}
}}
"""
    write_class(BASE_DIR_DATA, "blocks.materials", m, content, ["net.minecraft.world.level.block.Block"])

# Items
for i in ITEMS:
    content = f"""public class {i} extends Item {{
    public {i}(Properties properties) {{
        super(properties);
    }}
}}
"""
    write_class(BASE_DIR_DATA, "items", i, content, ["net.minecraft.world.item.Item"])

# Recipes
for r in RECIPES:
    content = f"""public class {r} implements Recipe<CraftingInput> {{
    @Override
    public boolean matches(CraftingInput inv, net.minecraft.world.level.Level level) {{ return false; }}
    @Override
    public ItemStack assemble(CraftingInput inv, net.minecraft.core.HolderLookup.Provider access) {{ return ItemStack.EMPTY; }}
    @Override
    public boolean canCraftInDimensions(int width, int height) {{ return true; }}
    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider access) {{ return ItemStack.EMPTY; }}
    @Override
    public RecipeSerializer<?> getSerializer() {{ return null; }}
    @Override
    public RecipeType<?> getType() {{ return null; }}
}}
"""
    write_class(BASE_DIR_DATA, "recipes", r, content, [
        "net.minecraft.world.item.crafting.Recipe",
        "net.minecraft.world.item.crafting.RecipeSerializer",
        "net.minecraft.world.item.crafting.RecipeType",
        "net.minecraft.world.item.crafting.CraftingInput",
        "net.minecraft.world.item.ItemStack"
    ])

# Weathering & Alloys
write_class(BASE_DIR_DATA, "systems.weathering", "WeatheringSystem", "public class WeatheringSystem {\n    public static void applyWeathering() {}\n}\n")
write_class(BASE_DIR_DATA, "systems.alloys", "AlloyRegistry", "public class AlloyRegistry {\n    public static void registerAlloys() {}\n}\n")

# Events
for e in EVENTS:
    content = f"""public class {e} extends Event {{
    public {e}() {{}}
}}
"""
    write_class(BASE_DIR_DATA, "events", e, content, ["net.neoforged.bus.api.Event"])

# Packets
for p in PACKETS:
    content = f"""public class {p} {{
    public {p}() {{}}
}}
"""
    write_class(BASE_DIR_DATA, "network", p, content)

# Client/GUI
for w in WORKSHOPS:
    content = f"""public class {w}Screen extends net.minecraft.client.gui.screens.Screen {{
    public {w}Screen(net.minecraft.network.chat.Component title) {{
        super(title);
    }}
}}
"""
    write_class(BASE_DIR_CORE, "client.gui", f"{w}Screen", content)

write_class(BASE_DIR_CORE, "client.render", "SlopeBlockRenderer", "public class SlopeBlockRenderer {\n    // TODO: implement block renderer\n}\n")

print("Files generated.")
