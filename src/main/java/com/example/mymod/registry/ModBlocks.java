package com.example.mymod.registry;

import com.example.mymod.MyMod;
import com.example.mymod.block.GyroBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MyMod.MODID);

    public static final DeferredBlock<GyroBlock> GYRO = BLOCKS.registerBlock("gyro", 
        GyroBlock::new, BlockBehaviour.Properties.of().strength(2.0f).requiresCorrectToolForDrops());
}