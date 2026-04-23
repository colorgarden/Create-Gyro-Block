package com.example.mymod.blockentity;

import com.example.mymod.GyroConfig;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import java.util.List;

public class GyroBlockEntity extends KineticBlockEntity {
    private static final double STABILIZATION_CONSTANT = 7.5;

    public GyroBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @SuppressWarnings("null")
    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide && level instanceof ServerLevel sl) {
            float currentSpeed = Math.abs(getSpeed());
            if (currentSpeed >= 256.0f) {
                applyStabilization(sl);
            }
        }
    }

    @Override
    public float calculateStressApplied() {
        // 从配置中读取应力消耗
        float impact = GyroConfig.GYRO_STRESS.get().floatValue();
        this.lastStressApplied = impact;
        return impact;
    }

    @SuppressWarnings("null")
    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (level != null && !level.isClientSide) {
            notifyUpdate();
        }
    }

    @SuppressWarnings("null")
    private void applyStabilization(ServerLevel sl) {
        SubLevel sub = Sable.HELPER.getContaining(sl, worldPosition);
        if (sub instanceof ServerSubLevel ssl) {
            RigidBodyHandle handle = RigidBodyHandle.of(ssl);
            if (handle != null && ssl.logicalPose() != null) {
                Quaterniond orient = ssl.logicalPose().orientation();
                Vector3d shipUp = orient.transform(new Vector3d(0, 1, 0), new Vector3d());
                Vector3d worldUp = new Vector3d(0, 1, 0);

                double angle = shipUp.angle(worldUp);
                if (angle > 0.01) {
                    Vector3d axis = shipUp.cross(worldUp, new Vector3d());
                    if (axis.lengthSquared() > 1e-6) {
                        axis.normalize();
                        Vector3d accel = axis.mul(angle * STABILIZATION_CONSTANT, new Vector3d());
                        int power = level.getSignal(worldPosition.above(), Direction.UP);
                        double ctrl = 1.0 - (power / 15.0);
                        if (ctrl > 0.01) {
                            handle.addLinearAndAngularVelocity(new Vector3d(), accel.mul(ctrl));
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("null")
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        float currentSpeed = Math.abs(getSpeed());
        if (currentSpeed > 0 && currentSpeed < 256.0f) {
            tooltip.add(Component.empty());
            tooltip.add(Component.literal(" ⚠ 运行速度过低").withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.literal("  至少需要: ").withStyle(ChatFormatting.GRAY)
                       .append(Component.literal("256 RPM").withStyle(ChatFormatting.WHITE)));
            added = true;
        }
        return added;
    }
}