package com.example.mymod.block;

import com.example.mymod.blockentity.GyroBlockEntity;
import com.example.mymod.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class GyroBlock extends KineticBlock implements IWrenchable, IBE<GyroBlockEntity> {
    // 1.21.1 标准 Codec 定义，不需要再写额外的 Override 方法
    public static final MapCodec<GyroBlock> CODEC = simpleCodec(GyroBlock::new);

    public GyroBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends KineticBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == Direction.Axis.Y;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    // ===== IBE 接口实现：让 Create 自动管理 BE 和动力连接 =====
    @Override
    public Class<GyroBlockEntity> getBlockEntityClass() {
        return GyroBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GyroBlockEntity> getBlockEntityType() {
        return ModBlockEntities.GYRO_BE.get();
    }

    @SuppressWarnings("null")
    @Nullable
    @Override
    public BlockEntity newBlockEntity(@javax.annotation.Nullable BlockPos pos, @javax.annotation.Nullable BlockState state) {
        return ModBlockEntities.GYRO_BE.get().create(pos, state);
    }

    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    }

    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void fallOn(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, 0.0F);
    }
}