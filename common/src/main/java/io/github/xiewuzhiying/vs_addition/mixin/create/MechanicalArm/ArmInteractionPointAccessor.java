package io.github.xiewuzhiying.vs_addition.mixin.create.MechanicalArm;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmAngleTarget;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmInteractionPoint.class)
public interface ArmInteractionPointAccessor {
    @Accessor("cachedAngles")
    void setCachedAngles(ArmAngleTarget cachedAngles);
}
