package io.github.xiewuzhiying.vs_addition.forge.mixin.create.hose_pulley;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.xiewuzhiying.vs_addition.util.TransformUtils;
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
public abstract class MixinHosePulleyBlockEntity extends KineticBlockEntity {

    @Shadow(remap = false)
    LerpedFloat offset;

    public MixinHosePulleyBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    @ModifyExpressionValue(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canBeReplaced()Z"))
    public boolean tick(boolean original, @Local(ordinal = 0) float newOffset){
        original = !original;
        Level level = this.level;
        Vec3 vec3From = TransformUtils.toWorldVec3(level, TransformUtils.vec3Below(TransformUtils.getFront(Direction.DOWN,this.worldPosition),this.offset.getValue()));
        Vec3 vec3To = TransformUtils.toWorldVec3(level, TransformUtils.vec3Below(TransformUtils.getFront(Direction.DOWN,this.worldPosition),newOffset));
        boolean unReplaced = !level.getBlockState(level.clip(new ClipContext(vec3From,vec3To, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY,null)).getBlockPos()).canBeReplaced();
        return !(original||unReplaced);
    }
}
