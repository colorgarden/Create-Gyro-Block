package com.example.mymod.registry;

import com.example.mymod.MyMod;
import com.example.mymod.blockentity.GyroBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    @SuppressWarnings("null")
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MyMod.MODID);

    @SuppressWarnings("null")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GyroBlockEntity>> GYRO_BE = 
        BLOCK_ENTITIES.register("gyro", () -> BlockEntityType.Builder.of(
            ModBlockEntities::createGyroBE, 
            ModBlocks.GYRO.get()
        ).build(null));

    private static GyroBlockEntity createGyroBE(BlockPos pos, BlockState state) {
        return new GyroBlockEntity(GYRO_BE.get(), pos, state);
    }
}