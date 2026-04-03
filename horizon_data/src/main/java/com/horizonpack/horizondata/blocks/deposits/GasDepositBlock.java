package com.horizonpack.horizondata.blocks.deposits;

import com.horizonpack.horizondata.blockentities.OilDepositBlockEntity;
import com.horizonpack.horizondata.core.HorizonData;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class GasDepositBlock extends BaseEntityBlock {
    public static final MapCodec<GasDepositBlock> CODEC = simpleCodec(GasDepositBlock::new);
    public static final BooleanProperty DEPLETED = BooleanProperty.create("depleted");

    public GasDepositBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(DEPLETED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DEPLETED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // We can reuse the OilDepositBlockEntity since the logic (decrement uses) is identical
        return new OilDepositBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(DEPLETED)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Grab your custom empty canister item definition
        Item emptyCanister = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(HorizonData.MODID, "empty_canister"));

        if (stack.getItem() == emptyCanister) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof OilDepositBlockEntity deposit) {
                    if (deposit.extract()) {
                        stack.shrink(1);

                        ItemStack fullCanister = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(HorizonData.MODID, "natural_gas_canister")));

                        if (stack.isEmpty()) {
                            player.setItemInHand(hand, fullCanister);
                        } else if (!player.getInventory().add(fullCanister)) {
                            player.drop(fullCanister, false);
                        }

                        if (deposit.isDepleted()) {
                            level.setBlock(pos, state.setValue(DEPLETED, true), 3);
                        }
                    }
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    // --- Add Particle Effects for Natural Gas ---
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Only emit particles if there is still gas left
        if (!state.getValue(DEPLETED)) {
            // ~20% chance per tick to spawn a subtle gas particle
            if (random.nextInt(5) == 0) {
                double d0 = (double)pos.getX() + random.nextDouble();
                double d1 = (double)pos.getY() + 1.0D; // Spawns just above the block
                double d2 = (double)pos.getZ() + random.nextDouble();

                // Using a white smoke particle to simulate venting gas
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, d0, d1, d2, 0.0D, 0.05D, 0.0D);
            }
        }
    }
}