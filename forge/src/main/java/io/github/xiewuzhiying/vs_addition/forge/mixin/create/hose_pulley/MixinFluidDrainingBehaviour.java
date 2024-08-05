package io.github.xiewuzhiying.vs_addition.forge.mixin.create.hose_pulley;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import com.simibubi.create.content.fluids.transfer.FluidManipulationBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.xiewuzhiying.vs_addition.util.TransformUtilsKt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;

@Pseudo
@Mixin(FluidDrainingBehaviour.class)
public abstract class MixinFluidDrainingBehaviour extends FluidManipulationBehaviour {
    public MixinFluidDrainingBehaviour(SmartBlockEntity be) {
        super(be);
    }

    @Inject(method = "pullNext", at = @At(value = "HEAD"), remap = false)
    public void pullNext(BlockPos root, boolean simulate, CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) LocalRef<BlockPos> blockPosLocalRef) {
        Vec3 vec3 = TransformUtilsKt.front(root, Direction.DOWN);
        Ship ship = VSGameUtilsKt.getShipManagingPos(this.getWorld(), vec3);
        if (ship != null) {
            vec3 = TransformUtilsKt.toWorld(vec3, ship);
        }
        List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(this.getWorld(), vec3.x, vec3.y, vec3.z, 0.1);
        if (!ships.isEmpty()) {
            Ship rootShip = VSGameUtilsKt.getShipManagingPos(this.getWorld(), ships.get(0));
            if (rootShip != null) {
                blockPosLocalRef.set(TransformUtilsKt.getToBlockPos(TransformUtilsKt.toShipyardCoordinates(vec3, rootShip)));
                return;
            }
        }
        blockPosLocalRef.set(TransformUtilsKt.getToBlockPos(vec3));
    }
}
