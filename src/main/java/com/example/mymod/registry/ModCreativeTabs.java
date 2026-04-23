package com.example.mymod.registry;

import com.example.mymod.MyMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModCreativeTabs {
    @SuppressWarnings("null")
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MyMod.MODID);

    @SuppressWarnings("null")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = 
        CREATIVE_TABS.register("gyroblock_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gyroblock_tab")) // 对应语言文件
            .icon(() -> new ItemStack(ModItems.GYRO_ITEM.get())) // 图标
            .displayItems((parameters, output) -> {
                // 将你的物品添加到标签页中
                output.accept(ModItems.GYRO_ITEM.get());
            })
            .build());
}