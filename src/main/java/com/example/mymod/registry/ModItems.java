package com.example.mymod.registry;

import java.util.List;

import javax.annotation.Nonnull;

import com.example.mymod.MyMod;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MyMod.MODID);

    // 陀螺仪物品：包含黄色加粗提示和灰色说明
    public static final DeferredItem<BlockItem> GYRO_ITEM = ITEMS.register("gyro", () -> 
        new BlockItem(ModBlocks.GYRO.get(), new Item.Properties()) {
            @SuppressWarnings("null")
            @Override
            public void appendHoverText(@Nonnull ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
                tooltip.add(Component.translatable("item.gyroblock.gyro.tooltip1").withStyle(ChatFormatting.YELLOW));
                tooltip.add(Component.translatable("item.gyroblock.gyro.tooltip2").withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("item.gyroblock.gyro.tooltip3").withStyle(ChatFormatting.GRAY));
                super.appendHoverText(stack, context, tooltip, flag);
            }
        }
    );
}