package com.example.mymod;

import com.example.mymod.registry.ModBlockEntities;
import com.example.mymod.registry.ModBlocks;
import com.example.mymod.registry.ModCreativeTabs;
import com.example.mymod.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MyMod.MODID)
public class MyMod {
    public static final String MODID = "gyroblock"; // 确保此 ID 与 build.gradle 相同 [cite: 26]

    public MyMod(IEventBus modEventBus) {
        // 按照规范在构造函数中注册所有 DeferredRegister
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
    }
}