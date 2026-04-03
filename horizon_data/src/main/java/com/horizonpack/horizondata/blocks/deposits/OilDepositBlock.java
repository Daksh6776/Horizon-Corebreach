package com.horizonpack.horizondata.blocks.deposits;

import com.horizonpack.horizondata.blockentities.OilDepositBlockEntity;
import com.horizonpack.horizondata.core.HorizonData;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class OilDepositBlock extends BaseEntityBlock {
    public static final MapCodec<OilDepositBlock> CODEC = simpleCodec(OilDepositBlock::new);
    public static final BooleanProperty DEPLETED = BooleanProperty.create("depleted");

    public OilDepositBlock(Properties properties) {
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
        return RenderShape.MODEL; // Prevents the block from being invisible
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OilDepositBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // If depleted, act like a normal block
        if (state.getValue(DEPLETED)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Check if the player is holding an empty vanilla bucket
        if (stack.is(Items.BUCKET)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof OilDepositBlockEntity deposit) {
                    if (deposit.extract()) {
                        // Consume empty bucket
                        stack.shrink(1);

                        // Give oil bucket (assuming you register it via DeferredRegister elsewhere, using ResourceLocation fetch here as a fallback)
                        ItemStack oilBucket = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(HorizonData.MODID, "oil_bucket")));

                        if (stack.isEmpty()) {
                            player.setItemInHand(hand, oilBucket);
                        } else if (!player.getInventory().add(oilBucket)) {
                            player.drop(oilBucket, false);
                        }

                        // Check if that was the last drop
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
}