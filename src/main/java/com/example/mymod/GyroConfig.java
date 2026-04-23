package com.example.mymod;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GyroConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Double> GYRO_STRESS;

    static {
        BUILDER.push("Gyro Settings");
        GYRO_STRESS = BUILDER
                .comment("陀螺仪的基础应力消耗值 (默认: 512.0)")
                .defineInRange("gyroStress", 512.0, 4.0, 16384.0);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}