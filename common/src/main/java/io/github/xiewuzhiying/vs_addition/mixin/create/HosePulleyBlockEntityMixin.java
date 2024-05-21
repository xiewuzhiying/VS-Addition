package io.github.xiewuzhiying.vs_addition.mixin.create;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.xiewuzhiying.vs_addition.util.transformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HosePulleyBlockEntity.class)
public abstract class HosePulleyBlockEntityMixin extends KineticBlockEntity {

    @Shadow
    LerpedFloat offset;

    public HosePulleyBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    @ModifyExpressionValue(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/Material;isReplaceable()Z"))
    public boolean tick(boolean original, @Local(ordinal = 0) float newOffset){
        original = !original;
        Level level = this.level;
        Vec3 vec3From = transformUtils.toWorldVec3(level,transformUtils.vec3Below(transformUtils.getFront(Direction.DOWN,this.worldPosition),this.offset.getValue()));
        Vec3 vec3To = transformUtils.toWorldVec3(level,transformUtils.vec3Below(transformUtils.getFront(Direction.DOWN,this.worldPosition),newOffset));
        boolean unReplaced = !level.getBlockState(level.clip(new ClipContext(vec3From,vec3To, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY,null)).getBlockPos()).getMaterial().isReplaceable();
        return !(original||unReplaced);
    }
}
