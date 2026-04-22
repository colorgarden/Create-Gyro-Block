package com.example.mymod.block;

import com.example.mymod.blockentity.GyroBlockEntity;
import com.example.mymod.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GyroBlock extends BaseEntityBlock {
    public static final MapCodec<GyroBlock> CODEC = simpleCodec(GyroBlock::new);
    private static final VoxelShape SHAPE = box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public GyroBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @SuppressWarnings("null")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @SuppressWarnings("null")
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
       if (!level.isClientSide) {
        // 1. 发送方块更新包到客户端，强制重新渲染该区域
        level.sendBlockUpdated(pos, oldState, state, 3);
        
        // 2. 如果它在子世界上，告诉 Sable 这里的物理数据变了
        if (level.getBlockEntity(pos) instanceof GyroBlockEntity be) {
            be.markPhysicsDirty();
        }
    }
    }

    @SuppressWarnings("null")
    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);
        if (level instanceof Level world && !world.isClientSide) {
            notifySubLevelUpdate(world, pos);
        }
    }

    private void notifySubLevelUpdate(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof GyroBlockEntity gyroBE) {
            gyroBE.markPhysicsDirty();
        }
    }

    @SuppressWarnings("null")
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("null")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // 仅在服务端运行 tick 逻辑
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.GYRO_BE.get(), GyroBlockEntity::tick);
    }

    @SuppressWarnings("null")
    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        // 核心特性：站在该方块上免疫掉落伤害
        super.fallOn(level, state, pos, entity, 0.0F);
    }

    @SuppressWarnings("null")
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GyroBlockEntity(pos, state);
    }
}