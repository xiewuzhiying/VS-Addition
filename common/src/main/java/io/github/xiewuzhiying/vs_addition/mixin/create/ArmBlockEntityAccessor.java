package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmBlockEntity.class)
public interface ArmBlockEntityAccessor {
    @Accessor("updateInteractionPoints")
    void setUpdateInteractionPoints(boolean updateInteractionPoints);
}
