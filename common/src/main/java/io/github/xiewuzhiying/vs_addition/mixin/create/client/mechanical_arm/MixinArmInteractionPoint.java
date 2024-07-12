package io.github.xiewuzhiying.vs_addition.mixin.create.client.mechanical_arm;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmAngleTarget;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSClientGameUtils;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

@Mixin(ArmInteractionPoint.class)
public abstract class MixinArmInteractionPoint {

    @Shadow
    protected abstract Vec3 getInteractionPositionVector();

    @Shadow
    protected abstract Direction getInteractionDirection();

    @Shadow public abstract Level getLevel();

    @Shadow public abstract BlockPos getPos();

    @Shadow(remap = false) protected ArmAngleTarget cachedAngles;

    @Inject(
            method = "getTargetAngles",
            at = @At("HEAD")
    )
    public void vs_addition$getTargetAngles(BlockPos armPos, boolean ceiling, CallbackInfoReturnable<ArmAngleTarget> cir) {
        ClientShip armShip = VSClientGameUtils.getClientShip(armPos.getX(), armPos.getY(), armPos.getZ());
        ClientShip targetShip = VSClientGameUtils.getClientShip(getInteractionPositionVector().x, getInteractionPositionVector().y, getInteractionPositionVector().z);
        if (armShip != targetShip) {
            Vector3d target = new Vector3d();
            if(armShip == null){
                target = VSGameUtilsKt.toWorldCoordinates(getLevel(), new Vector3d(getInteractionPositionVector().x, getInteractionPositionVector().y, getInteractionPositionVector().z));
            }
            else {
                Vector3d armWorldPos = VSGameUtilsKt.toWorldCoordinates(getLevel(), new Vector3d(armPos.getX(), armPos.getY(), armPos.getZ()));
                Vector3d targetWorldPos = VSGameUtilsKt.toWorldCoordinates(getLevel(), new Vector3d(getInteractionPositionVector().x, getInteractionPositionVector().y, getInteractionPositionVector().z));
                Quaterniond armShipQuat = new Quaterniond(armShip.getTransform().getShipToWorldRotation());
                Vector3d destPos = new Vector3d(targetWorldPos.sub(armWorldPos));
                target = VectorConversionsMCKt.toJOMLD(armPos).add(destPos.rotate(armShipQuat.conjugate()));
            }
            cachedAngles = new ArmAngleTarget(armPos, new Vec3(target.x, target.y, target.z) , getInteractionDirection(), ceiling);
        }
    }
}
