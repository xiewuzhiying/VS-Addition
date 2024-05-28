package io.github.xiewuzhiying.vs_addition.fabric.mixin.create;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.fluids.transfer.FluidFillingBehaviour;
import com.simibubi.create.content.fluids.transfer.FluidManipulationBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import io.github.xiewuzhiying.vs_addition.util.transformUtils;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;

@Mixin(FluidFillingBehaviour.class)
public abstract class MixinFluidFillingBehaviour extends FluidManipulationBehaviour {
    public MixinFluidFillingBehaviour(SmartBlockEntity be) {
        super(be);
    }
    @Inject(method = "tryDeposit",at = @At(value = "HEAD"),remap = false)
    public void tryDeposit(Fluid fluid, BlockPos root, TransactionContext ctx, CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) LocalRef<BlockPos> blockPosLocalRef){
        Vec3 vec3 = transformUtils.getFront(Direction.DOWN, root);
        Ship ship = VSGameUtilsKt.getShipManagingPos(this.getWorld(),vec3);
        if(ship!=null){
            vec3=transformUtils.toWorldVec3(ship,vec3);
        }
        List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(this.getWorld(),vec3.x,vec3.y,vec3.z,0.1);
        if(!ships.isEmpty()){
            Ship rootShip = VSGameUtilsKt.getShipManagingPos(this.getWorld(),ships.get(0));
            if(rootShip!=null){
                blockPosLocalRef.set(transformUtils.floorToBlockPos(transformUtils.toShipyardCoordinates(rootShip,vec3)));
                return;
            }
        }
        blockPosLocalRef.set(transformUtils.floorToBlockPos(vec3));
    }
}
