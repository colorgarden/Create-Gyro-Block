package com.example.mymod;

import javax.annotation.Nonnull;

import com.example.mymod.registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(MyMod.MODID)
public class MyMod {
    public static final String MODID = "gyroblock";

    public MyMod(@Nonnull IEventBus modEventBus, ModContainer modContainer) {
        // 1. 注册方块和物品
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        
        // 2. 注册方块实体 (请确保 ModBlockEntities 类里定义的是 BLOCK_ENTITIES)
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        
        // 3. 注册创造模式物品栏
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        // 4. 注册服务器配置文件
        modContainer.registerConfig(ModConfig.Type.SERVER, GyroConfig.SPEC);
    }
}