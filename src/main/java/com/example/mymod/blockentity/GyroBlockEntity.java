package com.example.mymod.blockentity;

import com.example.mymod.registry.ModBlockEntities;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class GyroBlockEntity extends BlockEntity {
    private static final double STABILIZATION_CONSTANT = 7.5;

    public GyroBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GYRO_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GyroBlockEntity be) {
        if (!level.isClientSide && level instanceof ServerLevel sl) {
            be.applyStabilization(sl);
        }
    }

    @SuppressWarnings("null")
    private void applyStabilization(ServerLevel sl) {
        SubLevel sub = Sable.HELPER.getContaining(sl, this.worldPosition);
        if (sub instanceof ServerSubLevel ssl) {
            RigidBodyHandle handle = RigidBodyHandle.of(ssl);
            if (handle != null) {
                Quaterniond orient = ssl.logicalPose().orientation();
                Vector3d shipUp = orient.transform(new Vector3d(0, 1, 0));
                Vector3d worldUp = new Vector3d(0, 1, 0);

                double angle = shipUp.angle(worldUp);
                if (angle > 0.01) {
                    Vector3d axis = shipUp.cross(worldUp, new Vector3d()).normalize();
                    Vector3d accel = axis.mul(angle * STABILIZATION_CONSTANT, new Vector3d());
                    
                    // 考虑上方红石信号，强度 15 时完全停止稳定
                    int power = level.getSignal(worldPosition.above(), Direction.UP);
                    double ctrl = 1.0 - (power / 15.0);
                    
                    if (ctrl > 0.01) {
                        handle.addLinearAndAngularVelocity(new Vector3d(), accel.mul(ctrl));
                    }
                }
            }
        }
    }

    public void markPhysicsDirty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markPhysicsDirty'");
    }
    @SuppressWarnings("null")
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    return saveWithoutMetadata(registries);
}

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
    // 创建一个同步包，当方块放置时立即发送给客户端
    return ClientboundBlockEntityDataPacket.create(this);
}
}