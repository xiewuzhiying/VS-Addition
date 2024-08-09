package io.github.xiewuzhiying.vs_addition.mixin.create.portable_interface;

import com.simibubi.create.content.contraptions.DirectionalExtenderScrollOptionSlot;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import io.github.xiewuzhiying.vs_addition.mixinducks.create.portable_interface.IPSIBehavior;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PortableStorageInterfaceBlockEntity.class)
public abstract class MixinPortableStorageInterfaceBlockEntity implements IPSIBehavior {

    public ScrollOptionBehaviour<WorkigMode> workingMode;

    @Inject(
            method = "addBehaviours",
            at = @At("RETURN"),
            remap = false
    )
    public void behaviour(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        this.workingMode = new ScrollOptionBehaviour<>(IPSIBehavior.WorkigMode.class, Lang.translateDirect("vs_addition.working_mode"), (PortableStorageInterfaceBlockEntity)(Object) this, vs_addition$getMovementModeSlot());
        behaviours.add(this.workingMode);
    }

    @Unique
    private ValueBoxTransform vs_addition$getMovementModeSlot() {
        return new DirectionalExtenderScrollOptionSlot((state, d) -> {
            Direction.Axis axis = d.getAxis();
            Direction.Axis bearingAxis = state.getValue(PortableStorageInterfaceBlock.FACING)
                    .getAxis();
            return bearingAxis != axis;
        });
    }

    @Override
    public ScrollOptionBehaviour<IPSIBehavior.WorkigMode> vs_addition$getWorkingMode() {
        return workingMode;
    }
}
